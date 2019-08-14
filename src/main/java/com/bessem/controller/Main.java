package com.bessem.controller;

import com.bessem.model.CourseIdea;
import com.bessem.model.CourseIdeaDao;
import com.bessem.model.CourseIdeaImpl;
import com.bessem.model.CourseIdeaNotFoundException;
import org.apache.log4j.BasicConfigurator;
import spark.ModelAndView;
import spark.Request;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class Main {

    private static final String FLASH_MESSAGE_KEY = "flash_message";
    private static final String MODEL_KEY = "model_key";

    public static void main(String[] args) {
        port(3000);
        BasicConfigurator.configure();
        staticFileLocation("/public");

        CourseIdeaDao ideaDao = new CourseIdeaImpl();

        Map<String, Object> model = new HashMap<>();

        before((req, res) -> {
            if (req.cookie("username") != null) {
                req.attribute("username", req.cookie("username"));
            }
            model.put("flashMessage", captureFlashMessage(req));
            req.attribute(MODEL_KEY, model);
        });

        before("/ideas", (req, res) -> {
            if (req.attribute("username") == null) {
                setFlashMessage(req, "Whoops, please sign in first!");
                res.redirect("/");
                halt();
            }
        });

        // index.hbs file is in resources/templates directory
        get("/", (req, res) -> {
            model.put("username", req.attribute("username"));
            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());

        post("/sign-in", (req, res) -> {
            String username = req.queryParams("username");
            model.put("username", username);
            res.cookie("username", username);
            res.redirect("/");
            return null;
        });

        get("/ideas", (req, res) -> {
            model.put("ideas", ideaDao.getAll());
            return new ModelAndView(model, "ideas.hbs");
        }, new HandlebarsTemplateEngine());

        post("/ideas", (req, res) -> {
            String title = req.queryParams("title");
            String author = req.attribute("username");
            CourseIdea idea = new CourseIdea(title, author);
            ideaDao.add(idea);

            res.redirect("/ideas");
            return null;
        });

        get("/ideas/:slug", (req, res) -> {
            CourseIdea idea = ideaDao.findBySlug(req.params("slug"));
            model.put("idea", idea);

            return new ModelAndView(model, "idea.hbs");
        }, new HandlebarsTemplateEngine());

        post("/ideas/:slug/vote", (req, res) -> {
            CourseIdea idea = ideaDao.findBySlug(req.params("slug"));
            //System.out.println(idea.getAuthor());
            boolean added = idea.vote(req.attribute("username"));
            if (added) {
                setFlashMessage(req, "Thanks for your vote!");
            } else {
                setFlashMessage(req, "You already voted!");
            }
            res.redirect("/ideas");
            return null;
        });

        exception(CourseIdeaNotFoundException.class, (exc, req, res) -> {
            res.status(404);
            model.put("exc", exc.getMessage());
            HandlebarsTemplateEngine engine = new HandlebarsTemplateEngine();
            String html = engine.render(
                    new ModelAndView(model, "not-found.hbs")
            );
            res.body(html);
        });
    }

    private static void setFlashMessage(Request req, String message) {
        req.session().attribute(FLASH_MESSAGE_KEY, message);
    }

    private static String getFlashMessage(Request req) {
        if (req.session(false) == null) {
            return null;
        }
        if (!req.session().attributes().contains(FLASH_MESSAGE_KEY)) {
            return null;
        }
        return (String) req.session().attribute(FLASH_MESSAGE_KEY);
    }

    private static String captureFlashMessage(Request req) {
        String message = getFlashMessage(req);
        if (message != null) {
            req.session().removeAttribute(FLASH_MESSAGE_KEY);
        }
        return message;
    }
}

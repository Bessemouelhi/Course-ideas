package com.bessem.controller;

import com.bessem.model.CourseIdea;
import com.bessem.model.CourseIdeaDao;
import com.bessem.model.CourseIdeaImpl;
import org.apache.log4j.BasicConfigurator;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class Main {

    public static void main(String[] args) {
        port(3000);
        BasicConfigurator.configure();
        staticFileLocation("/public");

        CourseIdeaDao ideaDao = new CourseIdeaImpl();

        Map map = new HashMap();

        before((req, res) -> {
            if (req.cookie("username") != null) {
                req.attribute("username", req.cookie("username"));
            }
        });

        before("/ideas", (req, res) -> {
            if (req.attribute("username") == null) {
                res.redirect("/");
                halt();
            }
        });

        // index.hbs file is in resources/templates directory
        get("/", (req, res) -> {
            map.put("username", req.attribute("username"));
            return new ModelAndView(map, "index.hbs");
        }, new HandlebarsTemplateEngine());

        post("/sign-in", (req, res) -> {
            String username = req.queryParams("username");
            map.put("username", username);
            res.cookie("username", username);
            res.redirect("/");
            return null;
        });

        get("/ideas", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
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
    }
}

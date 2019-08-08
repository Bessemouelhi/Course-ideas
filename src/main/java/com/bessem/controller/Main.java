package com.bessem.controller;

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

        Map map = new HashMap();
        map.put("username", "Bessem");

        // index.hbs file is in resources/templates directory
        get("/", (req, res) -> new ModelAndView(map, "index.hbs"),
                new HandlebarsTemplateEngine());

        post("/sign-in", (req, res) -> {
            map.put("username", req.queryParams("username"));
            return new ModelAndView(map, "sign-in.hbs");
        }, new HandlebarsTemplateEngine());
    }
}

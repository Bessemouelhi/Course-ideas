package com.bessem.controller;

import org.apache.log4j.BasicConfigurator;

import static spark.Spark.get;
import static spark.Spark.port;

public class Main {

    public static void main(String[] args) {
        port(3000);
        BasicConfigurator.configure();
        get("/home", (request, response) -> "Welcome Everybody!");
    }
}

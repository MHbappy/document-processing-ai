package com.bappy.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class DashboardController {
    @GetMapping("/api/test")
    String TestApi(@RequestParam(required = false) String data){
        log.error("There is an error in TestApi");
        log.info("There is an info in TestApi");
        log.debug("There is a debug info in TestApi");
        return "Hello World1!";
    }
}

package com.company.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.company.services.AuthService;

@RestController
@RequestMapping("/authenticate")
public class AuthController {

    @Autowired
    AuthService service;

    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    @RequestMapping(path = "/getUrl", method = RequestMethod.GET)
    public String getAuthUrl(){
        return service.getUrlForAuth();
    }

    @RequestMapping(path = "/addRawToken", method = RequestMethod.POST)
    public void addRawToken(@RequestBody String urlForParse){
        service.addToken(urlForParse);
    }
}

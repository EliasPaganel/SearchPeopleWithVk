package com.company.controllers;

import com.company.Pojos.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.company.services.MainService;


@RestController
@RequestMapping("/main")
public class MainController {

    @Autowired
    MainService service;

    private Logger logger = LoggerFactory.getLogger(MainController.class);

    @RequestMapping(value = "/search/people", method = RequestMethod.POST)
    public String run(@RequestBody UserDTO userDTO){
        return service.search(userDTO);
    }
}

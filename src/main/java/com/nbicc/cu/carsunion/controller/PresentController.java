package com.nbicc.cu.carsunion.controller;

import com.nbicc.cu.carsunion.constant.Authority;
import com.nbicc.cu.carsunion.service.PresentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/present")
@Authority
public class PresentController {
    @Autowired
    PresentService presentService;
}

package com.example.hrms.biz.request.controller;

import com.example.hrms.biz.request.model.Request;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/requests")
public class RequestController {

    @RequestMapping("")
    public String openRequestView(Model model) {
        model.addAttribute("requests", new Request());
        return "requests";
    }
    @RequestMapping("/create")
    public String openCreate1(Model model) {
        model.addAttribute("create1", new Request());
        return "create1";
    }
    @RequestMapping("/update/{requestId}")
    public String openupdaterequest(Model model) {
        model.addAttribute("updaterequest", new Request());
        return "updaterequest";
    }
}
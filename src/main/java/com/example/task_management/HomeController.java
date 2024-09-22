package com.example.task_management;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class HomeController {
    /* -- il trateaza ca pe un view
    @GetMapping("/")
    public String redirectToSwagger() {
        return "redirect:http://localhost:8080/swagger-ui/index.html";
    }
     */

    @GetMapping("/")
    public RedirectView redirectToSwagger() {
        return new RedirectView("/swagger-ui/index.html");
    }
}

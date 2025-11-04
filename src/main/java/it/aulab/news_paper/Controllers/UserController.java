package it.aulab.news_paper.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import it.aulab.news_paper.services.UserService;
import it.aulab.news_paper.Dtos.UserDto;



@Controller

public class UserController {
    @Autowired

    private UserService UserServices;

    @GetMapping("/") 
        public String home() 
        {
        return "home";
        }

    @GetMapping("/auth/register")
        public String register(Model model) 
        {
            model.addAttribute("user", new UserDto());
            return "auth/register";
        }
}


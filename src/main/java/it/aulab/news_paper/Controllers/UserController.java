package it.aulab.news_paper.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import it.aulab.news_paper.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import it.aulab.news_paper.Dtos.UserDto;
import it.aulab.news_paper.Models.User;



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

    @PostMapping("/register/save")
        public String registration(@Valid @ModelAttribute("user") UserDto userDto, 
                                    BindingResult result,
                                    Model model, 
                                    RedirectAttributes redirectAttributes,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {
        User existingUser = UserServices.findUserByEmail(userDto.getEmail());
        if (existingUser != null && existingUser.getEmail() != null && existingUser.getEmail().isEmpty()) {
            result.reject("email", null, "there is already an account registered with this email");
        }

        if (result.hasErrors()){
            model.addAttribute("user", userDto);
            return "auth/register";
        }

        UserServices.saveUser(userDto, redirectAttributes, request, response);
        redirectAttributes.addFlashAttribute("successMessage", "Registration Successful !");
        return "redirect:/auth/register?success";
    }
}


package it.aulab.news_paper.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Comparator;

import it.aulab.news_paper.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import it.aulab.news_paper.Dtos.ArticleDto;
import it.aulab.news_paper.Dtos.UserDto;
import it.aulab.news_paper.Models.User;
import it.aulab.news_paper.services.ArticleService;



@Controller

public class UserController {
    @Autowired
    private UserService UserServices;

    @Autowired
    private ArticleService articleService;

    @GetMapping("/") 
        public String home(Model viewModel)
        {
            List<ArticleDto> articles = articleService.readAll();

            Collections.sort(articles, Comparator.comparing(ArticleDto::getPublishDate).reversed());

            List<ArticleDto> lastThreeArticles = articles.stream().limit(3).collect(Collectors.toList());

            viewModel.addAttribute("articles", lastThreeArticles);

            return "home";
        }

    @GetMapping("/auth/register")
        public String register(Model model) 
        {
            model.addAttribute("user", new UserDto());
            return "auth/register";
        }

    @GetMapping("/auth/login")
        public String login() 
        {
            return "auth/login";
        }

    @PostMapping("/register/save")
        public String registration(@Valid @ModelAttribute("user") UserDto userDto, 
                                    BindingResult result,
                                    Model model, 
                                    RedirectAttributes redirectAttributes,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {
        User existingUser = UserServices.findUserByEmail(userDto.getEmail());
        if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
            result.reject("email", null, "there is already an account registered with this email");
        }

        System.out.println("Registration method called with email: " + userDto.getEmail());

        if (result.hasErrors()){
            model.addAttribute("user", userDto);
            return "auth/register";
        }

        UserServices.saveUser(userDto, redirectAttributes, request, response);
        redirectAttributes.addFlashAttribute("successMessage", "Registration Successful !");
        return "redirect:/";
    }

    @GetMapping("/search/{id}") 
    public String userArticleSearch(@PathVariable("id") Long id, Model viewModel) {
        User user = UserServices.find(id);
        viewModel.addAttribute("title", "All articles for user " + user.getUsername());

        List<ArticleDto> articles = articleService.searchByAuthor(user);
        viewModel.addAttribute("articles", articles);
        return "article/articles";
    }
}


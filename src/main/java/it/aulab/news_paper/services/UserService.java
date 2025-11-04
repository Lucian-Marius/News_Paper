package it.aulab.news_paper.services;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.aulab.news_paper.Dtos.UserDto;
import it.aulab.news_paper.Models.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {
    void saveUser (UserDto userDto, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response);

    User findUserByEmail (String email);
}
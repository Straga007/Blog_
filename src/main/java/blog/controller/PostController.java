package com.tomacat.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostController {
    @GetMapping // GET запрос /users
    public String users(Model model) {

        return "posts"; // Возвращаем название шаблона — users.html
    }
}

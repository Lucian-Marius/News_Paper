package it.aulab.news_paper.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.modelmapper.ModelMapper;

import it.aulab.news_paper.Dtos.CategoryDto;
import it.aulab.news_paper.Dtos.ArticleDto;
import it.aulab.news_paper.Models.Category;
import it.aulab.news_paper.services.ArticleService;
import it.aulab.news_paper.services.CategoryService;

@Controller
@RequestMapping("/categories")

public class CategoryController {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/search/{id}")
    public String categorySearch(@PathVariable("id") Long id, Model viewModel) {
        CategoryDto category = categoryService.read(id);
        
        viewModel.addAttribute("title", "Articles in category: " + category.getName());

        List<ArticleDto> articles = articleService.searchByCategory(modelMapper.map(category, Category.class));

        viewModel.addAttribute("articles", articles);

        return "article/articles";

    }
}

package it.aulab.news_paper.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import org.springframework.web.multipart.MultipartFile;


import it.aulab.news_paper.Dtos.CategoryDto;
import it.aulab.news_paper.Dtos.ArticleDto;
import it.aulab.news_paper.Models.Article;
import it.aulab.news_paper.Models.Category;
import it.aulab.news_paper.services.CrudService;
import it.aulab.news_paper.services.ArticleService;


@Controller
@RequestMapping("/articles")

public class ArticleController {
    @Autowired
    @Qualifier("categoryService")
    private CrudService<CategoryDto, Category, Long> categoryService;

    @Autowired
    private ArticleService articleService;

    @GetMapping
    private String articlesIndex(Model viewModel) {
        viewModel.addAttribute("title", "All articles");

        List<ArticleDto> articles = articleService.readAll();

        Collections.sort(articles, Comparator.comparing(ArticleDto::getPublishDate).reversed());
        viewModel.addAttribute("articles", articles);

        return "article/articles";
    }


    @GetMapping("create") 
    public String articleCreate(Model viewModel) {
        viewModel.addAttribute("title", "Create an article");
        viewModel.addAttribute("article", new Article());
        viewModel.addAttribute("category", categoryService.readAll());
        return "article/create";
    }

    @PostMapping
    public String articleStore(@Valid @ModelAttribute("article") Article article, 
                                BindingResult result,
                                RedirectAttributes redirectAttributes,
                                Principal principal,
                                @RequestParam("image") MultipartFile image,
                                Model viewModel) {
        System.out.println("[DEBUG] ArticleController: Received POST /articles");
        System.out.println("[DEBUG] ArticleController: Article title=" + article.getTitle() + ", subtitle=" + article.getSubtitle());
        System.out.println("[DEBUG] ArticleController: Image isEmpty=" + (image == null ? "null" : image.isEmpty()));
        if(result.hasErrors()){
            System.out.println("[DEBUG] ArticleController: Validation errors found");
            result.getFieldErrors().forEach(error -> {
                System.out.println("[DEBUG] Validation error - field: " + error.getField() + ", message: " + error.getDefaultMessage());
            });
            viewModel.addAttribute("title", "Create an article");
            viewModel.addAttribute("article", article);
            viewModel.addAttribute("category", categoryService.readAll());
            return "article/create";
        }
        articleService.create(article, principal, image);
        System.out.println("[DEBUG] ArticleController: ArticleService.create called");
        redirectAttributes.addFlashAttribute("successMessage", "Article created");
        return "redirect:/";
    }

    @GetMapping("detail/{id}")
    public String detailArticle(@PathVariable("id") Long id, Model viewModel) {
        viewModel.addAttribute("title", "Article Detail");
        viewModel.addAttribute("article", articleService.read(id));
        return "article/detail";
    }
}
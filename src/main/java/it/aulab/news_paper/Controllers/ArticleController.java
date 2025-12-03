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

import java.lang.ProcessBuilder.Redirect;
import java.security.Principal;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.springframework.web.multipart.MultipartFile;

import org.modelmapper.ModelMapper;

import it.aulab.news_paper.Dtos.CategoryDto;
import it.aulab.news_paper.Dtos.ArticleDto;
import it.aulab.news_paper.Models.Article;
import it.aulab.news_paper.Models.Category;
import it.aulab.news_paper.Repositories.ArticleRepository;
import it.aulab.news_paper.services.CrudService;
import it.aulab.news_paper.services.ArticleService;
import it.aulab.news_paper.Repositories.ImageRepository;


@Controller
@RequestMapping("/articles")

public class ArticleController {
    @Autowired
    @Qualifier("categoryService")
    private CrudService<CategoryDto, Category, Long> categoryService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ImageRepository imageRepository;

    @GetMapping
    private String articlesIndex(Model viewModel) {
        viewModel.addAttribute("title", "All articles");

        List<ArticleDto> articles = new ArrayList<ArticleDto>();
            for(Article article: articleRepository.findByIsAcceptedTrue()) {
                ArticleDto dto = modelMapper.map(article, ArticleDto.class);
                // Fetch and set image for this article
                it.aulab.news_paper.Models.Image image = imageRepository.findByArticleId(article.getId());
                dto.setImage(image);
                articles.add(dto);
            }

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

    @GetMapping("revisor/detail/{id}")
    public String revisorDetailArticle(@PathVariable("id") Long id, Model viewModel) {
        viewModel.addAttribute("title", "Article details");
        viewModel.addAttribute("article", articleService.read(id));
        return "revisor/detail";
    }

    @PostMapping("revisor/handle/{id}")
    public String handleArticle(@PathVariable("id") Long id,
                               @RequestParam("action") String action,
                               RedirectAttributes redirectAttributes) {
        if(action.equals("accept")) {
            articleService.setIsAccepted(true, id);
            redirectAttributes.addFlashAttribute("resultMessage", "Article accepted");
        } else if (action.equals("reject")) {
            articleService.setIsAccepted(false, id);
            redirectAttributes.addFlashAttribute("resultMessage", "Article rejected");
        } else {
            redirectAttributes.addFlashAttribute("resultMessage", "Incorrect action");
        }

        return "redirect:/revisor/dashboard";
    }

    @PostMapping("/accept")
    public String articleSetAccepted(@RequestParam("action") String action, @RequestParam("articleId") Long articleId, RedirectAttributes redirectAttributes) {
        if(action.equals("accept")) {
            articleService.setIsAccepted(true, articleId);
            redirectAttributes.addFlashAttribute("resultMessage", "Article accepted");
        } else if (action.equals("reject")) {
            articleService.setIsAccepted(false, articleId);
            redirectAttributes.addFlashAttribute("resultMessage", "Article rejected");
        } else {
            redirectAttributes.addFlashAttribute("resultMessage", "Incorrect action");
        }

        return "redirect:/revisor/dashboard";
    }
    
    
}
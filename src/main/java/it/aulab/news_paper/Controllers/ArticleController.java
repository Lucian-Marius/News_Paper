package it.aulab.news_paper.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.query.Param;
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
import java.util.Set;
import java.util.HashSet;
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

    @GetMapping("/search")
    public String articleSearch(@RequestParam("keyword") String keyword, Model viewModel) {
        viewModel.addAttribute("title", "Search results for: " + keyword);
        List<ArticleDto> articles = articleService.search(keyword);

        viewModel.addAttribute("articles", articles);

        return "article/articles";
    }

    @GetMapping
    private String articlesIndex(@RequestParam(value = "q", required = false) String query, Model viewModel) {
        System.out.println("[DEBUG] ArticleController: articlesIndex called with query: " + query);
        viewModel.addAttribute("title", "All articles");

        List<Article> articleEntities;
        if (query != null && !query.trim().isEmpty()) {
            System.out.println("[DEBUG] ArticleController: Performing search for query: " + query);
            // Search by title or author username
            List<Article> byTitle = articleRepository.findByTitleContainingIgnoreCaseAndIsAcceptedTrue(query);
            List<Article> byAuthor = articleRepository.findByUser_UsernameContainingIgnoreCaseAndIsAcceptedTrue(query);

            System.out.println("[DEBUG] ArticleController: byTitle: " + byTitle.size() + ", byAuthor: " + byAuthor.size());

            // Combine and remove duplicates
            Set<Article> combined = new HashSet<>();
            combined.addAll(byTitle);
            combined.addAll(byAuthor);
            articleEntities = new ArrayList<>(combined);
            System.out.println("[DEBUG] ArticleController: Combined search results: " + articleEntities.size());
        } else {
            articleEntities = articleRepository.findByIsAcceptedTrue();
            System.out.println("[DEBUG] ArticleController: Showing all articles: " + articleEntities.size());
        }

        List<ArticleDto> articles = new ArrayList<ArticleDto>();
        for(Article article: articleEntities) {
            ArticleDto dto = modelMapper.map(article, ArticleDto.class);
            // Fetch and set image for this article
            List<it.aulab.news_paper.Models.Image> images = imageRepository.findByArticleId(article.getId());
            if (!images.isEmpty()) {
                dto.setImage(images.get(0)); // Take the first image if multiple exist
            }
            articles.add(dto);
        }

        Collections.sort(articles, Comparator.comparing(ArticleDto::getPublishDate).reversed());
        viewModel.addAttribute("articles", articles);
        viewModel.addAttribute("query", query); // To display in the search box

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

    @GetMapping("/edit/{id}")
    public String editArticle(@PathVariable("id")Long id, Model viewModel, Principal principal) {
        ArticleDto article = articleService.read(id);
        if (!article.getUser().getEmail().equals(principal.getName())) {
            return "redirect:/writer/dashboard";
        }
        viewModel.addAttribute("title", "Article update");
        viewModel.addAttribute("article", article);
        viewModel.addAttribute("categories", categoryService.readAll());
        return "article/edit";
    }

    @PostMapping("/update/{id}")
    public String articleUpdate(@PathVariable("id")Long id,
                                @Valid @ModelAttribute("article") Article article,
                                BindingResult result,
                                RedirectAttributes redirectAttributes,
                                Principal principal,
                                MultipartFile file,
                                Model viewModel) {
        if (result.hasErrors()) {
            viewModel.addAttribute("title", "Article update");
            viewModel.addAttribute("article", article);
            viewModel.addAttribute("categories", categoryService.readAll());
            return "article/edit";
        }

        articleService.update(id, article, file);
        redirectAttributes.addFlashAttribute("successMessage", "Article updated");
        return "redirect:/articles";
    }

    @GetMapping("/delete/{id}")
    public String articleDelete(@PathVariable("id")Long id, RedirectAttributes redirectAttributes) {

        articleService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Article deleted");

        return "redirect:/writer/dashboard";
    }
}

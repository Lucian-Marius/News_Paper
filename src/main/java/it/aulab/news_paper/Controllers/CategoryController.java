package it.aulab.news_paper.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.modelmapper.ModelMapper;

import it.aulab.news_paper.Dtos.CategoryDto;
import it.aulab.news_paper.Dtos.ArticleDto;
import it.aulab.news_paper.Models.Category;
import it.aulab.news_paper.services.ArticleService;
import it.aulab.news_paper.services.CategoryService;
import jakarta.validation.Valid;

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

    @GetMapping("create")
    public String categoryCreate(Model viewModel) {
        viewModel.addAttribute("title", "Create category");
        viewModel.addAttribute("category", new Category());

        return "category/create";
    }

    @PostMapping
    public String categoryStore(@Valid @ModelAttribute("category") Category category, 
                                BindingResult result,
                                RedirectAttributes redirectAttributes,
                                Model viewModel) {
        if (result.hasErrors()) {
            viewModel.addAttribute("title", "Create a category");
            viewModel.addAttribute("category", category);
            return "category/create";
        }

        categoryService.create(category, null, null);
        redirectAttributes.addFlashAttribute("successMessage", "category created");
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/edit/{id}")
    public String categoryEdit(@PathVariable("id") Long id, Model viewModel) {
        viewModel.addAttribute("title", "Edit category");
        viewModel.addAttribute("category", categoryService.read(id));
        return "category/update";
    }

    @PostMapping("/update/{id}")
    public String categoryUpdate(@PathVariable("id") Long id,
                                 @Valid @ModelAttribute("category") Category category,
                                    BindingResult result,
                                    RedirectAttributes redirectAttributes,
                                    Model viewModel){


            if (result.hasErrors()) {
                viewModel.addAttribute("title", "Edit category");
                viewModel.addAttribute("category", category);
                return "category/update";
            }

    categoryService.update(id, category, null);
    redirectAttributes.addFlashAttribute("successMessage", "Updated");

    return "redirect:/admin/dashboard";
    }

    @GetMapping("delete/{id}")
    public String categoryDelete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        categoryService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Category deleted");
        return "redirect:/admin/dashboard";
    }
                                 
}

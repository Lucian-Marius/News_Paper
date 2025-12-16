package it.aulab.news_paper.services;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.apache.coyote.Response;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;

import it.aulab.news_paper.Dtos.CategoryDto;
import it.aulab.news_paper.Models.Article;
import it.aulab.news_paper.Models.Category;
import it.aulab.news_paper.Repositories.CategoryRepository;


@Service
public class CategoryService implements CrudService<CategoryDto, Category, Long>{

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Helper method to convert Category to CategoryDto with article count
     */
    private CategoryDto toDtoWithArticleCount(Category category) {
        CategoryDto dto = modelMapper.map(category, CategoryDto.class);
        dto.setNumberOfArticles(category.getArticles() != null ? category.getArticles().size() : 0);
        return dto;
    }

    @Override
    public List<CategoryDto> readAll(){
        List<CategoryDto> dtos = new ArrayList<CategoryDto>();
        for(Category category: categoryRepository.findAllWithArticles()){
            dtos.add(toDtoWithArticleCount(category));
        }
        return dtos;
    }

    @Override
    public CategoryDto read(Long key){
        Category category = categoryRepository.findById(key)
            .orElseThrow(() -> new RuntimeException("Category not found"));
        return toDtoWithArticleCount(category);
    }

    @Override
    public CategoryDto create(Category model, Principal principal, MultipartFile file) {
        return modelMapper.map(categoryRepository.save(model), CategoryDto.class);

    }

    @Override
    public CategoryDto update(Long key, Category model, MultipartFile file) {
        if (categoryRepository.existsById(key)){
            model.setId(key);
            return modelMapper.map(categoryRepository.save(model), CategoryDto.class);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional
    public void delete(Long id){
        if (categoryRepository.existsById(id)) {
            Category category = categoryRepository.findById(id).get();

            if (category.getArticles() != null) {
                Iterable<Article> articles = category.getArticles();
                    for (Article article: articles) {
                        article.setCategory(null);
                    }
            }

            categoryRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public List<CategoryDto> searchByCategory(Category category) {
        return new ArrayList<>();
    }
}

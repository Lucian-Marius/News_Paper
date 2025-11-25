package it.aulab.news_paper.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.modelmapper.ModelMapper;
import java.security.Principal;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.Optional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import it.aulab.news_paper.Models.Article;
import it.aulab.news_paper.services.ArticleService;
import it.aulab.news_paper.Repositories.UserRepository;
import it.aulab.news_paper.Repositories.ArticleRepository;
import it.aulab.news_paper.Models.User;
import it.aulab.news_paper.Dtos.ArticleDto;
import it.aulab.news_paper.services.CustomUserDetails;
import it.aulab.news_paper.services.ImageService;



@Service
public class ArticleService implements CrudService<ArticleDto, Article, Long> {

    

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private it.aulab.news_paper.Repositories.ImageRepository imageRepository;

    @Autowired 
    private ModelMapper modelMapper;

    @Override
    public List<ArticleDto> readAll() {
            List<ArticleDto> dtos = new ArrayList<>();
            for (Article article : articleRepository.findAll()) {
                ArticleDto dto = modelMapper.map(article, ArticleDto.class);
                // Fetch and set image for this article
                it.aulab.news_paper.Models.Image image = imageRepository.findByArticleId(article.getId());
                dto.setImage(image);
                dtos.add(dto);
            }
            return dtos;
    }


    @Override
    public ArticleDto create(Article article, Principal principal, MultipartFile image) {
        System.out.println("[DEBUG] ArticleService: create called");
        String url = " ";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = (userRepository.findById(userDetails.getId())).get();
            article.setUser(user);
            System.out.println("[DEBUG] ArticleService: User set to " + user.getEmail());
        }

        // Ensure both title and subtitle are set before saving
        if (article.getTitle() == null || article.getTitle().isEmpty()) {
            article.setTitle(article.getSubtitle()); // fallback if needed
            System.out.println("[DEBUG] ArticleService: Title fallback to subtitle");
        }

        Article savedArticle = articleRepository.save(article);
        System.out.println("[DEBUG] ArticleService: Article saved with ID " + savedArticle.getId());

        if (image != null && !image.isEmpty()) {
            System.out.println("[DEBUG] ArticleService: Image upload started");
            try {
                CompletableFuture<String> futureUrl = imageService.saveImageOnCloud(image);
                url = futureUrl.get();
                System.out.println("[DEBUG] ArticleService: Image uploaded to " + url);
                imageService.saveImageOnDB(url, savedArticle);
                System.out.println("[DEBUG] ArticleService: Image saved in DB");
            } catch (Exception e) {
                System.out.println("[DEBUG] ArticleService: Exception during image upload");
                e.printStackTrace();
            }
        } else {
            System.out.println("[DEBUG] ArticleService: No image uploaded");
        }

        ArticleDto dto = modelMapper.map(savedArticle, ArticleDto.class);
        return dto;
    }

  

    @Override
    public ArticleDto update(Long id, Article article, MultipartFile file) {
        // TODO: Implement update method
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void delete(Long id) {
        // TODO: Implement delete method
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public ArticleDto read(Long key) {
        Optional<Article> optArticle = articleRepository.findById(key);
        if (optArticle.isPresent()) {
            ArticleDto dto = modelMapper.map(optArticle.get(), ArticleDto.class);
            // Fetch and set image for this article
            it.aulab.news_paper.Models.Image image = imageRepository.findByArticleId(optArticle.get().getId());
            dto.setImage(image);
            return dto;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Article " + key + " not found");
        }
    }
}



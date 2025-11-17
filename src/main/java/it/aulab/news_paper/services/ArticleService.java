package it.aulab.news_paper.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.modelmapper.ModelMapper;
import java.security.Principal;
import java.util.List;

import it.aulab.news_paper.Models.Article;
import it.aulab.news_paper.services.ArticleService;
import it.aulab.news_paper.Repositories.UserRepository;
import it.aulab.news_paper.Repositories.ArticleRepository;
import it.aulab.news_paper.Models.User;
import it.aulab.news_paper.Dtos.ArticleDto;


@Service
public class ArticleService implements CrudService<ArticleDto, Article, Long> {

    @Autowired
    private UserRepository userRepository;

    @Autowired 
    private ModelMapper modelMapper;

    @Autowired
    private ArticleRepository articleRepository;


    @Override
    public ArticleDto create(Article article, Principal principal, MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = (userRepository.findById(userDetails.getId())).get();
            article.setUser(user);
        }

        ArticleDto dto = modelMapper.map(articleRepository.save(article), ArticleDto.class);

        return dto;
    }

    @Override
    public List<ArticleDto> readAll() {
        // TODO: Implement readAll method
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public ArticleDto read(Long id) {
        // TODO: Implement read method
        throw new UnsupportedOperationException("Not implemented yet");
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
}



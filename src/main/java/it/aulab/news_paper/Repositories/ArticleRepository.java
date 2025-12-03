package it.aulab.news_paper.Repositories;

import java.util.List;
import org.springframework.data.repository.ListCrudRepository;  

import it.aulab.news_paper.Models.Article;
import it.aulab.news_paper.Models.Category;
import it.aulab.news_paper.Models.User;


public interface ArticleRepository extends ListCrudRepository<Article, Long>
{
    List<Article> findByCategory(Category category);
    List<Article> findByUser(User user);
    List<Article> findByIsAcceptedTrue();
    List<Article> findByIsAcceptedFalse();
    List<Article> findByIsAcceptedIsNull();   
}

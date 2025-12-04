package it.aulab.news_paper.Repositories;

import java.util.List;
import org.springframework.data.repository.ListCrudRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
    List<Article> findByTitleContainingIgnoreCaseAndIsAcceptedTrue(String title);
    List<Article> findByBodyContainingIgnoreCaseAndIsAcceptedTrue(String body);
    List<Article> findByUser_UsernameContainingIgnoreCaseAndIsAcceptedTrue(String username);

    @Query("SELECT a FROM Article a WHERE a.isAccepted = true AND (" +
        "LOWER(a.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
        "LOWER(a.subtitle) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
        "LOWER(a.body) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
        "LOWER(a.user.username) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
        "LOWER(a.category.name) LIKE LOWER(CONCAT('%', :query, '%')))")

    List<Article> searchArticles(@Param("query") String query);
}

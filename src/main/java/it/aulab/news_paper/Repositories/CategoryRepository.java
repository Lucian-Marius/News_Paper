package it.aulab.news_paper.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import it.aulab.news_paper.Models.Category;

public interface CategoryRepository extends ListCrudRepository<Category, Long>{
    
    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.articles")
    List<Category> findAllWithArticles();
    
}

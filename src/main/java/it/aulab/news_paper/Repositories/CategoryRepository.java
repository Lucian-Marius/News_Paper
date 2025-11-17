package it.aulab.news_paper.Repositories;

import org.springframework.data.repository.ListCrudRepository;
import it.aulab.news_paper.Models.Category;

public interface CategoryRepository extends ListCrudRepository<Category, Long>{

}

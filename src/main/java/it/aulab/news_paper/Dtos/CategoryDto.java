package it.aulab.news_paper.Dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CategoryDto {
    private Long id;
    private String name;
    private Integer numberOfArticles;
}

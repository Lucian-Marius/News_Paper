package it.aulab.news_paper.Dtos;

import java.time.LocalDate;

import it.aulab.news_paper.Models.Category;
import it.aulab.news_paper.Models.Image;
import it.aulab.news_paper.Models.User;

import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class ArticleDto {
    private Long id;
    private String title;
    private String subtitle;
    private String body;
    private LocalDate publishDate;
    private User user;
    private Category category;
    private Image image;
}

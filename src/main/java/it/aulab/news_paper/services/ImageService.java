package it.aulab.news_paper.services;

import it.aulab.news_paper.Models.Article;
import org.springframework.web.multipart.MultipartFile;
import java.util.concurrent.CompletableFuture;
import java.io.IOException;

public interface ImageService {

    void saveImageOnDB(String url, Article article);
    CompletableFuture<String> saveImageOnCloud(MultipartFile file) throws Exception;
    void deleteImage(String imagePath) throws IOException;
}

package it.aulab.news_paper.services;

public interface EmailService {

    void sendSimpleEmail(String to, String subject, String text);

}

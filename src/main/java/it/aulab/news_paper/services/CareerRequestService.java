package it.aulab.news_paper.services;

import it.aulab.news_paper.Models.CareerRequest;
import it.aulab.news_paper.Models.User;

public interface CareerRequestService {

    Boolean isRoleAlreadyAssigned(User user, CareerRequest careerRequest);
    void save(CareerRequest careerRequest, User user);
    void careerAccept(Long requestId);
    CareerRequest find(Long id);
    
}

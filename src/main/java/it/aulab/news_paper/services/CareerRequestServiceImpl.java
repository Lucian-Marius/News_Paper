package it.aulab.news_paper.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.aulab.news_paper.Models.CareerRequest;
import it.aulab.news_paper.Repositories.CareerRequestRepository;
import it.aulab.news_paper.Models.User;

import java.util.List;

@Service
public class CareerRequestServiceImpl implements CareerRequestService {
    
    @Autowired
    private CareerRequestRepository careerRequestRepository;

    @Autowired
    private EmailService emailService;  
    
    @Transactional
    public Boolean isRoleAlreadyAssigned(User user, CareerRequest careerRequest) {
        
        List<Long>  allUserId = careerRequestRepository.findAllUserIds();

        if (!allUserId.contains(user.getId())) {

        return false;
        }

        List<Long> requests = careerRequestRepository.findByUserId(user.getId());

        return requests.stream().anyMatch(roleId -> roleId.equals(careerRequest.getRole().getId()));
    }
    
    @Transactional
    public void save(CareerRequest careerRequest, User user) {
        careerRequest.setUser(user);
        careerRequest.setIsChecked(false);
        careerRequestRepository.save(careerRequest);

        emailService.sendSimpleEmail("lucian.marius.work@gmail.com", "Role Request" + careerRequest.getRole().getName(), "There is a role request from " + user.getUsername());
    }

    @Override
    public void careerAccept(Long requestId){
        throw new UnsupportedOperationException("Unimplemented Method");
    }

    @Override
    public CareerRequest find(Long id) {
        throw new UnsupportedOperationException("Unimplemented Method");
    }
}

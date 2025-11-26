package it.aulab.news_paper.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.aulab.news_paper.Models.Role;
import it.aulab.news_paper.Models.CareerRequest;
import it.aulab.news_paper.Models.User;
import it.aulab.news_paper.Repositories.RoleRepository;
import it.aulab.news_paper.Repositories.CareerRequestRepository;
import it.aulab.news_paper.Repositories.UserRepository;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/operations")
public class OperationController {

    @Autowired  
    private RoleRepository roleRepository;

    @Autowired
    private CareerRequestRepository careerRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/career/request")
    public String careerRequestCreate(Model viewModel) {
        viewModel.addAttribute("title", "Insert your request");
        viewModel.addAttribute("careerRequest", new CareerRequest());

        List<Role> roles = roleRepository.findAll();

        roles.removeIf(e -> e.getName().equals("ROLE_USER"));
        viewModel.addAttribute("roles", roles);

        return "career/requestForm";
    }

    @PostMapping("/career/request/save")
    public String careerRequestSave(CareerRequest careerRequest, Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        careerRequest.setUser(user);
        careerRequestRepository.save(careerRequest);
        return "redirect:/";
    }
}


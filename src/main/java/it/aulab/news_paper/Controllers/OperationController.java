package it.aulab.news_paper.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.aulab.news_paper.services.CareerRequestService;
import it.aulab.news_paper.Models.Role;
import it.aulab.news_paper.Models.CareerRequest;
import it.aulab.news_paper.Models.User;
import it.aulab.news_paper.Repositories.RoleRepository;
import it.aulab.news_paper.Repositories.UserRepository;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/operations")
public class OperationController {

    @Autowired  
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CareerRequestService careerRequestService;

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
    public String careerRequestSave(@ModelAttribute("careerRequest") CareerRequest careerRequest, 
                                     Principal principal, 
                                     RedirectAttributes redirectAttributes) {
        User user = userRepository.findByEmail(principal.getName());

        if (careerRequestService.isRoleAlreadyAssigned(user, careerRequest)) {
            redirectAttributes.addFlashAttribute("errorMessage", "You are already assigned to this role");
            return "redirect:/";
        }

        careerRequestService.save(careerRequest, user);
        redirectAttributes.addFlashAttribute("successMessage", "Your request was sent");

        return "redirect:/";
    }

    @GetMapping("/career/request/detail/{id}")
    public String careerRequestDetail(@PathVariable("id") Long id, Model viewModel) {
        viewModel.addAttribute("title", "Request details");
        viewModel.addAttribute("request", careerRequestService.find(id));
        return "career/careerDetail";
    }

    @PostMapping("/career/request/accept/{requestId}")
    public String careerRequestAccept(@PathVariable Long requestId, RedirectAttributes redirectAttributes) {
        careerRequestService.careerAccept(requestId);
        redirectAttributes.addFlashAttribute("successMessage", "Role granted");

        return "redirect:/admin/dashboard";
    }
    
}

package com.example.TravelPlanner.registration;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;
@Controller
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;
    @GetMapping(path = "/registration")
    public String showRegisterPage() {return "register";}

    @PostMapping(path = "/registration")
    public String register(@RequestParam Map<String, String> request, RedirectAttributes redirectAttributes) {
        RegistrationRequest registrationRequest = new RegistrationRequest(
                request.get("firstName"), request.get("lastName"),request.get("email"),request.get("password"));
        String endpoint;
        try {
            registrationService.register(registrationRequest);
            redirectAttributes.addFlashAttribute("success", "Sign up Complete!");
            endpoint="redirect:login";
        }
        catch (Exception ex)
        {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            endpoint="redirect:registration";
        }
        return endpoint;
    }
}

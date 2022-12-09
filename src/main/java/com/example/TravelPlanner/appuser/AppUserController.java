package com.example.TravelPlanner.appuser;

import com.example.TravelPlanner.Event.EventService;
import com.example.TravelPlanner.registration.RegistrationRequest;
import com.example.TravelPlanner.registration.RegistrationService;
import com.example.TravelPlanner.travel.Travel;
import com.example.TravelPlanner.travel.TravelService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
//@RequestMapping(path = "registration")
@AllArgsConstructor
public class AppUserController {

    private final AppUserService appUserService;
    private final EventService eventService;
    private final TravelService travelService;


    @GetMapping(path = "/home")
    public String showHomePage(Authentication authentication, Model model){
        AppUser currentUser = appUserService.getLoggedUser(authentication);
        travelService.setAllTransientTraveParams(currentUser.getId());
        model.addAttribute("user", currentUser);

        return "home";
    }
}

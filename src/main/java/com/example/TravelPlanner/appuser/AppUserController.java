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

    private final RegistrationService registrationService;
    private final AppUserService appUserService;
    private final EventService eventService;
    private final TravelService travelService;

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

    @GetMapping(path = "/login")
    public String showLoginPage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        String errorMessage = null;
        if (session != null) {
            AuthenticationException ex = (AuthenticationException) session
                    .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (ex != null) {
                errorMessage = ex.getMessage();
            }
        }
        model.addAttribute("error", errorMessage);
        return "login";
    }

/*    @PostMapping(path = "/home")
    public String HomePage(Authentication authentication){
        authentication = SecurityContextHolder.getContext().getAuthentication();
        authentication.getName();
        return "home";
    }*/

    @GetMapping(path = "/home")
    public String showHomePage(Authentication authentication, Model model){
        authentication = SecurityContextHolder.getContext().getAuthentication(); //get data about logged user
        String email = authentication.getPrincipal().toString();
        UserDetails currentUser = appUserService.loadUserByUsername(email);
        AppUser user = (AppUser) currentUser;
        travelService.setAllStartEndTravelDates(((AppUser)currentUser).getId());
        model.addAttribute("user", currentUser);

/*        List<Travel> travelList = travelService.getAllTravels(((AppUser) currentUser).getId());
        for(Travel travel : travelList)
        {
            travel.setStartDate(eventService);
            travel.setEndDate();
        }
        model.addAttribute("travels", travelList);*/
        return "home";
    }


}

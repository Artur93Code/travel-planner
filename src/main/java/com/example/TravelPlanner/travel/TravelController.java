package com.example.TravelPlanner.travel;

import com.example.TravelPlanner.appuser.AppUser;
import com.example.TravelPlanner.appuser.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@AllArgsConstructor
public class TravelController {
    private final AppUserService appUserService;
    private final TravelService travelService;

    @PostMapping(path = "/travel/add")
    public String addNewTravel(@RequestParam Map<String, String> request, Model model, Authentication authentication)
    {
        AppUser currentUser = appUserService.getLoggedUser(authentication);
        travelService.addTravel(request.get("title"), request.get("description"), currentUser);

        travelService.setAllTransientTraveParams(currentUser.getId());
        model.addAttribute("user", currentUser);
        return "home";
    }
}

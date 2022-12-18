package com.example.TravelPlanner.travel;

import com.example.TravelPlanner.appuser.AppUser;
import com.example.TravelPlanner.appuser.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@AllArgsConstructor
public class TravelController {
    private final AppUserService appUserService;
    private final TravelService travelService;
    //private final TravelRepository travelRepository; //test

    @PostMapping(path = "/travel/add")
    public String addNewTravel(@RequestParam Map<String, String> request, Model model, Authentication authentication)
    {
        AppUser currentUser = appUserService.getLoggedUser(authentication);
        travelService.addTravel(request.get("title"), request.get("description"), currentUser);

        travelService.setAllTransientTraveParams(currentUser.getId());
        model.addAttribute("user", currentUser);
        return "redirect:/home";
    }

    @PostMapping(path = "/travel/delete/{travelId}")
    public String deleteTravel(@PathVariable Long travelId, Model model, Authentication authentication)
    {
        AppUser currentUser = appUserService.getLoggedUser(authentication);

        travelService.deleteTravel(currentUser, travelId);

        travelService.setAllTransientTraveParams(currentUser.getId());
        model.addAttribute("user", currentUser);
        return "redirect:/home";
    }
}

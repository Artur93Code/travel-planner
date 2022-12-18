package com.example.TravelPlanner.travel;

import com.example.TravelPlanner.appuser.AppUser;
import com.example.TravelPlanner.appuser.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "api/travel")
@AllArgsConstructor
public class TravelAPIController {

    private final AppUserService appUserService;
    private final TravelService travelService;

    @PostMapping(path = "/add")
    public String addNewTravel(@RequestBody Map<String, String> request, /*Model model,*/ Authentication authentication)
    {
        try {
            AppUser currentUser = appUserService.getLoggedUser(authentication);
            travelService.addTravel(request.get("title"), request.get("description"), currentUser);
        }
        catch (Exception e){
            throw e;
        }

        return "Success! Travel added";
    }

    @DeleteMapping(path = "/delete/{travelId}")
    public String deleteTravel(@PathVariable Long travelId, Model model, Authentication authentication)
    {
        AppUser currentUser = appUserService.getLoggedUser(authentication);

        travelService.deleteTravel(currentUser, travelId);

        return "Success! Travel "+travelId+" deleted";
    }
}

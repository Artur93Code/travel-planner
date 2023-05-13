package com.example.TravelPlanner.travel;

import com.example.TravelPlanner.Event.EventType;
import com.example.TravelPlanner.appuser.AppUser;
import com.example.TravelPlanner.appuser.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
public class TravelController {
    private final AppUserService appUserService;
    private final TravelService travelService;
    //private final TravelRepository travelRepository; //test

    @PostMapping(path = "/travel/add")
    public ModelAndView addNewTravel(@RequestParam Map<String, String> request, Model model, Authentication authentication)
    {
        ModelAndView modelAndView = new ModelAndView("home");
        AppUser currentUser = appUserService.getLoggedUser(authentication);
        travelService.addTravel(request.get("title"), request.get("description"), currentUser);

        travelService.setAllTransientTravelParams(currentUser.getId());
        model.addAttribute("user", currentUser);
        model.addAttribute("success", "Travel added!");
        return modelAndView;
    }

    @PostMapping(path = "/travel/delete/{travelId}")
    public String deleteTravel(@PathVariable Long travelId, Model model, Authentication authentication)
    {
        AppUser currentUser = appUserService.getLoggedUser(authentication);

        travelService.deleteTravel(currentUser, travelId);

        travelService.setAllTransientTravelParams(currentUser.getId());
        model.addAttribute("user", currentUser);
        return "redirect:/home";
    }

    @GetMapping(path = "/travel/details/{travelId}")
    public String showDetailsTravel(@PathVariable Long travelId, Model model, Authentication authentication)
    {
        try {
            AppUser currentUser = appUserService.getLoggedUser(authentication);
            Travel currentTravel = travelService.getCustomTravel(travelId);

            travelService.setAllTransientTravelParams(currentUser.getId());
            model.addAttribute("user", currentUser);
            model.addAttribute("travel", currentTravel);
        }
        catch (Exception e){}

        return "travel";
    }

    @PostMapping(path="/travel/update/{travelId}")
    public ModelAndView updateEvent(@PathVariable Long travelId, @RequestParam Map<String, String> request, Model model,
                                    Authentication authentication, RedirectAttributes redirectAttributes) {

        ModelAndView modelAndView = new ModelAndView("home");
        AppUser currentUser = appUserService.getLoggedUser(authentication);
        List<Travel> userTravels =currentUser.getTravels();


        if(userTravels != null) {
            try {
                //long travelId = Long.parseLong(request.get("travelId"));
                String title = request.get("travelEditTitle");
                String description = request.get("travelEditDescription");

                travelService.updateTravel(currentUser,travelId, title, description);
            }
            catch (Exception e)
            {
                travelService.setAllTransientTravelParams(currentUser.getId());
                model.addAttribute("user", currentUser);
                modelAndView.addObject("error", e.getMessage());
                return modelAndView;
            }
        }

        travelService.setAllTransientTravelParams(currentUser.getId());
        modelAndView.addObject("success", "Travel edited!");
        model.addAttribute("user", currentUser);
        return modelAndView;

    }
}

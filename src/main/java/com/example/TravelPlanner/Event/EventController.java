package com.example.TravelPlanner.Event;

import com.example.TravelPlanner.appuser.AppUser;
import com.example.TravelPlanner.appuser.AppUserService;
import com.example.TravelPlanner.travel.TravelService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class EventController {

    private final AppUserService appUserService;
    private final TravelService travelService;

    private final EventService eventService;

    @PostMapping(path = "/event/delete/{eventId}")
    public String deleteEvent(@PathVariable Long eventId, Model model, Authentication authentication)
    {
        AppUser currentUser = appUserService.getLoggedUser(authentication);

        eventService.deleteEvent(currentUser, eventId);

        travelService.setAllTransientTraveParams(currentUser.getId());
        model.addAttribute("user", currentUser);
        return "redirect:/home";
    }
}

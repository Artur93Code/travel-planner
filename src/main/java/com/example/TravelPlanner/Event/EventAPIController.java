package com.example.TravelPlanner.Event;

import com.example.TravelPlanner.appuser.AppUser;
import com.example.TravelPlanner.appuser.AppUserService;
import com.example.TravelPlanner.travel.TravelService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/event")
@AllArgsConstructor
public class EventAPIController {

    private final AppUserService appUserService;
    private final EventService eventService;

    @DeleteMapping(path = "/delete/{eventId}")
    public String deleteTravel(@PathVariable Long eventId, Model model, Authentication authentication)
    {
        AppUser currentUser = appUserService.getLoggedUser(authentication);

        eventService.deleteEvent(currentUser, eventId);

        return "Success! Event "+eventId+" deleted";
    }

}

package com.example.TravelPlanner.Event;

import com.example.TravelPlanner.appuser.AppUser;
import com.example.TravelPlanner.appuser.AppUserService;
import com.example.TravelPlanner.travel.Travel;
import com.example.TravelPlanner.travel.TravelService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api/event")
@AllArgsConstructor
public class EventAPIController {

    private final AppUserService appUserService;
    private final EventService eventService;

    @DeleteMapping(path = "/delete/{eventId}")
    public String deleteEvent(@PathVariable Long eventId, Model model, Authentication authentication)
    {
        AppUser currentUser = appUserService.getLoggedUser(authentication);

        eventService.deleteEvent(currentUser, eventId);

        return "Success! Event "+eventId+" deleted";
    }

    @PostMapping(path = "/add")
    public String addNewEvent(@RequestBody Map<String, String> request, Authentication authentication) {
        try {
            AppUser currentUser = appUserService.getLoggedUser(authentication);
            List<Travel> userTravels = currentUser.getTravels();
            //Check if logged user is owner of the travel entity which he wants to add event entity. And if he is then get this travel entity
            Travel currentTravel = userTravels.stream().filter(o -> o.getId().equals(Long.parseLong(request.get("eventTravelId"))))
                    .findFirst()
                    .get();
            if (currentTravel != null) {
                EventType eventType = EventType.valueOf(request.get("eventType"));
                LocalDateTime startDate = LocalDateTime.parse(request.get("eventStartDate"));
                LocalDateTime endDate = LocalDateTime.parse(request.get("eventEndDate"));
                eventService.addEvent(request.get("eventTitle"), eventType,
                        startDate, endDate, Double.parseDouble(request.get("eventCost")), currentTravel);
            }
        } catch (Exception e) {
            throw e;
        }
        return "Success! Event "+request.get("eventTitle")+" added";
    }

}

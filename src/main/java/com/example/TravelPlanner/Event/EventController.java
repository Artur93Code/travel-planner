package com.example.TravelPlanner.Event;

import com.example.TravelPlanner.appuser.AppUser;
import com.example.TravelPlanner.appuser.AppUserService;
import com.example.TravelPlanner.travel.Travel;
import com.example.TravelPlanner.travel.TravelRepository;
import com.example.TravelPlanner.travel.TravelService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @PostMapping(path = "/event/add")
    public String addNewEvent(@RequestParam Map<String, String> request, Model model, Authentication authentication)
    {
        AppUser currentUser = appUserService.getLoggedUser(authentication);
        List<Travel> userTravels =currentUser.getTravels();
        //Check if logged user is owner of the travel entity which he wants to add event entity. And if he is then get this travel entity
        Travel currentTravel = userTravels.stream().filter(o -> o.getId().equals(Long.parseLong(request.get("eventTravelId"))))
                .findFirst()
                .get();
        if(currentTravel != null) {
            EventType eventType = EventType.valueOf(request.get("eventType"));
            LocalDateTime startDate = LocalDateTime.parse(request.get("eventStartDate"));
            LocalDateTime endDate = LocalDateTime.parse(request.get("eventEndDate"));
            eventService.addEvent(request.get("eventTitle"), eventType,
                    startDate, endDate, Double.parseDouble(request.get("eventCost")), currentTravel);
        }

        travelService.setAllTransientTraveParams(currentUser.getId());
        model.addAttribute("user", currentUser);
        return "redirect:/home";
    }
}

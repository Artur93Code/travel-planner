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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.DecimalFormat;
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

        travelService.setAllTransientTravelParams(currentUser.getId());
        model.addAttribute("user", currentUser);
        return "redirect:/home";
    }

    @PostMapping(path = "/event/add")
    public ModelAndView addNewEvent(@RequestParam Map<String, String> request, Model model,
                                    Authentication authentication, RedirectAttributes redirectAttributes)
    {
        ModelAndView modelAndView = new ModelAndView("travel");
        AppUser currentUser = appUserService.getLoggedUser(authentication);
        List<Travel> userTravels =currentUser.getTravels();
        //Check if logged user is owner of the travel entity which he wants to add event entity. And if he is then get this travel entity
        Travel currentTravel = userTravels.stream().filter(o -> o.getId().equals(Long.parseLong(request.get("eventTravelId"))))
                .findFirst()
                .get();
        if(currentTravel != null) {
            try {
                EventType eventType = EventType.valueOf(request.get("eventType"));
                LocalDateTime startDate = LocalDateTime.parse(request.get("eventStartDate"));
                LocalDateTime endDate = LocalDateTime.parse(request.get("eventEndDate"));

                eventService.addEvent(request.get("eventTitle"), eventType,
                        startDate, endDate, Double.parseDouble(request.get("eventCost")), currentTravel);
            }
            catch (Exception e)
            {
                travelService.setAllTransientTravelParams(currentUser.getId());
                model.addAttribute("user", currentUser);
                model.addAttribute("travel", currentTravel);
                //modelAndView.addObject("error", "Something went wrong. Please try again");
                modelAndView.addObject("error", e.getMessage());
                return modelAndView;
            }
        }

        travelService.setAllTransientTravelParams(currentUser.getId());
        modelAndView.addObject("success", "Event added!");//test
        model.addAttribute("user", currentUser);
        model.addAttribute("travel", currentTravel);
        return modelAndView;
    }

    @PostMapping(path="/event/update/{eventId}")
    public ModelAndView updateEvent(@PathVariable Long eventId, @RequestParam Map<String, String> request, Model model,
                            Authentication authentication, RedirectAttributes redirectAttributes) {

        ModelAndView modelAndView = new ModelAndView("travel");
        AppUser currentUser = appUserService.getLoggedUser(authentication);
        List<Travel> userTravels =currentUser.getTravels();
        //Check if logged user is owner of the travel entity which he wants to add event entity. And if he is then get this travel entity
        Travel currentTravel = userTravels.stream().filter(o -> o.getId().equals(Long.parseLong(request.get("eventEditTravelId"))))
                .findFirst()
                .get();

        if(currentTravel != null) {
            try {
                //long eventId = Long.parseLong(request.get("eventEditId"));
                EventType eventType = EventType.valueOf(request.get("eventEditType"));
                LocalDateTime startDate = LocalDateTime.parse(request.get("eventEditStartDate"));
                LocalDateTime endDate = LocalDateTime.parse(request.get("eventEditEndDate"));

                eventService.updateEvent(eventId,request.get("eventEditTitle"), eventType,
                        startDate, endDate, Double.parseDouble(request.get("eventEditCost")), currentTravel);
            }
            catch (Exception e)
            {
                travelService.setAllTransientTravelParams(currentUser.getId());
                model.addAttribute("user", currentUser);
                model.addAttribute("travel", currentTravel);
                modelAndView.addObject("error", e.getMessage());
                return modelAndView;
            }
        }

        travelService.setAllTransientTravelParams(currentUser.getId());
        modelAndView.addObject("success", "Event edited!");
        model.addAttribute("travel", currentTravel);
        model.addAttribute("user", currentUser);
        return modelAndView;

    }
}

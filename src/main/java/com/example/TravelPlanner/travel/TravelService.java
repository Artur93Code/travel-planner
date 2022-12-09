package com.example.TravelPlanner.travel;

import com.example.TravelPlanner.Event.Event;
import com.example.TravelPlanner.Event.EventRepository;
import com.example.TravelPlanner.Event.EventService;
import com.example.TravelPlanner.appuser.AppUser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class TravelService {

    private final TravelRepository travelRepository;
    private final EventService eventService;


    public List<Travel> getAllTravels(Long appUserId){
        return  travelRepository.findByAppUserId(appUserId);
    }

    public void  setAllTransientTraveParams(Long appUserId)
    {
        List<Travel> travelList = travelRepository.findByAppUserId(appUserId);
        for (Travel travel : travelList)
        {
            travel.setStartDate();
            travel.setEndDate();
            travel.setTotalCost();
        }
    }

/*    public void updateStartDate(Long travelId){
        List<Event> eventList = eventService.getAllEvents(travelId);
        LocalDateTime startTravelDate = null;
        for (Event event : eventList)
        {
            if((startTravelDate==null) || (event.getStartDate().isBefore(startTravelDate)))
            {
                startTravelDate = event.getStartDate();
            }
        }
        //travelRepository.updateStartDate(startTravelDate, travelId);
    }*/

    public void addTravel(String title, String description, AppUser appUser)
    {
        Travel newTravel = new Travel(title, description, appUser);
        travelRepository.save(newTravel);
    }
}

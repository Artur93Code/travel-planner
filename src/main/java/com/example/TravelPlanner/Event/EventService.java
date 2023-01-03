package com.example.TravelPlanner.Event;

import com.example.TravelPlanner.appuser.AppUser;
import com.example.TravelPlanner.travel.Travel;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public List<Event> getAllEvents(Long travelId){
        return  eventRepository.findByTravelId(travelId);
    }

    public void deleteEvent(AppUser currentUser, Long eventId)
    {
        try {
            boolean isExist = false;
            List<Travel> travelList = currentUser.getTravels();
            for(Travel travel : travelList)
            {
                List<Event> eventList = travel.getEvents();
                isExist = eventList.stream().filter(o -> o.getId().equals(eventId)).findFirst().isPresent();
                //if find event to delete then exit from for loop
                if(isExist)
                {
                    break;
                }
            }
            if (isExist) {
                eventRepository.deleteById(eventId);
            }
            else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "You don't have permission to delete this event or it's not exist");
            }
        }
        catch (ResponseStatusException e)
        {
            if(e.getStatus().equals(HttpStatus.NOT_FOUND)){
                throw e;
            }
            else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong", e);
            }
        }
    }

}

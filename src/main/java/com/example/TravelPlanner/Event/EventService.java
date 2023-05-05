package com.example.TravelPlanner.Event;

import com.example.TravelPlanner.appuser.AppUser;
import com.example.TravelPlanner.travel.Travel;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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

    public void addEvent(String title, EventType eventType, LocalDateTime startDate, LocalDateTime endDate,
                          Double cost, Travel travel)
    {
            //EventType eventType1 = EventType.ACCOMMODATION;
        try {
            Exception ex;
            if (startDate.isAfter(endDate)) {
                ex = new Exception("Start date must be before end date");
                throw ex;
            }
            Event newEvent = new Event(title, eventType, startDate, endDate, cost, travel);
            eventRepository.save(newEvent);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateEvent(long id, String title, EventType eventType, LocalDateTime startDate, LocalDateTime endDate,
                         Double cost, Travel travel)
    {
        Optional <Event> findEvent = eventRepository.findById(id);
        if(findEvent.isPresent()){
            Event editedEvent = findEvent.get();
            editedEvent.setTitle(title);
            editedEvent.setEventType(eventType);
            editedEvent.setStartDate(startDate);
            editedEvent.setEndDate(endDate);
            editedEvent.setCost(cost);
            editedEvent.setTravel(travel);

            eventRepository.save(editedEvent);
        }
    }

}

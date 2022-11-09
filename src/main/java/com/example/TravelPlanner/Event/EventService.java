package com.example.TravelPlanner.Event;

import com.example.TravelPlanner.travel.Travel;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public List<Event> getAllEvents(Long travelId){
        return  eventRepository.findByTravelId(travelId);
    }

}

package com.example.TravelPlanner.Event;

import com.example.TravelPlanner.travel.Travel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    Optional<Event> findById(int id);

    //List<Object> findByIdIn(Long travel_id);
    List<Event> findByTravelId(Long travelId);

    void deleteById(Long id);
}

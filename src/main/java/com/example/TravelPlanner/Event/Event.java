package com.example.TravelPlanner.Event;

import com.example.TravelPlanner.appuser.AppUser;
import com.example.TravelPlanner.travel.Travel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Event {
    @SequenceGenerator(
            name="event_sequence",
            sequenceName = "event_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "event_sequence"
    )
    private Long id;

    private String title;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Double cost;

    @ManyToOne
    @JoinColumn(nullable = false, name = "travel_id")
    private Travel travel;


    public Event(String title, EventType eventType, LocalDateTime startDate, LocalDateTime endDate, Double cost, Travel travel) {
        this.title = title;
        this.eventType = eventType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.cost = cost;
        this.travel = travel;
    }
}

package com.example.TravelPlanner.travel;


import com.example.TravelPlanner.Event.Event;
import com.example.TravelPlanner.Event.EventService;
import com.example.TravelPlanner.appuser.AppUser;
import com.example.TravelPlanner.appuser.AppUserRole;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Travel {
    @SequenceGenerator(
            name="travel_sequence",
            sequenceName = "travel_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "travel_sequence"
    )
    private Long id;

    private String title;

    private String description;

    @Transient
    private LocalDateTime startDate;

    @Transient
    private LocalDateTime endDate;

    @Transient
    private Double totalCost;

    @ManyToOne
    @JoinColumn(nullable = false, name = "app_user_id")
    private AppUser appUser;

    @OneToMany(mappedBy = "travel")
    private List<Event> events;

    public Travel(String title, String description, AppUser appUser) {
        this.title = title;
        this.description = description;
        this.appUser = appUser;
    }

/*    public Travel(String title, String description, AppUser appUser, List events) {
        this.title = title;
        this.description = description;
        this.appUser = appUser;
        this.events = events;
    }*/

    public void setStartDate() {
        LocalDateTime startTravelDate = null;
        if(this.getEvents()!=null) {
            List<Event> eventList = this.getEvents();
            for (Event event : eventList) {
                if ((startTravelDate == null) || (event.getStartDate().isBefore(startTravelDate))) {
                    startTravelDate = event.getStartDate();
                }
            }
        }
        this.startDate = startTravelDate;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate() {
        //List<Event> eventList = eventService.getAllEvents(id);
        LocalDateTime endTravelDate = null;
        if(this.getEvents()!=null) {
            List<Event> eventList = this.getEvents();
            for (Event event : eventList)
            {
                if((endTravelDate==null) || (event.getEndDate().isBefore(endTravelDate)))
                {
                    endTravelDate = event.getEndDate();
                }
            }

        }
        this.endDate = endTravelDate;
    }

    public void setTotalCost() {
        double totalCost = 0d;
        if (this.getEvents() != null) {
            List<Event> eventList = this.getEvents();
            for (Event event : eventList) {
                totalCost += event.getCost();
            }
        }
        this.totalCost = totalCost;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}

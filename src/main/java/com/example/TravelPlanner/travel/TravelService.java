package com.example.TravelPlanner.travel;

import com.example.TravelPlanner.Event.Event;
import com.example.TravelPlanner.Event.EventRepository;
import com.example.TravelPlanner.Event.EventService;
import com.example.TravelPlanner.appuser.AppUser;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class TravelService {

    private final TravelRepository travelRepository;
    private final EventService eventService;


    public List<Travel> getAllTravels(Long appUserId){
        return  travelRepository.findByAppUserId(appUserId);
    }

    public Travel getCustomTravel(Long travelId) throws Exception {

        Optional<Travel> optionalTravel = travelRepository.findById(travelId);
        try{
            if(optionalTravel.isEmpty()){
                //Travel customTravel = optionalTravel.get();
                throw new Exception("Travel not exist");
            }
        }
        catch(Exception e){
            throw e;
        }
        return optionalTravel.get();
    }

    public void  setAllTransientTravelParams(Long appUserId)
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

    public void deleteTravel(AppUser currentUser, Long travelId)
    {
        try {
            List<Travel> travelList = currentUser.getTravels();

            //Checks if logged user is the owner (parent) of the travel entity (child) who want delete
            boolean checkOwner = travelList.stream().filter(o -> o.getId().equals(travelId)).findFirst().isPresent();
            if (checkOwner) {
                travelRepository.deleteById(travelId);
            }
            else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "You don't have permission to delete this travel or it's not exist");
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

package com.example.TravelPlanner;

import com.example.TravelPlanner.Event.Event;
import com.example.TravelPlanner.Event.EventRepository;
import com.example.TravelPlanner.Event.EventService;
import com.example.TravelPlanner.Event.EventType;
import com.example.TravelPlanner.appuser.AppUser;
import com.example.TravelPlanner.appuser.AppUserRepository;
import com.example.TravelPlanner.appuser.AppUserRole;
import com.example.TravelPlanner.travel.Travel;
import com.example.TravelPlanner.travel.TravelRepository;
import com.example.TravelPlanner.travel.TravelService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class TravelPlanner {

	public static void main(String[] args) {
		SpringApplication.run(TravelPlanner.class, args);
	}

	//Create pre-defined entities - users, travels and events
	@Bean
	CommandLineRunner createTestUsers(AppUserRepository appUserRepository,
									  TravelRepository travelRepository,
									  EventRepository eventRepository,
									  BCryptPasswordEncoder bCryptPasswordEncoder,
									  EventService eventService)
	{
		return args -> {
			AppUser testUser = new AppUser("Krzysztof",
					"Kowalski",
					"email@wp.pl",
					bCryptPasswordEncoder.encode("test"),
					AppUserRole.USER);
			testUser.setEnabled(true);
			appUserRepository.save(testUser);

			Travel testTravel = new Travel("Journey to London",
					"My first journey to UK with my friends",
					testUser);
			travelRepository.save(testTravel);

			Event testEvent = new Event("Visit on London Brigde",
					EventType.SIGHTSEEING,
					LocalDateTime.now(),
					LocalDateTime.now().plusHours(2),
					130d,
					testTravel);
			eventRepository.save(testEvent);

			//testTravel.setStartDate(eventService);
			//testTravel.setEndDate();

		};
	}
}

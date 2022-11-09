package com.example.TravelPlanner.security.config;

import com.example.TravelPlanner.Event.Event;
import com.example.TravelPlanner.Event.EventRepository;
import com.example.TravelPlanner.Event.EventService;
import com.example.TravelPlanner.Event.EventType;
import com.example.TravelPlanner.appuser.AppUser;
import com.example.TravelPlanner.appuser.AppUserRepository;
import com.example.TravelPlanner.appuser.AppUserRole;
import com.example.TravelPlanner.appuser.AppUserService;
import com.example.TravelPlanner.security.CustomAuthenticationProvider;
import com.example.TravelPlanner.travel.Travel;
import com.example.TravelPlanner.travel.TravelRepository;
import com.example.TravelPlanner.travel.TravelService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig /*extends WebSecurityConfigurerAdapter*/ {

    private final AppUserService appUserService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CustomAuthenticationProvider customAuthenticationProvider;

/*    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("api/registration/**")
                .permitAll()
                .anyRequest()
                .authenticated().and()
                .formLogin();
    }*/

    @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                .csrf().disable()
                .authorizeRequests()
                //.antMatchers("/api/registration/**","/registration/**","/styles/**")
                    .antMatchers("/api/registration/**","/api/login/**","/registration/**","/styles/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                    .loginPage("/login")//custom login page
                    .usernameParameter("email")  //put this if input id and name for username in login form is different from username
                    .passwordParameter("password") //put this if input id and name for password in login form is different from password
                    .defaultSuccessUrl("/home", true)
                .permitAll()
                .and()
                .logout();

        return http.build();
    }

/*    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }*/

/*    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(appUserService);
        return provider;
    }*/

/*    @Bean
    public AuthenticationManager authManager(HttpSecurity http)
            throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(appUserService)
                .passwordEncoder(bCryptPasswordEncoder)
                .and()
                .build();
    }*/

    @Bean
    public AuthenticationManager authManager(HttpSecurity http)
            throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(customAuthenticationProvider);
        return authenticationManagerBuilder.build();

    }

    //Create pre-defined entities - users, travels and events
    @Bean
    CommandLineRunner createTestUsers(AppUserRepository appUserRepository,
                                      TravelRepository travelRepository,
                                      EventRepository eventRepository,
                                      BCryptPasswordEncoder bCryptPasswordEncoder,
                                      EventService eventService,
                                      TravelService travelService)
    {
        return args -> {
            AppUser testUser = new AppUser("Krzysztof","Kowalski", "email@wp.pl",bCryptPasswordEncoder.encode("test"), AppUserRole.USER);
            testUser.setEnabled(true);
            appUserRepository.save(testUser);

            Travel testTravel = new Travel("Journey to London", "My first journey to UK with my friends",testUser);
            travelRepository.save(testTravel);

            Event testEvent = new Event("Visit on London Brigde",
                    EventType.SIGHTSEEING,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusHours(2),
                    130d,
                    testTravel);
            eventRepository.save(testEvent);
            testTravel.setStartDate(eventService);
            //travelService.updateStartDate(testTravel.getId());
            testTravel.setEndDate(eventService);
/*            List<Event> eventList = testTravel.getEvents();
            if(eventList==null)
            {
                eventList = new ArrayList<>();
            }
            eventList.add(testEvent);
            try {
                travelRepository.updateListEvents(eventList, testTravel.getId());
            }
            catch (Exception e)
            {
                System.out.println(e);
            }*/
            //travelRepository.save(testTravel);

            System.out.println("TEST: event  data start "+testEvent.getStartDate());
            System.out.println("TEST: travel title "+testEvent.getTravel().getTitle());
            System.out.println("TEST: travel data start "+testTravel.getStartDate());
            System.out.println("TEST: travel data end "+testTravel.getEndDate());
        };
    }
}

package com.example.TravelPlanner.registration;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/registration")
@AllArgsConstructor
public class RegistrationAPIController {

    private final RegistrationService registrationService;

    @PostMapping
    public String register(@RequestBody RegistrationRequest request) {return registrationService.register(request);}


    @GetMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token) {return registrationService.conirmToken(token);}
}

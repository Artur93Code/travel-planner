package com.example.TravelPlanner.login;

import com.example.TravelPlanner.security.CustomAuthenticationProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "api/login")
@AllArgsConstructor
public class LoginController {


    private final CustomAuthenticationProvider customAuthenticationProvider;

    @GetMapping
    public String login(@RequestBody Map<String, String> request){
        try{
            Authentication authentication = customAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(request.get("email"), request.get("password")));
            SecurityContextHolder.getContext().setAuthentication(authentication); //create session
            return  authentication.toString();
        }
        catch (Exception ex){
            return ex.getMessage();
        }
    }
}

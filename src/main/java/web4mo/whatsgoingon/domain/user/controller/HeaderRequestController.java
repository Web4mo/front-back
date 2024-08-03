package web4mo.whatsgoingon.domain.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web4mo.whatsgoingon.config.Authentication.CustomerUserDetailsService;
import web4mo.whatsgoingon.config.Authentication.JwtTokenProvider;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HeaderRequestController {
    CustomerUserDetailsService customerUserDetailsService;

    @GetMapping public ResponseEntity<Object> authHeaderChecker(HttpServletRequest request){
        Map<String,String> response = new HashMap<>(){
            {put("Authorization", request.getHeader("Authorization"));
            }};
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        System.out.println("현재 Authorization:"+ authentication.getAuthorities());
        //System.out.println("granted : "+ customerUserDetailsService.loadUserByUsername(authentication.getName()).getAuthorities());
        return ResponseEntity.ok(response);
    }
}
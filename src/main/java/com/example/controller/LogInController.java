package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Set;

@RestController
public class LogInController {

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @GetMapping("/user")
    public Principal user(Principal user){
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId("facebook");
        String clientId = clientRegistration.getClientId();
        String clientName = clientRegistration.getClientName();
        String clientSecret = clientRegistration.getClientSecret();
        String authorizationUri = clientRegistration.getProviderDetails().getAuthorizationUri();
        ClientRegistration.ProviderDetails.UserInfoEndpoint userInfoEndpoint = clientRegistration.getProviderDetails().getUserInfoEndpoint();
        String jwkSetUri = clientRegistration.getProviderDetails().getJwkSetUri();
        String tokenUri = clientRegistration.getProviderDetails().getTokenUri();
        String registrationId = clientRegistration.getRegistrationId();
        Set<String> scopes = clientRegistration.getScopes();

        return user;
    }
}

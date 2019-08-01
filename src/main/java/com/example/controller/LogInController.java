package com.example.controller;

import org.apache.http.HttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;


@RestController
public class LogInController {

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    private HttpGet get;
    private HttpClient httpClient;

    @GetMapping("/user")
    public void user(HttpServletResponse responsel) throws IOException {
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


    }
}

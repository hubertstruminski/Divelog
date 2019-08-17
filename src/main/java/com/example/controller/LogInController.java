package com.example.controller;

import org.apache.http.HttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;


@Controller
@CrossOrigin
public class LogInController {

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    private HttpGet get;
    private HttpClient httpClient;

    @GetMapping("/user")
    public String user(Principal user, HttpServletResponse response) throws IOException {
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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();

        Object credentials1 = authentication.getCredentials();
        Object principal2 = authentication.getPrincipal();


        OAuth2Authentication authentication1 = (OAuth2Authentication) authentication;

        Authentication userAuthentication = authentication1.getUserAuthentication();
        Object credentials2 = authentication1.getCredentials();
        OAuth2Request oAuth2Request = authentication1.getOAuth2Request();
        Object principal3 = authentication1.getPrincipal();


        Object principal1 = authentication.getPrincipal();
        System.out.println(principal1);
        String tokenValue = details.getTokenValue();
        System.out.println(tokenValue);

        System.out.println(user.getName());

        OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) user;
        boolean authenticated = oAuth2Authentication.getUserAuthentication().isAuthenticated();
        System.out.println(authenticated);

        return "index.html";
    }
}
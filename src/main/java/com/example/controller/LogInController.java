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


@RestController
public class LogInController {

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    private HttpGet get;
    private HttpClient httpClient;

    @GetMapping("/user")
    public Principal user(Principal user) throws IOException {
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

        Object principal = authentication.getPrincipal();
        Object credentials = authentication.getCredentials();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
        String tokenValue = details.getTokenValue();
        System.out.println(tokenValue);

        System.out.println(user.getName());
        Object decodedDetails = details.getDecodedDetails();
        System.out.println(decodedDetails.toString());

        OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) user;
        OAuth2Request oAuth2Request = oAuth2Authentication.getOAuth2Request();
        return user;
    }
}

package com.example.controller;

import com.example.config.JwtTokenProvider;
import com.example.config.SecurityConstants;
import io.jsonwebtoken.Claims;
import org.apache.http.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
public class TwitterWebhookController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @GetMapping("/twitter/webhooks/register/{jwtToken}")
    public ResponseEntity<?> registerTwitterWebhooks(@PathVariable String jwtToken) throws IOException {
        if(jwtTokenProvider.validateToken(jwtToken)) {
            Claims claimsFromJwt = jwtTokenProvider.getClaimsFromJwt(jwtToken);
            String accessToken = (String) claimsFromJwt.get("accessToken");

            String consumerKeySecret = SecurityConstants.TWITTER_CONSUMER_KEY + ":" + SecurityConstants.TWITTER_CONSUMER_SECRET;
            byte[] consumerKeySecretBytes = consumerKeySecret.getBytes(StandardCharsets.UTF_8);
            String encodedKeys = Base64.getEncoder().encodeToString(consumerKeySecretBytes);

            // GET BEARER TOKEN - POST request
            CloseableHttpClient httpClient2 = HttpClients.createDefault();
            HttpPost httpPost2 = new HttpPost("https://api.twitter.com/oauth2/token?grant_type=client_credentials");

            httpPost2.addHeader("Authorization", "Basic " + encodedKeys);
            httpPost2.addHeader("Content-Type", "application/x-www-form-urlencoded");

            HttpResponse response = httpClient2.execute(httpPost2);
            HttpEntity entity = response.getEntity();

            String json = EntityUtils.toString(response.getEntity());
            System.out.println(json);

            int indexAccessToken = json.indexOf("\"access_token\":\"");
            String bearerToken = json.substring(indexAccessToken + 16, json.length() - 2);
            System.out.println(bearerToken);


            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost("https://api.twitter.com/1.1/account_activity/all/divelogactivity/webhooks.json");

            List<NameValuePair> params = new ArrayList<NameValuePair>(1);
            params.add(new BasicNameValuePair("url", "http://localhost:8080/twitter/webhook/callback"));
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

//            httppost.addHeader("authorization", "OAuth oauth_consumer_key=\"" + SecurityConstants.TWITTER_CONSUMER_KEY +
//                    "\", oauth_nonce=\"GENERATED\", oauth_signature=\"GENERATED\", oauth_signature_method=\"HMAC-SHA1\"" +
//                    ", oauth_timestamp=\"GENERATED\", oauth_token=\"" + accessToken + "\", oauth_version=\"1.0\"");
            httppost.addHeader("authorization", "Bearer " + bearerToken);
            httppost.addHeader("Content-Type", "application/x-www-form-urlencoded");

            HttpResponse response2 = httpClient.execute(httppost);

            String webhook = EntityUtils.toString(response2.getEntity());
            System.out.println(webhook);


        }

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @GetMapping("/twitter/webhook/callback")
    public ResponseEntity<?> getTwitterWebhookCallback(HttpServletRequest request, HttpServletResponse response) {
        System.out.println(request);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}

package com.example.controller;

import com.example.config.JwtTokenProvider;
import com.example.config.SecurityConstants;
import com.example.dto.*;
import com.example.enums.Provider;
import com.example.model.Connection;
import com.example.model.CustomTwitter;
import com.example.repository.ConnectionRepository;
import com.example.repository.CustomTwitterRepository;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Null;
import java.awt.image.BufferedImage;
import java.io.*;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;

import org.apache.commons.io.FileUtils;

@RestController
public class TwitterController {

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private CustomTwitterRepository twitterRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @GetMapping("/signin")
    public String loginWithTwitter(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("todfC8BjhF9MbQ7VUeGY8EyWH")
                .setOAuthConsumerSecret("ftDjrAI9KMaZOtYWpg0sZWGx6lqIq4Jhan7uokwMdC2yKHbDj2")
                .setIncludeEmailEnabled(true);

        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        request.getSession().setAttribute("twitter", twitter);
        try {
            StringBuffer callbackURL = request.getRequestURL();
            int index = callbackURL.lastIndexOf("/");
            callbackURL.replace(index, callbackURL.length(), "").append("/callback");

            RequestToken requestToken = twitter.getOAuthRequestToken(callbackURL.toString());
            request.getSession().setAttribute("requestToken", requestToken);

            response.sendRedirect(requestToken.getAuthenticationURL());

            return "redirect:https://api.twitter.com/oauth/authorize";
        } catch (TwitterException e) {
            throw new ServletException(e);
        }
    }

    @GetMapping("/callback")
    public void loginWithTwitterCallback(HttpServletRequest request, HttpServletResponse response)
            throws TwitterException, ServletException, IOException {
        Twitter twitter = (Twitter) request.getSession().getAttribute("twitter");
        RequestToken requestToken = (RequestToken) request.getSession().getAttribute("requestToken");
        String verifier = request.getParameter("oauth_verifier");

        AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
        User user = twitter.verifyCredentials();

        if(accessToken == null || user == null) {
            return;
        }

        Connection foundedUser = connectionRepository.findByUserIDOrTwitterUserIdOrEmail(null,
                BigInteger.valueOf(accessToken.getUserId()), user.getEmail());

        String jwtToken = null;

        if(foundedUser == null) {
            Connection connection = new Connection();
            connection = setUserInfo(connection, accessToken, user);
            connection.setCreatedAt(new Date());
            connection.setUserID(null);

            CustomTwitter customTwitter = new CustomTwitter();

            connectionRepository.save(connection);
            setTwitter(connection, accessToken, customTwitter);

            jwtToken = jwtTokenProvider.generateTokenForTwitter(setConnectionDtoForTwitterToken(connection, customTwitter));
        } else {
            setUserInfo(foundedUser, accessToken, user);
            connectionRepository.save(foundedUser);

            CustomTwitter foundedTwitter = twitterRepository.findByUser(foundedUser);

            if(foundedTwitter == null) {
                CustomTwitter customTwitter = new CustomTwitter();
                setTwitter(foundedUser, accessToken, customTwitter);

                jwtToken = jwtTokenProvider.generateTokenForTwitter(setConnectionDtoForTwitterToken(foundedUser, customTwitter));
            } else {
                setTwitter(foundedUser, accessToken, foundedTwitter);

                jwtToken = jwtTokenProvider.generateTokenForTwitter(setConnectionDtoForTwitterToken(foundedUser, foundedTwitter));
            }

        }
        request.getSession().removeAttribute("requestToken");

        Cookie cookie = new Cookie("twitterJwtToken", jwtToken);
        cookie.setSecure(false);
        cookie.setMaxAge(60 * 60 * 24);
        cookie.setPath("/");

        response.addCookie(cookie);
        response.sendRedirect("http://localhost:3000/twitter");
    }

    @PostMapping("/twitter/users/search/{searchInput}/{jwtToken}")
    public ResponseEntity<?> findTwitterPeople(@PathVariable String searchInput, @PathVariable String jwtToken)
            throws TwitterException {
        Twitter twitter = setTwitterConfiguration(jwtToken);

        if(twitter == null) {
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }
        ResponseList<User> users = twitter.searchUsers(searchInput, 1);
        return new ResponseEntity<ResponseList<User>>(users, HttpStatus.OK);
    }

    @GetMapping("/twitter/available/closest/trends/{latitude}/{longitude}/{jwtToken}")
    public ResponseEntity<?> getAvailableTrends(@PathVariable double latitude, @PathVariable double longitude,
                                                @PathVariable String jwtToken) throws TwitterException {
        Twitter twitter = setTwitterConfiguration(jwtToken);

        if(twitter == null) {
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }

        GeoLocation geoLocation = new GeoLocation(latitude, longitude);
        ResponseList<Location> closestTrends = twitter.getClosestTrends(geoLocation);

        List<TrendDto> trendList = new ArrayList<>();

        for(Location location: closestTrends) {
            Trends placeTrends = twitter.getPlaceTrends(location.getWoeid());
            Trend[] trends = placeTrends.getTrends();

            for(Trend trend: trends) {
                if(trend.getTweetVolume() == -1) {
                    continue;
                }
                TrendDto trendDto = new TrendDto();

                trendDto.setCountryName(location.getCountryName());
                trendDto.setName(trend.getName());

                String tweetVolume = convertTweetVolume(trend.getTweetVolume());
                trendDto.setTweetVolume(tweetVolume);

                trendList.add(trendDto);
            }
        }
        return new ResponseEntity<List<TrendDto>>(trendList, HttpStatus.OK);
    }

    @GetMapping("/twitter/friends/list/{jwtToken}")
    public ResponseEntity<?> getFriendsList(@PathVariable String jwtToken) throws TwitterException {
        Twitter twitter = setTwitterConfiguration(jwtToken);

        if(twitter == null) {
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }

        Claims claimsFromJwt = jwtTokenProvider.getClaimsFromJwt(jwtToken);
        Object twitterUserID = claimsFromJwt.get("twitterUserID");

        User user = twitter.showUser(String.valueOf(claimsFromJwt.get("screenName")));
        long id1 = user.getId();

        IDs followersIDs = twitter.getFollowersIDs(id1, -1);
        List<User> friendsList = new ArrayList<>();

        for(long id: followersIDs.getIDs()) {
            friendsList.add(user);
        }

        return new ResponseEntity<List<User>>(friendsList, HttpStatus.OK);
    }

    @GetMapping("/twitter/home/timeline/{jwtToken}")
    public ResponseEntity<?> getHomeTimeline(@PathVariable String jwtToken) throws TwitterException {
        Twitter twitter = setTwitterConfiguration(jwtToken);

        if(twitter == null) {
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }

        ResponseList<Status> homeTimeline = twitter.getHomeTimeline();
        List<String> oEmbedTweets = new ArrayList<>();
        StringBuilder builder = new StringBuilder();

        for(Status tweet: homeTimeline) {
            builder = createOEmbedTweet(tweet, twitter, builder);
        }
        return new ResponseEntity<String>(builder.toString(), HttpStatus.OK);
    }

    @PostMapping("/twitter/create/tweet/{jwtToken}")
    public ResponseEntity<?> createTweet(@PathVariable String jwtToken, @RequestBody TweetDto tweetDto)
            throws TwitterException, IOException, URISyntaxException {
        Twitter twitter = setTwitterConfiguration(jwtToken);

        if(twitter == null) {
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }
        long[] mediaIds = null;

        if(tweetDto.getFiles().size() > 0) {
            mediaIds = new long[tweetDto.getFiles().size()];
            int count = 0;

            for(TweetFileDto file: tweetDto.getFiles()) {
                URL url = new URL(file.getUrl());

                BufferedImage image = ImageIO.read(url);
                ByteArrayOutputStream os = new ByteArrayOutputStream();

                if(file.getType().contains("png")) {
                    ImageIO.write(image, "png", os);
                } else {
                    ImageIO.write(image, "jpg", os);
                }
                InputStream is = new ByteArrayInputStream(os.toByteArray());

                UploadedMedia uploadedMedia = twitter.uploadMedia(file.getName(), is);

                mediaIds[count] = uploadedMedia.getMediaId();
                count++;
            }
        }

        StatusUpdate update = new StatusUpdate(tweetDto.getMessage());
        update.setMediaIds(mediaIds);

        Status status = twitter.updateStatus(update);

        String url= "https://twitter.com/" + status.getUser().getScreenName() + "/status/" + status.getId();
        OEmbedRequest oEmbedRequest = new OEmbedRequest(status.getId(), url);
        OEmbed oEmbed = twitter.getOEmbed(oEmbedRequest);

        return new ResponseEntity<String>(oEmbed.getHtml(), HttpStatus.OK);
    }

    @PostMapping("/twitter/search/tweets/{jwtToken}")
    public ResponseEntity<?> getSearchTweets(@RequestBody String query, @PathVariable String jwtToken) throws TwitterException {
        Twitter twitter = setTwitterConfiguration(jwtToken);

        if(twitter == null) {
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }

        Query tweetsQuery = new Query(query);

        QueryResult queryResult = twitter.search(tweetsQuery);
        List<Status> tweets = queryResult.getTweets();
        StringBuilder builder = new StringBuilder();

        for(Status tweet: tweets) {
            builder = createOEmbedTweet(tweet, twitter, builder);
        }

        return new ResponseEntity<String>(builder.toString(), HttpStatus.OK);
    }

    @GetMapping("/twitter/direct/messages/{jwtToken}")
    public ResponseEntity<?> getDirectMessages(@PathVariable String jwtToken) throws TwitterException {
        Twitter twitter = setTwitterConfiguration(jwtToken);

        if(twitter == null) {
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }

        DirectMessageList directMessages = twitter.getDirectMessages(50);

        while(directMessages.getNextCursor() != null) {
            DirectMessageList directMessagesFromNextCursor = twitter.getDirectMessages(50, directMessages.getNextCursor());

            directMessages.addAll(directMessagesFromNextCursor);
        }

        Set<TwitterInboxDto> messagesSet = new HashSet<>();
        List<Long> recipientIds = new ArrayList<>();
        List<Long> senderIds = new ArrayList<>();

        for(DirectMessage message: directMessages) {
            if(!checkIfConversationExist(senderIds, recipientIds, message)) {
                TwitterInboxDto twitterInboxDto = new TwitterInboxDto();

                twitterInboxDto.setRecipientId(String.valueOf(message.getRecipientId()));
                twitterInboxDto.setSenderId(String.valueOf(message.getSenderId()));
                twitterInboxDto.setCreatedAt(message.getCreatedAt());
                twitterInboxDto.setText(message.getText());

                User user = twitter.showUser(message.getRecipientId());
                twitterInboxDto.setName(user.getName());
                twitterInboxDto.setScreenName(user.getScreenName());
                twitterInboxDto.setPictureUrl(user.get400x400ProfileImageURL());

                messagesSet.add(twitterInboxDto);
                recipientIds.add(message.getRecipientId());
                senderIds.add(message.getSenderId());
            }
        }
        return new ResponseEntity<Set<TwitterInboxDto>>(messagesSet, HttpStatus.OK);
    }

    @PostMapping("/twitter/direct/messages/specified/person/{jwtToken}")
    public ResponseEntity<?> getDirectMessagesWithSpecifiedPerson(@PathVariable String jwtToken) {
        Twitter twitter = setTwitterConfiguration(jwtToken);

        if(twitter == null) {
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    private Connection setUserInfo(Connection connection, AccessToken accessToken, User user) {
        connection.setTwitterUserId(BigInteger.valueOf(accessToken.getUserId()));
        connection.setProviderId(Provider.TWITTER.getProvider());
        connection.setEmail(user.getEmail());
        connection.setPictureUrl(user.get400x400ProfileImageURL());
        connection.setLoggedAt(new Date());
        connection.setName(user.getName());
        connection.setAccessToken(accessToken.getToken());
        connection.setAuthenticated(true);

        return connection;
    }

    private void setTwitter(Connection connection, AccessToken accessToken, CustomTwitter customTwitter) {
        customTwitter.setUser(connection);
        customTwitter.setTokenSecret(accessToken.getTokenSecret());
        customTwitter.setScreenName(accessToken.getScreenName());

        twitterRepository.save(customTwitter);
    }

    private ConnectionDto setConnectionDtoForTwitterToken(Connection user, CustomTwitter twitter) {
        ConnectionDto connectionDto = new ConnectionDto();

        connectionDto.setTwitterUserId(user.getTwitterUserId());
        connectionDto.setProviderId(user.getProviderId());
        connectionDto.setPictureUrl(user.getPictureUrl());
        connectionDto.setName(user.getName());
        connectionDto.setLoggedAt(user.getLoggedAt());
        connectionDto.setEmail(user.getEmail());
        connectionDto.setCreatedAt(user.getCreatedAt());
        connectionDto.setAccessToken(user.getAccessToken());

        connectionDto.setTokenSecret(twitter.getTokenSecret());
        connectionDto.setScreenName(twitter.getScreenName());

        return connectionDto;
    }

    private Twitter setTwitterConfiguration(String jwtToken) {
        if(jwtTokenProvider.validateToken(jwtToken)) {
            Claims claimsFromJwt = jwtTokenProvider.getClaimsFromJwt(jwtToken);
            String accessToken = (String) claimsFromJwt.get("accessToken");
            String tokenSecret = (String) claimsFromJwt.get("tokenSecret");

            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true)
                    .setOAuthConsumerKey(SecurityConstants.TWITTER_CONSUMER_KEY)
                    .setOAuthConsumerSecret(SecurityConstants.TWITTER_CONSUMER_SECRET)
                    .setOAuthAccessToken(accessToken)
                    .setOAuthAccessTokenSecret(tokenSecret);
            TwitterFactory tf = new TwitterFactory(cb.build());
            Twitter twitter = tf.getInstance();
            return twitter;
        }
        return null;
    }

    private String convertTweetVolume(int tweetVolume) {
        String result = String.valueOf(tweetVolume);
        StringBuilder builder = new StringBuilder();

        for(int i=0; i<result.length(); i++) {
            if(i == result.length() - 3) {
                builder.append(",");
            }
            builder.append(result.charAt(i));
        }
        return builder.toString();
    }

    private StringBuilder createOEmbedTweet(Status tweet, Twitter twitter, StringBuilder builder) throws TwitterException {
        String url= "https://twitter.com/" + tweet.getUser().getScreenName() + "/status/" + tweet.getId();
        OEmbedRequest oEmbedRequest = new OEmbedRequest(tweet.getId(), url);

        OEmbed oEmbed = twitter.getOEmbed(oEmbedRequest);
        return builder.append(oEmbed.getHtml());
    }

    private boolean checkIfConversationExist(List<Long> senderIds, List<Long> recipientIds, DirectMessage message) {
        return (recipientIds.contains(message.getRecipientId()) && senderIds.contains(message.getSenderId())) ||
                (recipientIds.contains(message.getSenderId()) && senderIds.contains(message.getRecipientId()));
    }
}

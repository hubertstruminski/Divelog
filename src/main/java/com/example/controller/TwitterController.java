package com.example.controller;

import com.example.cache.service.TwitterCacheService;
import com.example.config.JwtTokenProvider;
import com.example.config.SecurityConstants;
import com.example.dto.*;
import com.example.enums.Provider;
import com.example.model.Connection;
import com.example.model.CustomTwitter;
import com.example.repository.ConnectionRepository;
import com.example.repository.CustomTwitterRepository;
import io.jsonwebtoken.Claims;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;

import java.math.BigInteger;
import java.net.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;
import static org.apache.commons.lang.StringEscapeUtils.escapeJavaScript;

@CrossOrigin
@RestController
public class TwitterController {

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private CustomTwitterRepository twitterRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private TwitterCacheService twitterCacheService;

    @GetMapping(value = "/signin", produces = "application/json")
    public void loginWithTwitter(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("todfC8BjhF9MbQ7VUeGY8EyWH")
                .setOAuthConsumerSecret("ftDjrAI9KMaZOtYWpg0sZWGx6lqIq4Jhan7uokwMdC2yKHbDj2")
                .setIncludeEmailEnabled(true);

        System.out.println("/signin JSP function");
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
        } catch (TwitterException e) {
            throw new ServletException(e);
        }
    }

    @GetMapping(value = "/callback", produces = "applicationP/json")
    public void loginWithTwitterCallback(HttpServletRequest request, HttpServletResponse response)
            throws TwitterException, ServletException, IOException {
        Twitter twitter = (Twitter) request.getSession().getAttribute("twitter");
        RequestToken requestToken = (RequestToken) request.getSession().getAttribute("requestToken");
        String verifier = request.getParameter("oauth_verifier");
        System.out.println("/callback JSP function");
        AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
        User user = twitter.verifyCredentials();

        if(accessToken == null || user == null) {
            return;
        }

        Connection foundedUser = connectionRepository.findByTwitterUserId(BigInteger.valueOf(accessToken.getUserId()));

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

        response.sendRedirect("http://localhost:3000/twitter/likes/" + jwtToken);
//        response.sendRedirect("https://divelog.eu/twitter/likes/" + jwtToken);
    }

    @GetMapping(value = "/twitter/login/validate/token/{jwtToken}")
    public ResponseEntity<?> validateJwtTokenForTwitterLogin(@PathVariable String jwtToken) {
        if(jwtTokenProvider.validateToken(jwtToken)) {
            Claims claimsFromJwt = jwtTokenProvider.getClaimsFromJwt(jwtToken);

            BigInteger twitterUserID = BigInteger.valueOf((Long) claimsFromJwt.get("twitterUserID"));
            String email = (String) claimsFromJwt.get("email");
            String accessToken = (String) claimsFromJwt.get("accessToken");
            Long createdAt = (Long) claimsFromJwt.get("createdAt");
            String tokenSecret = (String) claimsFromJwt.get("tokenSecret");

            Connection foundUser = connectionRepository.findByTwitterUserId(twitterUserID);
            CustomTwitter twitterUser = twitterRepository.findByUser(foundUser);

            if(foundUser == null || twitterUser == null) {
                return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
            }

            if(checkTwitterCredentialsForJwtToken(accessToken, createdAt, tokenSecret, foundUser, twitterUser)) {
                return new ResponseEntity<Void>(HttpStatus.OK);
            }
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/twitter/users/search/{searchInput}/{jwtToken}", produces = "application/json")
    public ResponseEntity<?> findTwitterPeople(@PathVariable String searchInput, @PathVariable String jwtToken)
            throws TwitterException {
        Twitter twitter = setTwitterConfiguration(jwtToken);

        if(twitter == null) {
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }

        ResponseList<User> users = null;

        try {
            users = twitterCacheService.searchUsers(twitter, searchInput, 1);
        } catch(TwitterException e) {
            return handleRateLimitExceedException(e.getStatusCode());
        }

        return new ResponseEntity<ResponseList<User>>(users, HttpStatus.OK);
    }

    @GetMapping(value = "/twitter/available/closest/trends/{latitude}/{longitude}/{jwtToken}", produces = "application/json")
    @CrossOrigin
    public ResponseEntity<?> getAvailableTrends(@PathVariable double latitude, @PathVariable double longitude,
                                                @PathVariable String jwtToken) throws TwitterException {
        Twitter twitter = setTwitterConfiguration(jwtToken);

        if(twitter == null) {
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }

        GeoLocation geoLocation = new GeoLocation(latitude, longitude);

        ResponseList<Location> closestTrends = null;
        List<TrendDto> trendList = new ArrayList<>();
        try {
            closestTrends = twitterCacheService.getClosestTrends(twitter, geoLocation);

            for(Location location: closestTrends) {
                Trends placeTrends = twitterCacheService.getPlaceTrends(twitter, location.getWoeid());

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
        } catch(TwitterException exception) {
            return handleRateLimitExceedException(exception.getStatusCode());
        }

        return new ResponseEntity<List<TrendDto>>(trendList, HttpStatus.OK);
    }

    @GetMapping(value = "/twitter/friends/list/{jwtToken}", produces = "application/json")
    public ResponseEntity<?> getFriendsList(@PathVariable String jwtToken) throws TwitterException {
        Twitter twitter = setTwitterConfiguration(jwtToken);

        if(twitter == null) {
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }

        Claims claimsFromJwt = jwtTokenProvider.getClaimsFromJwt(jwtToken);
        Object twitterUserID = claimsFromJwt.get("twitterUserID");

        User me = null;
        IDs followersIDs = null;

        try {
            me = twitterCacheService.showUserString(twitter, String.valueOf(claimsFromJwt.get("screenName")));
            followersIDs = twitterCacheService.getFollowersIDs(twitter, me.getId(), -1);
        } catch(TwitterException exception) {
            return handleRateLimitExceedException(exception.getStatusCode());
        }

        List<User> friendsList = new ArrayList<>();

        for(long id: followersIDs.getIDs()) {
            User user = null;
            try {
                user = twitterCacheService.showUserLong(twitter, id);
                friendsList.add(user);
            } catch(TwitterException exception) {
                return handleRateLimitExceedException(exception.getStatusCode());
            }
        }
        return new ResponseEntity<List<User>>(friendsList, HttpStatus.OK);
    }

    @GetMapping(value = "/twitter/home/timeline/{jwtToken}")
    public ResponseEntity<?> getHomeTimeline(@PathVariable String jwtToken) throws TwitterException {
        Twitter twitter = setTwitterConfiguration(jwtToken);

        if(twitter == null) {
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }

        ResponseList<Status> homeTimeline = null;
        try {
            homeTimeline = twitterCacheService.getHomeTimeline(twitter);
        } catch(TwitterException exception) {
            return handleRateLimitExceedException(exception.getStatusCode());
        }

        final StringBuilder builder = new StringBuilder();
        AtomicBoolean isExceptionInsideStream = new AtomicBoolean(false);

        homeTimeline.stream()
                .forEach(tweet -> {
                    try {
                        createOEmbedTweet(tweet, twitter, builder);
                    } catch (TwitterException e) {
                        if(e.getStatusCode() == 429) {
                            isExceptionInsideStream.set(true);
                            return;
                        }
                    }
                });

        if(isExceptionInsideStream.get()) {
            return handleRateLimitExceedException(429);
        }

        return new ResponseEntity<String>(builder.toString(), HttpStatus.OK);
    }

    @PostMapping(value = "/twitter/create/tweet/{jwtToken}", produces = "application/json")
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
        String escapedJavascriptMessage = escapeJavaScript(tweetDto.getMessage());
        String escapedHtmlMessage = escapeHtml(escapedJavascriptMessage);

        StatusUpdate update = new StatusUpdate(escapedHtmlMessage);
        update.setMediaIds(mediaIds);

        OEmbed oEmbed = null;
        try {
            Status status = twitter.updateStatus(update);

            String url= "https://twitter.com/" + status.getUser().getScreenName() + "/status/" + status.getId();
            OEmbedRequest oEmbedRequest = new OEmbedRequest(status.getId(), url);
            oEmbed = twitter.getOEmbed(oEmbedRequest);
        } catch(TwitterException exception) {
            return handleRateLimitExceedException(exception.getStatusCode());
        }

        return new ResponseEntity<String>(oEmbed.getHtml(), HttpStatus.OK);
    }

    @PostMapping(value = "/twitter/search/tweets/{jwtToken}", produces = "application/json")
    public ResponseEntity<?> getSearchTweets(@RequestBody String query, @PathVariable String jwtToken) throws TwitterException {
        Twitter twitter = setTwitterConfiguration(jwtToken);

        if(twitter == null) {
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }

        StringBuilder builder = new StringBuilder();
        try {
            QueryResult queryResult = twitterCacheService.search(twitter, query);

            List<Status> tweets = queryResult.getTweets();

            for(Status tweet: tweets) {
                builder = createOEmbedTweet(tweet, twitter, builder);
            }
        } catch(TwitterException exception) {
            return handleRateLimitExceedException(exception.getStatusCode());
        }

        return new ResponseEntity<String>(builder.toString(), HttpStatus.OK);
    }

    @GetMapping(value = "/twitter/direct/messages/{jwtToken}", produces = "application/json")
    public ResponseEntity<?> getDirectMessages(@PathVariable String jwtToken) throws TwitterException {
        Twitter twitter = setTwitterConfiguration(jwtToken);

        if(twitter == null) {
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }

        Claims claimsFromJwt = jwtTokenProvider.getClaimsFromJwt(jwtToken);
        String twitterUserID = String.valueOf(claimsFromJwt.get("twitterUserID"));

        DirectMessageList directMessages = getDirectMessagesByRestAPI(twitter);

        Set<TwitterInboxDto> messagesSet = new HashSet<>();
        List<Long> recipientIds = new ArrayList<>();
        List<Long> senderIds = new ArrayList<>();



        for(DirectMessage message: directMessages) {
            if(!checkIfConversationExist(senderIds, recipientIds, message)) {
                TwitterInboxDto twitterInboxDto = new TwitterInboxDto();

                twitterInboxDto.setCreatedAt(message.getCreatedAt());
                twitterInboxDto.setText(message.getText());

                String userId = null;
                if(!twitterUserID.equals(String.valueOf(message.getRecipientId()))) {
                    userId = String.valueOf(message.getRecipientId());
                }

                if(!twitterUserID.equals(String.valueOf(message.getSenderId()))) {
                    userId = String.valueOf(message.getSenderId());
                }

                twitterInboxDto.setUserId(userId);

                User user = twitter.showUser(Long.parseLong(userId));
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

    @PostMapping(value = "/twitter/direct/messages/specified/person/{jwtToken}", produces = "application/json")
    public ResponseEntity<?> getDirectMessagesWithSpecifiedPerson(@RequestBody String userId,
                                                                  @PathVariable String jwtToken) throws TwitterException {
        Twitter twitter = setTwitterConfiguration(jwtToken);

        if(twitter == null) {
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }

        Claims claimsFromJwt = jwtTokenProvider.getClaimsFromJwt(jwtToken);
        long twitterUserID = (long) claimsFromJwt.get("twitterUserID");

        DirectMessageList directMessages = getDirectMessagesByRestAPI(twitter);
        List<SingleDirectMessage> privateMessages = new ArrayList<>();

        userId = userId.replace("\"", "");

        for(DirectMessage message: directMessages) {
            if(checkIfMessagesFromSpecifiedPerson(userId, message)) {
                SingleDirectMessage singleMessage = new SingleDirectMessage();

                singleMessage.setId(message.getId());
                singleMessage.setText(message.getText());
                singleMessage.setSenderId(message.getSenderId());
                singleMessage.setRecipientId(message.getRecipientId());
                singleMessage.setCreatedAt(message.getCreatedAt());
                singleMessage.setTwitterOwnerId(twitterUserID);

                EntitySupport entitySupport = message;
                singleMessage.setMediaEntities(entitySupport.getMediaEntities());
                singleMessage.setUrlEntities(entitySupport.getURLEntities());

                privateMessages.add(singleMessage);
            }
        }
        return new ResponseEntity<>(privateMessages, HttpStatus.OK);
    }

    @PostMapping(value = "/twitter/direct/messages/search/people/{jwtToken}", produces = "application/json")
    public ResponseEntity<?> searchPeopleToStartConversations(@RequestBody String searchInput,
                                                              @PathVariable String jwtToken) throws TwitterException {
        Twitter twitter = setTwitterConfiguration(jwtToken);

        if(twitter == null) {
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }

        Claims claimsFromJwt = jwtTokenProvider.getClaimsFromJwt(jwtToken);
        long twitterUserID = (long) claimsFromJwt.get("twitterUserID");

        ResponseList<User> users = twitter.searchUsers(searchInput, 1);
        List<ContactDirectMessage> contacts = new ArrayList<>();

        int count = 0;
        for(User user: users) {
            if(count == 5) {
                break;
            }
            Relationship relationship = twitter.showFriendship(twitterUserID, user.getId());

            ContactDirectMessage contact = new ContactDirectMessage();

            contact.setName(user.getName());
            contact.setScreenName(user.getScreenName());
            contact.setPictureUrl(user.get400x400ProfileImageURL());

            if(relationship.canSourceDm()) {
                contact.setDMAccessible(true);
            } else {
                contact.setDMAccessible(false);
            }
            contacts.add(contact);
            count++;
        }
        return new ResponseEntity<List<ContactDirectMessage>>(contacts, HttpStatus.OK);
    }

    @PostMapping(value = "/twitter/direct/message/person/photo/retrieve/{jwtToken}")
    public ResponseEntity<?> getPhotoFromSingleDirectMessage(@RequestBody String photoUrl, @PathVariable String jwtToken) throws IOException, InvalidKeyException, NoSuchAlgorithmException, SignatureException, ParseException, TwitterException {
        Twitter twitter = setTwitterConfiguration(jwtToken);

        InputStream dmImageAsStream = twitter.getDMImageAsStream(photoUrl);

        byte[] bytes = IOUtils.toByteArray(dmImageAsStream);
        byte[] encode = Base64.getEncoder().encode(bytes);

        return new ResponseEntity<>(encode, HttpStatus.OK);
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

    private ResponseEntity<?> handleRateLimitExceedException(int statusCode) {
        if(statusCode == 429) {
            return new ResponseEntity<>(HttpStatus.valueOf(statusCode));
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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

        OEmbed oEmbed = twitterCacheService.getOEmbed(twitter, oEmbedRequest);

        System.out.println("getOEmbed -> limit: " + oEmbed.getRateLimitStatus().getLimit());
        System.out.println("getOEmbed -> remaining: " + oEmbed.getRateLimitStatus().getRemaining());

        return builder.append(oEmbed.getHtml());
    }

    private boolean checkIfConversationExist(List<Long> senderIds, List<Long> recipientIds, DirectMessage message) {
        return (recipientIds.contains(message.getRecipientId()) && senderIds.contains(message.getSenderId())) ||
                (recipientIds.contains(message.getSenderId()) && senderIds.contains(message.getRecipientId()));
    }

    private boolean checkIfMessagesFromSpecifiedPerson(String userId, DirectMessage message) {
        return userId.equals(String.valueOf(message.getRecipientId())) ||
                userId.equals(String.valueOf(message.getSenderId()));
    }

    private DirectMessageList getDirectMessagesByRestAPI(Twitter twitter) throws TwitterException {
        DirectMessageList directMessages = twitter.getDirectMessages(50);

        while(directMessages.getNextCursor() != null) {
            DirectMessageList directMessagesFromNextCursor = twitter.getDirectMessages(50, directMessages.getNextCursor());

            directMessages.addAll(directMessagesFromNextCursor);
        }
        return directMessages;
    }

    private boolean checkTwitterCredentialsForJwtToken(String accessToken, long createdAt, String tokenSecret,
                                                    Connection foundUser, CustomTwitter twitterUser) {
        return accessToken.equals(foundUser.getAccessToken()) &&
                createdAt == foundUser.getCreatedAt().getTime() &&
                tokenSecret.equals(twitterUser.getTokenSecret());
    }
}

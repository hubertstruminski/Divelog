package com.example.controller;

import com.example.config.JwtTokenProvider;
import com.example.config.SecurityConstants;
import com.example.config.Signature;
import com.example.dto.*;
import com.example.enums.Provider;
import com.example.model.Connection;
import com.example.model.CustomTwitter;
import com.example.repository.ConnectionRepository;
import com.example.repository.CustomTwitterRepository;
import com.sun.jndi.toolkit.url.Uri;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CircularRedirectException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Null;
import java.awt.image.BufferedImage;
import java.io.*;

import java.math.BigInteger;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.io.FileUtils;

import static javax.xml.crypto.dsig.SignatureMethod.HMAC_SHA1;

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
    private Signature signature;

    @PostMapping("/signin/twitter")
    public String loginWithTwitter(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("todfC8BjhF9MbQ7VUeGY8EyWH")
                .setOAuthConsumerSecret("ftDjrAI9KMaZOtYWpg0sZWGx6lqIq4Jhan7uokwMdC2yKHbDj2")
                .setIncludeEmailEnabled(true);

        System.out.println("/signin function");
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

    @PostMapping("/callback")
    public void loginWithTwitterCallback(HttpServletRequest request, HttpServletResponse response)
            throws TwitterException, ServletException, IOException {
        Twitter twitter = (Twitter) request.getSession().getAttribute("twitter");
        RequestToken requestToken = (RequestToken) request.getSession().getAttribute("requestToken");
        String verifier = request.getParameter("oauth_verifier");
        System.out.println("/callback function");
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
//        response.sendRedirect("http://divelog.eu/twitter");
//        response.sendRedirect("http://localhost:3000/twitter");
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

        DirectMessageList directMessages = getDirectMessagesByRestAPI(twitter);

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
    public ResponseEntity<?> getDirectMessagesWithSpecifiedPerson(@RequestBody RecipientSender recipientSender,
                                                                  @PathVariable String jwtToken) throws TwitterException {
        Twitter twitter = setTwitterConfiguration(jwtToken);

        if(twitter == null) {
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }

        Claims claimsFromJwt = jwtTokenProvider.getClaimsFromJwt(jwtToken);
        long twitterUserID = (long) claimsFromJwt.get("twitterUserID");

        DirectMessageList directMessages = getDirectMessagesByRestAPI(twitter);
        List<SingleDirectMessage> privateMessages = new ArrayList<>();

        for(DirectMessage message: directMessages) {
            if(checkIfMessagesFromSpecifiedPerson(recipientSender, message)) {
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

    @PostMapping("/twitter/direct/messages/search/people/{jwtToken}")
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


    public String getTwitterServerTime() throws IOException, ParseException {
        HttpsURLConnection con = (HttpsURLConnection)
                new URL("https://api.twitter.com/oauth/request_token").openConnection();
        con.setRequestMethod("HEAD");
        con.getResponseCode();
        String twitterDate= con.getHeaderField("Date");
        DateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
        Date date = formatter.parse(twitterDate);
        return String.valueOf(date.getTime() / 1000L);
    }

    @PostMapping("/twitter/direct/message/person/photo/retrieve/{jwtToken}")
    public ResponseEntity<?> getPhotoFromSingleDirectMessage(@RequestBody String photoUrl, @PathVariable String jwtToken) throws IOException, InvalidKeyException, NoSuchAlgorithmException, SignatureException, ParseException {
        Claims claimsFromJwt = jwtTokenProvider.getClaimsFromJwt(jwtToken);

        String accessToken = (String) claimsFromJwt.get("accessToken");
        String tokenSecret = (String) claimsFromJwt.get("tokenSecret");

        String randomData = RandomStringUtils.randomAlphanumeric(32).toUpperCase();
        byte[] bytes = randomData.getBytes("UTF-8");
        String base64OAuthNonce = Base64.getEncoder().encodeToString(bytes);

        String twitterServerTime = getTwitterServerTime();

        String OAUTH_CONSUMER_KEY = SecurityConstants.TWITTER_CONSUMER_KEY;
        String OAUTH_SIGNATURE_METHOD = SecurityConstants.OAUTH_SIGNATURE_METHOD;
        String OAUTH_TIMESTAMP = twitterServerTime;

        String mySignatureString = myFunctionGetSignature(accessToken, photoUrl, tokenSecret, OAUTH_TIMESTAMP);

        String authorizationHeader = URLEncoder.encode("oauth_consumer_key", "UTF-8") + "=" + quoted(URLEncoder.encode(OAUTH_CONSUMER_KEY, "UTF-8"), false)
                + URLEncoder.encode("oauth_nonce", "UTF-8") + "=" + quoted(URLEncoder.encode(base64OAuthNonce, "UTF-8"), false)
                + URLEncoder.encode("oauth_signature", "UTF-8") + "=" + quoted(URLEncoder.encode(mySignatureString, "UTF-8"), false)
                + URLEncoder.encode("oauth_signature_method", "UTF-8") + "=" + quoted(URLEncoder.encode(OAUTH_SIGNATURE_METHOD, "UTF-8"), false)
                + URLEncoder.encode("oauth_timestamp", "UTF-8") + "=" + quoted(URLEncoder.encode(OAUTH_TIMESTAMP, "UTF-8"), false)
                + URLEncoder.encode("oauth_token", "UTF-8") + "=" + quoted(URLEncoder.encode(accessToken, "UTF-8"), false)
                + URLEncoder.encode("oauth_version", "UTF-8") + "=" + quoted(URLEncoder.encode("1.0", "UTF-8"), true);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(photoUrl);

        httpGet.addHeader("Authorization", "OAuth " + authorizationHeader);
        httpGet.addHeader("Content-Type", "application/x-www-form-urlencoded");
        httpGet.addHeader("Accept-Encoding", "multipart/form-data");

        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
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

    private boolean checkIfMessagesFromSpecifiedPerson(RecipientSender recipientSender, DirectMessage message) {
        return (recipientSender.getRecipientId().equals(String.valueOf(message.getRecipientId())) &&
                recipientSender.getSenderId().equals(String.valueOf(message.getSenderId()))) ||
                (recipientSender.getSenderId().equals(String.valueOf(message.getRecipientId())) &&
                        recipientSender.getRecipientId().equals(String.valueOf(message.getSenderId())));
    }

    private DirectMessageList getDirectMessagesByRestAPI(Twitter twitter) throws TwitterException {
        DirectMessageList directMessages = twitter.getDirectMessages(50);

        while(directMessages.getNextCursor() != null) {
            DirectMessageList directMessagesFromNextCursor = twitter.getDirectMessages(50, directMessages.getNextCursor());

            directMessages.addAll(directMessagesFromNextCursor);
        }
        return directMessages;
    }

    private String createHeaderForGetPhotoRequest(String methodRequest, String apiUrl, String parameterString,
                                                  String tokenSecret) throws UnsupportedEncodingException {
        String signatureBaseString = methodRequest + "&" + URLEncoder.encode(apiUrl, "UTF-8") + "&" +
                URLEncoder.encode(parameterString, "UTF-8");

        String signingKey = URLEncoder.encode(SecurityConstants.TWITTER_CONSUMER_SECRET, "UTF-8") + "&" +
                URLEncoder.encode(tokenSecret, "UTF-8");


        return signatureBaseString;
    }

    private static String getSignature(String url, String params)
            throws UnsupportedEncodingException, NoSuchAlgorithmException,
            InvalidKeyException {
        /**
         * base has three parts, they are connected by "&": 1) protocol 2) URL
         * (need to be URLEncoded) 3) Parameter List (need to be URLEncoded).
         */
        StringBuilder base = new StringBuilder();
        base.append("GET&");
        base.append(url);
        base.append("&");
        base.append(params);
        System.out.println("Stirng for oauth_signature generation:" + base);
        // yea, don't ask me why, it is needed to append a "&" to the end of
        // secret key.
        byte[] keyBytes = (SecurityConstants.TWITTER_CONSUMER_SECRET + "&").getBytes("UTF-8");

        SecretKey key = new SecretKeySpec(keyBytes, "HmacSHA1");

        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(key);

        // encode it, base64 it, change it to string and return.
        return new String(Base64.getEncoder().encode(mac.doFinal(base.toString().getBytes(
                "UTF-8"))), "UTF-8").trim();
    }

    private String quoted(String string, boolean last) {
        if(last) {
            return "\"" + string + "\"";
        }
        return "\"" + string + "\"" + ", ";
    }

    private String myFunctionGetSignature(String accessToken, String photoUrl, String tokenSecret, String timestamp) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        String randomData = RandomStringUtils.randomAlphanumeric(32).toUpperCase();
        byte[] bytes = randomData.getBytes("UTF-8");
        System.out.println(bytes.length);
        String base64OAuthNonce = Base64.getEncoder().encodeToString(bytes);

        UriComponents encode = UriComponentsBuilder.newInstance()
                .queryParam("include_entities", "true")
                .queryParam("oauth_consumer_key", SecurityConstants.TWITTER_CONSUMER_KEY)
                .queryParam("oauth_nonce", base64OAuthNonce)
                .queryParam("oauth_signature_method", SecurityConstants.OAUTH_SIGNATURE_METHOD)
                .queryParam("oauth_timestamp", timestamp)
                .queryParam("oauth_token", accessToken)
                .queryParam("oauth_version", "1.0")
                .build().encode();

        // PARAMETER STRING
        String parameterString = encode.toUriString().substring(1);

        // SIGNATURE BASE STRING
        String signatureBaseString = "GET&" + URLEncoder.encode(photoUrl, "UTF-8") + "&" + URLEncoder.encode(parameterString, "UTF-8");

        // SIGNING KEY
        String signingKey = URLEncoder.encode(SecurityConstants.TWITTER_CONSUMER_SECRET, "UTF-8") + "&"
                + URLEncoder.encode(tokenSecret, "UTF-8");

        return signature.calculateRFC2104HMAC(signatureBaseString, signingKey);
    }
}

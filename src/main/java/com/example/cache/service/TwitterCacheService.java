package com.example.cache.service;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import twitter4j.*;

@Service
@CacheConfig(cacheNames = {"twitter/home/timeline",
        "twitter/users/search", "twitter/friends/list", "...showUser/string", "...showUser/long",
        "trends/closest", "trends/place", "search/tweets", "tweet/oembed"})
public class TwitterCacheService {

    @Cacheable("twitter/home/timeline")
    public ResponseList<Status> getHomeTimeline(Twitter twitter) throws TwitterException {
        return twitter.getHomeTimeline();
    }

    @Cacheable(value = "twitter/users/search", condition = "#searchInput.length() >= 4")
    public ResponseList<User> searchUsers(Twitter twitter, String searchInput, int paging) throws TwitterException {
        return twitter.searchUsers(searchInput, paging);
    }

    @Cacheable("twitter/friends/list")
    public IDs getFollowersIDs(Twitter twitter, long id, int cursor) throws TwitterException {
        return twitter.getFollowersIDs(id, cursor);
    }

    @Cacheable("...showUser/string")
    public User showUserString(Twitter twitter, String screenName) throws TwitterException {
        return twitter.showUser(screenName);
    }

    @Cacheable("...showUser/long")
    public User showUserLong(Twitter twitter, long id) throws TwitterException {
        return twitter.showUser(id);
    }

    @Cacheable("trends/closest")
    public ResponseList<Location> getClosestTrends(Twitter twitter, GeoLocation geoLocation) throws TwitterException {
        return twitter.getClosestTrends(geoLocation);
    }

    @Cacheable("trends/place")
    public Trends getPlaceTrends(Twitter twitter, int woeid) throws TwitterException {
        return twitter.getPlaceTrends(woeid);
    }

    @CachePut("twitter/home/timeline")
    public Status updateStatus(Twitter twitter, StatusUpdate statusUpdate) throws TwitterException {
        return twitter.updateStatus(statusUpdate);
    }

    @Cacheable(value = "search/tweets", condition = "#query.length() >= 2")
    public QueryResult search(Twitter twitter, String query) throws TwitterException {
        Query tweetsQuery = new Query(query);
        return twitter.search(tweetsQuery);
    }

    @Cacheable("tweet/oembed")
    public OEmbed getOEmbed(Twitter twitter, OEmbedRequest oEmbedRequest) throws TwitterException {
        return twitter.getOEmbed(oEmbedRequest);
    }
}

package com.example.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

@Configuration
public class TwitterCacheConfig {

    @Bean
    @Primary
    public CacheManager compositeCacheManager() {
        return new CompositeCacheManager(twitterHomeTimelineCacheManager(),
                twitterSearchUsersCacheManager(),
                followersIDsCacheManager(),
                twitterShowUserStringCacheManager(),
                twitterShowUserLongCacheManager(),
                twitterClosestTrendsCacheManager(),
                twitterPlaceTrendsCacheManager(),
                twitterSearchTweetsCacheManager(),
                twitterCreateOEmbedTweetCacheManager(),
                twitterDirectMessagesCacheManager(),
                twitterShowFriendshipCacheManager());
    }

    @Bean
    public CacheManager twitterHomeTimelineCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("twitter/home/timeline");
        cacheManager.setCaffeine(getTwitterHomeTimeline());
        return cacheManager;
    }
    Caffeine< Object, Object > getTwitterHomeTimeline() {
        return Caffeine.newBuilder()
                .initialCapacity(20)
                .maximumSize(150)
                .expireAfterAccess(1, TimeUnit.MINUTES); // 15requests / 15min
    }


    @Bean
    public CacheManager twitterSearchUsersCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("twitter/users/search");
        cacheManager.setCaffeine(searchUsers());
        return cacheManager;
    }
    Caffeine< Object, Object > searchUsers() {
        return Caffeine.newBuilder()
                .initialCapacity(150)
                .maximumSize(750)
                .expireAfterAccess(1, TimeUnit.SECONDS); // 900requests / 15min
    }

    @Bean
    public CacheManager followersIDsCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("twitter/friends/list");
        cacheManager.setCaffeine(getFollowersIDs());
        return cacheManager;
    }
    Caffeine< Object, Object > getFollowersIDs() {
        return Caffeine.newBuilder()
                .initialCapacity(200)
                .maximumSize(800)
                .expireAfterAccess(1, TimeUnit.MINUTES); // 15req / 15min
    }


    @Bean
    public CacheManager twitterShowUserStringCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("...showUser/string");
        cacheManager.setCaffeine(showUserString());
        return cacheManager;
    }
    Caffeine< Object, Object > showUserString() {
        return Caffeine.newBuilder()
                .initialCapacity(20)
                .maximumSize(125)
                .expireAfterAccess(1, TimeUnit.SECONDS); // 900requests / 15min
    }


    @Bean
    public CacheManager twitterShowUserLongCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("...showUser/long");
        cacheManager.setCaffeine(showUserLong());
        return cacheManager;
    }
    Caffeine< Object, Object > showUserLong() {
        return Caffeine.newBuilder()
                .initialCapacity(20)
                .maximumSize(125)
                .expireAfterAccess(1, TimeUnit.SECONDS); // 900requests / 15min
    }

    @Bean
    public CacheManager twitterClosestTrendsCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("trends/closest");
        cacheManager.setCaffeine(getClosestTrends());
        return cacheManager;
    }
    Caffeine< Object, Object > getClosestTrends() {
        return Caffeine.newBuilder()
                .initialCapacity(35)
                .maximumSize(100)
                .expireAfterAccess(12, TimeUnit.SECONDS); // 75requests / 15min
    }

    @Bean
    public CacheManager twitterPlaceTrendsCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("trends/place");
        cacheManager.setCaffeine(getPlaceTrends());
        return cacheManager;
    }
    Caffeine< Object, Object > getPlaceTrends() {
        return Caffeine.newBuilder()
                .initialCapacity(35)
                .maximumSize(100)
                .expireAfterAccess(12, TimeUnit.SECONDS); // 75requests / 15min
    }

    @Bean
    public CacheManager twitterSearchTweetsCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("search/tweets");
        cacheManager.setCaffeine(searchTweets());
        return cacheManager;
    }
    Caffeine< Object, Object > searchTweets() {
        return Caffeine.newBuilder()
                .initialCapacity(60)
                .maximumSize(450)
                .expireAfterAccess(5, TimeUnit.SECONDS); // 180requests / 15min
    }

    @Bean
    public CacheManager twitterCreateOEmbedTweetCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("tweet/oembed");
        cacheManager.setCaffeine(createOEmbedTweet());
        return cacheManager;
    }
    Caffeine< Object, Object > createOEmbedTweet() {
        return Caffeine.newBuilder()
                .initialCapacity(150)
                .maximumSize(750)
                .expireAfterAccess(5, TimeUnit.SECONDS); // 180requests / 15min
    }

    @Bean
    public CacheManager twitterDirectMessagesCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("twitter/direct/messages");
        cacheManager.setCaffeine(retrieveDirectMessages());
        return cacheManager;
    }
    Caffeine< Object, Object > retrieveDirectMessages() {
        return Caffeine.newBuilder()
                .initialCapacity(200)
                .maximumSize(2000)
                .expireAfterAccess(1, TimeUnit.MINUTES); // 15requests / 15min
    }

    @Bean
    public CacheManager twitterShowFriendshipCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("friendships/show");
        cacheManager.setCaffeine(showFriendship());
        return cacheManager;
    }
    Caffeine< Object, Object > showFriendship() {
        return Caffeine.newBuilder()
                .initialCapacity(50)
                .maximumSize(500)
                .expireAfterAccess(5, TimeUnit.SECONDS); // 180requests / 15min
    }
}

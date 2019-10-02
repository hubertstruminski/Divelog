package com.example.config;

import org.apache.catalina.filters.CorsFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;

@Configuration
@EnableWebSecurity
public class SocialConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/", "/login**", "/webjars/**", "/error**",
                        "/signin", "/getuserdata/**", "/add/marker/**", "/get/markers/**", "/delete/marker/**/**",
                        "/logout/**", "/add/logbook/**/**", "/get/logbook/**", "/logbook/**/**", "/**/**", "/edit/logbook/**/**",
                        "/pdf/logbook/**/**", "/add/topic", "/get/topic/posts/**", "/add/post", "/delete/post/**/**", "/post/**/**",
                        "/delete/post/file/**/**", "/get/topic/number/comments/**/**", "/update/topic/number/displays/**",
                        "/topic/likes/vote/**/**", "/update/topic/**")
                .permitAll()
                .anyRequest()
                .authenticated()

                .and()
                .addFilterBefore(new CorsFilter(), ChannelProcessingFilter.class);
    }
}

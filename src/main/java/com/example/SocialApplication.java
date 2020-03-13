package com.example;

import com.example.util.FilestackDataPicker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.IOException;

@SpringBootApplication
@ComponentScan(basePackages = { "com.example" })
@EntityScan(basePackages = {"com.example.model" })
@EnableJpaRepositories(basePackages = {"com.example.repository"})
public class SocialApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(SocialApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SocialApplication.class);
	}

	@Bean
	public static FilestackDataPicker createFileStackBean() throws IOException {
		return FilestackDataPicker.build("src/main/resources/filestack.txt");
	}
}

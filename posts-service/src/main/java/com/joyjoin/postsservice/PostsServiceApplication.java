package com.joyjoin.postsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PostsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PostsServiceApplication.class, args);
		String a = null;
		System.out.println(a.length());
	}

}

package com.ss.lms;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class LmsApplication 
{
	public static void main(String[] args) 
	{
		System.out.println("ADMIN");
		SpringApplication.run(LmsApplication.class, args);
	}
}
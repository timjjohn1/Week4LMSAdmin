package com.ss.lms;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LmsApplication 
{
	public static void main(String[] args) 
	{
		System.out.println("Hello my World!");
		SpringApplication.run(LmsApplication.class, args);
	}
}
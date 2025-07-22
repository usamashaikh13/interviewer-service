package com.xplore.interviewer;

import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class InterviewerServiceApplication {


	public static void main(String[] args) {
		SpringApplication.run(InterviewerServiceApplication.class, args);
		System.out.println("🎯 Interviewer Service started on port 8081");

	}


}

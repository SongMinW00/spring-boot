package com.example.demo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import jakarta.annotation.PostConstruct;
import java.util.Date;
import java.util.TimeZone;

@SpringBootApplication
//@SuppressWarnings("deprecation")
@EnableJpaAuditing
public class StartSpringApplication {

    @PostConstruct
    public void started(){
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        System.out.println("현재시각: " + new Date());
    }
    public static void main(String[] args) {

        SpringApplication.run(StartSpringApplication.class, args);
    }
//    @SpringBootApplication is a convenience annotation that adds all the following:
//
//@Configuration: Tags the class as a source of bean definitions for the application context.
//
//@EnableAutoConfiguration: Tells Spring Boot to start adding beans based on classpath settings, other beans, and various property settings. For example, if spring-webmvc is on the classpath, this annotation flags the application as a web application and activates key behaviors, such as setting up a DispatcherServlet.
//
//@ComponentScan: Tells Spring to look for other components, configurations, and services in the com/example package, letting it find the controllers.
}

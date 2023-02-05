package com.example.demo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
@SuppressWarnings("deprecation")
public class StartSpringApplication {

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

package com.icampagne.seabattle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
@SpringBootApplication
public class IcampagneSeabattleApplication {

    @RequestMapping("/shoot/{userId}")
    public String shoot(@PathVariable("userId") String userId, @RequestParam("seaX") int seaX) {
    	System.out.println("User " + userId + "  shoots to " + seaX);
        return "User " + userId + "  shoots to " + seaX;
    }

    @RequestMapping("/version")
    String version() {
        return "iCAMPAGNE Seabattle version 0.0.1  8 March 2017";
    }

	public static void main(String[] args) {
		SpringApplication.run(IcampagneSeabattleApplication.class, args);
	}
}

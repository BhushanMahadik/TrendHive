package com.TrendHive.TrendHive;

import com.TrendHive.TrendHive.services.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TrendHiveApplication {

	@Autowired
	UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(TrendHiveApplication.class, args);
	}

	@PostConstruct
	public void init(){
		try{
			System.out.println("Creating super user....");
			userService.createSuperUser("Bhushan","Bhushan@1234","mahadikbhushan768@gmail.com","Dombivili	");
			System.out.println("Super user created.");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error creating super user: "+e.getMessage());
		}
	}

}

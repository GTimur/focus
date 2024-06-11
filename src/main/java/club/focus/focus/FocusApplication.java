package club.focus.focus;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import club.focus.focus.dto.MessageDto;
import club.focus.focus.utils.MessagesReader;

@SpringBootApplication
public class FocusApplication {

	public static void main(String[] args) {	
		SpringApplication.run(FocusApplication.class, args);		
		
	}

}

package po.lab.example.predix.timeseries;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {

	@RequestMapping("/ping")
	public String ping(){
		return "pong - " + System.currentTimeMillis();
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}

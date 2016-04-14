package po.lab.example.predix.timeseries;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Application {

	@Value("${demo.timeseries.zoneId}")
	String zoneId;

	@Value("${demo.timeseries.accessToken}")
	String accessToken;

	@Bean
	public RestTemplate restTemplate(){
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getInterceptors().add(headersAddingInterceptor());
		return restTemplate;
	}

	public ClientHttpRequestInterceptor headersAddingInterceptor() {
		return (request, body, execution) -> {
			request.getHeaders().set("Predix-Zone-Id", zoneId);
			request.getHeaders().set("Authorization", "Bearer " + accessToken);
			return execution.execute(request, body);
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}

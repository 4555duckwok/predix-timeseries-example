package po.lab.example.predix.timeseries;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

@SpringBootApplication
@EnableOAuth2Client
@EnableConfigurationProperties
public class Application {

	@Value("${demo.timeseries.zoneId}")
	String zoneId;

	@Bean
	@ConfigurationProperties("security.oauth2.client")
	public ClientCredentialsResourceDetails details() {
		return new ClientCredentialsResourceDetails();
	}

	@Bean
	public OAuth2RestTemplate restTemplate(OAuth2ClientContext context, ClientCredentialsResourceDetails details) {
		OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(details, context);
		restTemplate.getInterceptors().add(headersAddingInterceptor());
		return restTemplate;
	}

	public ClientHttpRequestInterceptor headersAddingInterceptor() {
		return (request, body, execution) -> {
			request.getHeaders().set("Predix-Zone-Id", zoneId);
			return execution.execute(request, body);
		};
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("u").password("p").roles("USER", "ADMIN");
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}

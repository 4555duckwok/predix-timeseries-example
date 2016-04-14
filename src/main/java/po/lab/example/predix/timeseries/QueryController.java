package po.lab.example.predix.timeseries;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import static java.util.Collections.emptyMap;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class QueryController {

    @Value("${demo.timeseries.queryUrlPrefix}")
    private String queryUrlPrefix;

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping("/ping")
    public String ping(){
        return "pong - " + System.currentTimeMillis();
    }

    @ExceptionHandler
    @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
    public Object exceptionHandler(HttpStatusCodeException e) {
        return e.getResponseBodyAsString();
    }

    @RequestMapping("/tags")
    public String queryTags() throws Exception {
        return restTemplate.getForEntity(queryUrlPrefix + "/tags", String.class, emptyMap()).getBody();
    }

    @RequestMapping("/latest")
    public String queryLatestValues(@RequestParam(defaultValue = "{\"tags\":[{\"name\":\"Compressor-2015:CompressionRatio\"}]}") String requestBody) throws Exception {
        return restTemplate.postForEntity(queryUrlPrefix + "/datapoints/latest", requestBody, String.class, emptyMap()).getBody();
    }
}

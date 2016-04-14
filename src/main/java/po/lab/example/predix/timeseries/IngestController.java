package po.lab.example.predix.timeseries;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PreDestroy;
import java.io.IOException;

@RestController
@RequestMapping(value = "/wss", produces = MediaType.APPLICATION_JSON_VALUE)
public class IngestController {

    @Value("${demo.timeseries.ingestUrl}")
    String ingestUrl;

    @Value("${demo.timeseries.zoneId}")
    String zoneId;

    WebSocketClient wsc = new WebSocketClient();

    @Autowired
    @Qualifier("restTemplate")
    private OAuth2RestTemplate restTemplate;

    private String getAccessToken() {
        return restTemplate.getAccessToken().getValue();
    }

    @RequestMapping("/connect")
    public String connect() throws Exception {
        if (!wsc.isConnected()) {
            wsc.connect(ingestUrl, getAccessToken(), zoneId);
            return "Connected";
        }
        return "Failed to connect";
    }

    @PreDestroy
    @RequestMapping("/disconnect")
    private String disconnect() throws IOException {
        if (wsc.isConnected()) {
            wsc.disconnect();
        }
        return "Disconnected";
    }

    @RequestMapping("/ingest")
    public String ingest(@RequestParam(value = "amount", defaultValue = "1") int dataPointAmount) throws IOException {
        if (!wsc.isConnected()) {
            return "Not Connected";
        }
        wsc.sendMessage(DataPointGenerator.generateMessage(dataPointAmount));
        return "Ingested " + dataPointAmount + " datapoints.";
    }
}

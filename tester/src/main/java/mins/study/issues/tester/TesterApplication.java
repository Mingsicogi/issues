package mins.study.issues.tester;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class TesterApplication {

    public static void main(String[] args) {
        SpringApplication.run(TesterApplication.class, args);
    }

    @RestController
    @RequestMapping("/test")
    public static class Tester {

        RestTemplate restTemplate = new RestTemplate();
        ExecutorService service = Executors.newFixedThreadPool(100);

        @GetMapping("/servlet/async")
        public String asyncHttpRequest(@RequestParam(required = false) Integer times) {
            if(times == null || times == 0) {
                times = 100;
            }

            for (int i = 0; i < times; i++) {
                final Integer param = i;
                service.execute(() -> {
                    HttpHeaders headers = new HttpHeaders();
                    headers.setBearerAuth("token_" + param);
                    restTemplate.postForEntity("http://localhost:8081/servlet/thread/issue", new HttpEntity<>(param, headers), String.class);
                });
            }

            return "OK";
        }

        @GetMapping("/servlet")
        public String httpRequest(@RequestParam(required = false) Integer times) {
            if(times == null || times == 0) {
                times = 100;
            }

            for (int i = 0; i < times; i++) {
                HttpHeaders headers = new HttpHeaders();
                headers.setBearerAuth("token_" + i);
                restTemplate.postForEntity("http://localhost:8081/servlet/thread/issue", new HttpEntity<>(i, headers), String.class);
            }

            return "OK";
        }
    }
}

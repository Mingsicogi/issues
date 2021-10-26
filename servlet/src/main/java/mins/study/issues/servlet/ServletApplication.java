package mins.study.issues.servlet;

import lombok.extern.slf4j.Slf4j;
import mins.study.issues.servlet.filter.MyFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static mins.study.issues.servlet.filter.InMemoryStorage.MEMORY_STORE;
import static mins.study.issues.servlet.filter.InMemoryStorage.MEMORY_STORE_ATTR;

@Slf4j
@SpringBootApplication
public class ServletApplication {

    public static void main(String[] args) {

        SpringApplication.run(ServletApplication.class, args);
    }

    @RestController
    public static class ServletTest {

        @PostMapping("/servlet/thread/issue")
        public String threadIssue(String number, HttpServletRequest request) {
            String header = request.getHeader(HttpHeaders.AUTHORIZATION);
            String attrHeader = (String)request.getAttribute("header_after");
            MEMORY_STORE_ATTR.add(attrHeader);
            if (!header.equals(attrHeader)) {
                log.error("@@@ different header and attribute value : {} | {}", header, attrHeader);
            }
            return "OK";
        }

        @GetMapping("/servlet/thread/issue/testResult")
        public String result() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("request header total data count : " + MEMORY_STORE.size());
            stringBuilder.append("\t\trequest header total data count except duplication data : " + MEMORY_STORE.stream().distinct().count());
            stringBuilder.append("\t\tattribute header total data count except duplication data : " + MEMORY_STORE_ATTR.stream().distinct().count());
            stringBuilder.append("\t\tattribute header total data count except duplication data : " + MEMORY_STORE_ATTR.stream().distinct().count());

            return stringBuilder.toString();
        }

        @GetMapping("/servlet/thread/issue/testResult/clear")
        public String result_clear() {
            MEMORY_STORE.clear();
            MEMORY_STORE_ATTR.clear();
            return "CLEAR";
        }
    }

    @Bean
    public FilterRegistrationBean<MyFilter> myFilterFilterRegistrationBean() {
        FilterRegistrationBean<MyFilter> filterFilterRegistrationBean = new FilterRegistrationBean<>();

        filterFilterRegistrationBean.addUrlPatterns("/servlet/thread/issue");
        filterFilterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);

        filterFilterRegistrationBean.setFilter(new MyFilter());

        return filterFilterRegistrationBean;
    }
}

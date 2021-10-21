package mins.study.issues.servlet.filter;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static mins.study.issues.servlet.filter.InMemoryStorage.MEMORY_STORE;

@Slf4j
public class MyFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;

        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("### header : {}", header);
        MEMORY_STORE.add(header);

        request.setAttribute("header_after", header);

        filterChain.doFilter(servletRequest, servletResponse);
    }
}

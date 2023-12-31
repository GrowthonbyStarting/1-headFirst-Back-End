package starting.growthon.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final CorsProperties corsProperties;

    public static final String ALLOWED_METHOD_NAMES = "GET,HEAD,POST,PUT,DELETE,TRACE,OPTIONS,PATCH";

    @Autowired
    public WebConfig(CorsProperties corsConfiguration) {
        this.corsProperties = corsConfiguration;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        /*
        registry.addMapping("/**")
                .allowedOrigins(corsProperties.getOrigins().toArray(new String[0]))
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Access-Control-Allow-Origin")
                .allowCredentials(true);
        registry.addMapping("/auth/**")
                .allowedOrigins(corsProperties.getOrigins().toArray(new String[0]))
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Access-Control-Allow-Origin")
                .allowCredentials(true);
        registry.addMapping("/mentor/**")
                .allowedOrigins(corsProperties.getOrigins().toArray(new String[0]))
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Access-Control-Allow-Origin")
                .allowCredentials(true);
         */
        // 참고: https://velog.io/@wnguswn7/CORS-%EC%97%90%EB%9F%AC-Access-to-XMLHttpRequest-at-from-origin-has-been-blocked-by-CORS-policy
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "http://43.201.17.248:3000")
                .allowedOriginPatterns("*") // 안에 해당 주소를 넣어도 됨
                .allowedHeaders("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS" , "PATCH")
                .allowCredentials(true);

        registry.addMapping("/mentor/**")
                .allowedOrigins("http://localhost:3000", "http://43.201.17.248:3000")
                .allowedOriginPatterns("*") // 안에 해당 주소를 넣어도 됨
                .allowedHeaders("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS" , "PATCH")
                .allowCredentials(true);
    }
}

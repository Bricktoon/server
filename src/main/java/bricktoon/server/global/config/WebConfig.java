package bricktoon.server.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://bricktoonserver-env.eba-zgg9mm72.ap-northeast-2.elasticbeanstalk.com",
                        "https://www.bricktoon-server2025.shop",
                        "https://front-bricktoon-1myj.vercel.app",
                        "https://www.bricktoon2025.store",
                        "http://localhost:8080", "http://localhost:5173")
                .allowedMethods("GET", "POST", "DELETE", "PATCH", "PUT", "OPTIONS")
                .allowCredentials(true)
                .maxAge(3000);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RateLimitingInterceptor());
    }

}

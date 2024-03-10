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
                .allowedOrigins("http://bricktoonserver-env.eba-cbaq3hyp.ap-northeast-2.elasticbeanstalk.com",
                        "https://www.bricktoon-server.shop",
                        "https://front-bricktoon-1myj.vercel.app",
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

package com.campus.media.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
        
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:./uploads/");
    }
    
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index.html");
        registry.addViewController("/login").setViewName("forward:/index.html");
        registry.addViewController("/home").setViewName("forward:/index.html");
        registry.addViewController("/upload").setViewName("forward:/index.html");
        registry.addViewController("/dashboard").setViewName("forward:/index.html");
        registry.addViewController("/admin/media").setViewName("forward:/index.html");
        registry.addViewController("/admin/rules").setViewName("forward:/index.html");
        registry.addViewController("/admin/users").setViewName("forward:/index.html");
        registry.addViewController("/admin/audit").setViewName("forward:/index.html");
    }
}

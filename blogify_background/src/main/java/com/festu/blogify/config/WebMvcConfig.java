package com.festu.blogify.config;

import com.festu.blogify.interceptor.AuthInterceptor;
import com.festu.blogify.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    FileService fileService;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //将匹配上/files/**虚拟路径的url映射到文件上传到服务器的目录，获取静态资源
        registry.addResourceHandler(fileService.getPathPattern() + "**").addResourceLocations("file:" + fileService.getUploadPath());
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 表明允许哪些域访问, 简单点可为 *
                .allowedOrigins("http://localhost:8080")
                .allowedHeaders("*")
                .allowedMethods("*")
                // allowCredentials(true): 表示附带身份凭证
                // 一旦使用 allowCredentials(true) 方法，则 allowedOrigins("*") 需要指明特定的域，而不能是 *
                .allowCredentials(true)
                .maxAge(86400);
    }

    @Bean
    public AuthInterceptor getAuthInterceptor() {
        return new AuthInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(getAuthInterceptor())
                .addPathPatterns("/api/v1/**")
                .excludePathPatterns("/api/v1/user/login")
                .excludePathPatterns("/api/v1/role/auth")
                .excludePathPatterns("/api/v1/display/**");
    }
}

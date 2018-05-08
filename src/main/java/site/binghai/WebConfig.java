package site.binghai;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import site.binghai.inters.BaseInterceptor;

/**
 * Created by IceSea on 2018/5/7.
 * GitHub: https://github.com/IceSeaOnly
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Bean
    public BaseInterceptor userInter(){
        return new BaseInterceptor() {
            @Override
            protected String getTag() {
                return "user";
            }
        };
    }

    @Bean
    public BaseInterceptor officeInter(){
        return new BaseInterceptor() {
            @Override
            protected String getTag() {
                return "office";
            }
        };
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/dashBoard/").setViewName("dashBoard");
        registry.addViewController("/").setViewName("dashBoard");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInter()).addPathPatterns("/user/**");
        registry.addInterceptor(officeInter()).addPathPatterns("/office/**");
    }
}

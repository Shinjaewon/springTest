package com.qo_op.api.conf;

import com.qo_op.api.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;


@Configuration
public class WebConfig {

    @Autowired
    private MemberRepository memberRepository;

    @Bean
    public FilterRegistrationBean loginCheckFilter() {

        FilterRegistrationBean<Filter> filterRegistration = new FilterRegistrationBean<>();
        filterRegistration.setFilter(new LoginCheckFilter(memberRepository));
        filterRegistration.setOrder(1);
        filterRegistration.addUrlPatterns("/myInfo");
        return filterRegistration;
    }
}

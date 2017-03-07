/*
package com.springboot.zero.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf()
                .disable().authorizeRequests().antMatchers("/").permitAll();


//        http.httpBasic().and().authorizeRequests()
//                .antMatchers("/send-pin").permitAll()
//                .antMatchers("/check-pin").permitAll()
//                .antMatchers("/index.html", "/", "/login", "/someotherrurl")
//                .permitAll().anyRequest().authenticated().and().csrf()
//                .csrfTokenRepository(csrfTokenRepository()).and()
//                .addFilterAfter(csrfHeaderFilter(), CsrfFilter.class);
    }

}*/

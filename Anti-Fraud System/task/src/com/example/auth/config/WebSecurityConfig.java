package com.example.auth.config;

import com.example.auth.entity.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final UserDetailsService userDetailsService;

    public WebSecurityConfig(RestAuthenticationEntryPoint restAuthenticationEntryPoint, UserDetailsService userDetailsService) {
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(getEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .csrf().disable().headers().frameOptions().disable()
                .and()
                .authorizeRequests() // manage access
                .antMatchers(HttpMethod.POST, "/api/auth/user").permitAll()
                .antMatchers(HttpMethod.POST, "/api/antifraud/transaction/**").hasAnyRole(Role.MERCHANT.getName())
                .antMatchers(HttpMethod.PUT, "/api/antifraud/transaction/**").hasAnyRole(Role.SUPPORT.getName())
                .antMatchers(HttpMethod.GET, "/api/antifraud/history/**").hasAnyRole(Role.SUPPORT.getName())
                .antMatchers(HttpMethod.PUT, "/api/auth/access/**").hasAnyRole(Role.ADMINISTRATOR.getName())
                .antMatchers(HttpMethod.PUT, "/api/auth/role/**").hasAnyRole(Role.ADMINISTRATOR.getName())
                .antMatchers(HttpMethod.DELETE,"/api/auth/user/**").hasAnyRole(Role.ADMINISTRATOR.getName())
                .mvcMatchers("/api/auth/list").hasAnyRole(Role.ADMINISTRATOR.getName(),Role.SUPPORT.getName())
                .mvcMatchers("/api/antifraud/suspicious-ip**/**","/api/antifraud/stolencard**/**").hasRole(Role.SUPPORT.getName())
                .antMatchers("/actuator/shutdown").permitAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }
}

package com.binarxbca.chapter6.config;

import com.binarxbca.chapter6.model.enums.ERoles;
import com.binarxbca.chapter6.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers(
                        "/chapter6/public/checkUsernameAvailability",
                        "/chapter6/public/checkEmailAvailability",
                        "/chapter6/public/signin",
                        "/chapter6/public/users/signup",
                        "/chapter6/public/admin/signup",
                        "/chapter6/public/films",
                        "/chapter6/public/films/showing",
                        "/chapter6/public/films/{id}",
                        "swagger-ui/**"
                ).permitAll()
                .antMatchers(
                        "/chapter6/auth/films/add",
                        "/chapter6/auth/films/update/{id}",
                        "/chapter6/auth/films/delete/{id}",
                        "/chapter6/auth/schedules",
                        "/chapter6/auth/schedules/add",
                        "/chapter6/auth/schedules/{id}",
                        "/chapter6/auth/schedules/update/{id}",
                        "/chapter6/auth/schedules/delete/{id}",
                        "/chapter6/auth/seats",
                        "/chapter6/auth/seats/add",
                        "/chapter6/auth/seats/{id}",
                        "/chapter6/auth/seats/update/{id}",
                        "/chapter6/auth/seats/delete/{id}",
                        "/chapter6/auth/users",
                        "/chapter6/auth/users/{username}/profile",
                        "/chapter6/auth/users/update/{username}",
                        "/chapter6/auth/users/delete{username}"
                ).hasRole(ERoles.ADMIN.name())
                .antMatchers(
                        "/chapter6/auth/invoices",
                        "/chapter6/auth/invoices/**",
                        "/chapter6/auth/schedules",
                        "/chapter6/auth/schedules/{id}",
                        "/chapter6/auth/seats",
                        "/chapter6/auth/seats/{id}",
                        "/chapter6/auth/users/{username}/profile",
                        "/chapter6/auth/users/update/{username}",
                        "/chapter6/auth/users/delete/{username}",
                        "/chapter6/auth/users/{username}/films"
                ).hasRole(ERoles.CUSTOMER.name())
                .anyRequest()
                .authenticated();

        http.addFilterBefore(
                authenticationJwtTokenFilter(),
                UsernamePasswordAuthenticationFilter.class
        );
    }
}

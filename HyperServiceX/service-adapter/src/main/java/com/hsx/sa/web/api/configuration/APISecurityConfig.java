package com.hsx.sa.web.api.configuration;

import com.hsx.sa.web.api.configuration.filters.APIKeyAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@Configuration
@EnableWebSecurity
@Order(1)
//todo : This is just a place holder config for filtering. Will be developed once we finalize the authentication and authorization flows
public class APISecurityConfig extends WebSecurityConfigurerAdapter {

    private final Logger logger = LoggerFactory.getLogger(APISecurityConfig.class);

    @Value("${default-security-key:Authorization}")
    private String principalRequestHeader;

    @Value("${authToken:tempVal}")
    private String principalRequestValue;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        APIKeyAuthenticationFilter filter = new APIKeyAuthenticationFilter(principalRequestHeader);
        filter.setAuthenticationManager(new AuthenticationManager() {

            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                logger.info("Authenticating -------------------------------------");
                String principal = (String) authentication.getPrincipal();
                authentication.setAuthenticated(true);
                return authentication;
            }
        });
        httpSecurity.
                antMatcher("**/swagger-resources/**").authorizeRequests().
                and().
                antMatcher("*").
                csrf().disable().
                sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
                and().addFilter(filter).authorizeRequests().anyRequest().authenticated();


//        httpSecurity.
//                antMatcher("**/swagger-resources/**").authorizeRequests().
//                and().
//                antMatcher("*").
//                csrf().disable().
//                sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
//                and().addFilter(filter).authorizeRequests().anyRequest().authenticated();
    }
}

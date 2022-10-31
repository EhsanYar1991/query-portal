package com.yar.iot.queryportal.config;


import com.yar.iot.queryportal.config.security.JwtAuthenticationEntryPoint;
import com.yar.iot.queryportal.config.security.JwtRequestFilter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class WebSecurityConfig {

    private static final String[] SWAGGER_URLS = {
            "/swagger-ui.html/**",
            "/webjars/**",
            "/configuration/**",
            "/swagger-resources/**",
            "/v2/api-docs"
    };

    private static final String[] AUTHORIZED_URLS = {
            "/auth/login",
            "/auth/authenticate",
            "/auth/register"
    };

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final UserDetailsService userDetailsService;
    private final JwtRequestFilter jwtRequestFilter;
    private final PasswordEncoder passwordEncoder;

    /**
     * Create {@link SecurityFilterChain} bean
     *
     * @param http The http security
     * @return {@link SecurityFilterChain}
     * */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(AUTHORIZED_URLS).permitAll()
                .anyRequest().authenticated()
                .and()
                .logout()
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        return http.build();
    }

    /**
     * Create {@link WebSecurityCustomizer} bean
     *
     * @return {@link WebSecurityCustomizer}
     * */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().antMatchers(SWAGGER_URLS);
    }

    /**
     * Create {@link AuthenticationManager} bean
     *
     * @return {@link AuthenticationManager}
     * */
    @Bean
    public AuthenticationManager authenticationManager()  {
        return authentication -> {
            UserDetails userDetails = userDetailsService.loadUserByUsername(authentication.getName());
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(authentication.getName(),
                    authentication.getCredentials(), userDetails.getAuthorities());
            usernamePasswordAuthenticationToken.setDetails(userDetails);
            return usernamePasswordAuthenticationToken;
        };
    }

    /**
     * Setting the custom user detail service and password encoder for authentication manager
     *
     * @param auth the {@link AuthenticationManagerBuilder}
     * */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

}

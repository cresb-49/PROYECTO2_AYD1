/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import usac.api.filters.JwtRequestFilter;
import usac.api.services.authentification.AuthenticationService;

/**
 *
 * @author Luis Monterroso
 */
@Configuration
@EnableWebSecurity
public class PathsSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationService service;
    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    /**
     * Configuracion de los roles autorizados para hacer request con urls
     * especiales
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .antMatchers("/api/**/public/**").permitAll()
                .antMatchers("/api/**/private/**").hasAnyRole("ADMIN")
                .antMatchers("/api/**/protected/**").hasAnyRole("CLIENTE", "ADMIN")
                .antMatchers("/api/**/cliente/**").hasRole("CLIENTE")
                .anyRequest().authenticated()
                .and()
                .cors()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //agregamos el filtro jwt antes de los demas filtros
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(service);//indicamos que el manager se apoye en un servicio de autenticacion
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

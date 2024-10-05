/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.filters;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import usac.api.services.authentification.JwtGeneratorService;

/**
 *
 * @author luism
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtGeneratorService jwtGeneratorService;

    @Override
    public void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String jwt = header.substring(7);
            String user = jwtGeneratorService.extractUserName(jwt);

            if (user != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(user);

                if (jwtGeneratorService.validateTOken(jwt, userDetails)) {

                    UsernamePasswordAuthenticationToken authenticationToken
                            = new UsernamePasswordAuthenticationToken(
                                    userDetails, null,
                                    userDetails.getAuthorities());

                    authenticationToken.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(
                            authenticationToken);

                    System.out.println("User authenticated: " + user); // Debugging

                    // Imprimir roles del usuario
                    Collection<? extends GrantedAuthority> authorities
                            = userDetails.getAuthorities();

                    System.out.println("User roles: " + authorities.stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.joining(", ")));
                } else {
                    System.out.println("JWT validation failed."); // Debugging
                }
            }
        }
        filterChain.doFilter(request, response);
    }

}

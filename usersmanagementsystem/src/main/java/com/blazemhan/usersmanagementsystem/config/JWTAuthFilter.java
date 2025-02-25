package com.blazemhan.usersmanagementsystem.config;

import com.blazemhan.usersmanagementsystem.service.JWTUtils;
import com.blazemhan.usersmanagementsystem.service.UserDetailsImpls;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTAuthFilter  extends OncePerRequestFilter {


    private final JWTUtils jwtUtils;

    private final UserDetailsImpls userDetailsImpls;


    public JWTAuthFilter(JWTUtils jwtUtils, UserDetailsImpls userDetailsImpls) {
        this.jwtUtils = jwtUtils;
        this.userDetailsImpls = userDetailsImpls;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            final String token = authorizationHeader.substring(7);
            String userEmail = jwtUtils.extractUsername(token);
            if(userEmail != null && SecurityContextHolder.getContext().getAuthentication()== null){

                UserDetails userDetails = userDetailsImpls.loadUserByUsername(userEmail);

                if(jwtUtils.isValid(token,userDetails)){
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,
                            null,userDetails.getAuthorities());

                    authenticationToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);


                }
            }
            filterChain.doFilter(request,response);



        }
        }

}

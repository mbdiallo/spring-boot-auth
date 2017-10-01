package com.ginteq.springbootauth.endpoint.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ginteq.springbootauth.endpoint.repository.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;


import static com.ginteq.springbootauth.endpoint.security.SecurityConstants.*;

/**
 * Created by bachir on 01/10/2017.
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
    }

    @Override
    public  Authentication attemptAuthentication(HttpServletRequest req,
                                                 HttpServletResponse res) throws AuthenticationException {
        try{
            User creds = new ObjectMapper().readValue(req.getInputStream(),User.class);

            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    creds.getUserName(),
                    creds.getPassword(),
                    new ArrayList<>()
            ));
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                             HttpServletResponse res,
                                             FilterChain chain,
                                             Authentication auth) throws IOException{
        String token = Jwts.builder()
                .setSubject(((User) auth.getPrincipal()).getUserName())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512,SECRET)
                .compact();
        res.addHeader(HEADER_STRING,TOKEN_PREFIX + token);

    }
}

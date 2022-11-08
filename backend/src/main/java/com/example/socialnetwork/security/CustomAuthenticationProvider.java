package com.example.socialnetwork.security;

import com.example.socialnetwork.dao.UserDAO;
import com.example.socialnetwork.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private UserDAO userDAO;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        System.out.println("custom auth provider");
        if (authentication == null || authentication.getPrincipal() == null || authentication.getCredentials() == null)
            throw new InsufficientAuthenticationException("Username or password not provided.");

        String username = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();

        Optional<User> found = userDAO.findByUsername(username);
        System.out.println("1");
        System.out.println(username);
        System.out.println(password);
        System.out.println(found.get().getPassword());
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

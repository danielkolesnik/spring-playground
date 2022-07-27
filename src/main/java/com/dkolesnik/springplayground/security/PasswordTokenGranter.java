package com.dkolesnik.springplayground.security;

import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Description
 *
 * @author Daniel A. Kolesnik <daneelkolesnik@gmail.com>
 * @version 0.1.1
 * @since 2022-07-26
 */
public class PasswordTokenGranter extends AbstractTokenGranter {
    
    public static final String GRANT_TYPE = "password";
    
    private final AuthenticationManager authenticationManager;

    public PasswordTokenGranter(AuthorizationServerEndpointsConfigurer endpointsConfigurer, AuthenticationManager authenticationManager) {
        super(endpointsConfigurer.getTokenServices(), endpointsConfigurer.getClientDetailsService(), endpointsConfigurer.getOAuth2RequestFactory(), GRANT_TYPE);

        this.authenticationManager = authenticationManager;
    }

    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters = new LinkedHashMap(tokenRequest.getRequestParameters());
        String username = parameters.get("username");
        String password = parameters.get("password");
        // Protect from downstream leaks of password
        parameters.remove("password");
        
        Authentication userAuthentication = new UsernamePasswordAuthenticationToken(username, password);
        ((AbstractAuthenticationToken)userAuthentication).setDetails(parameters);

        try {
            userAuthentication = this.authenticationManager.authenticate(userAuthentication);
        } catch (AccountStatusException ase) {
            //covers expired, locked, disabled cases (mentioned in section 5.2, draft 31)
            throw new InvalidGrantException(ase.getMessage());
            
        } catch (BadCredentialsException e) {
            // If the username/password are wrong the spec says we should send 400/invalid grant
            throw new InvalidGrantException(e.getMessage());
            
        } catch (UsernameNotFoundException var10) {
            throw new InvalidGrantException("username not found");
        }

        if (userAuthentication != null && userAuthentication.isAuthenticated()) {
            OAuth2Request storedOAuth2Request = this.getRequestFactory().createOAuth2Request(client, tokenRequest);
            
            // MFA could be integrated here
            
            return new OAuth2Authentication(storedOAuth2Request, userAuthentication);
            
        } else {
            throw new InvalidGrantException("Could not authenticate user:" + username);
        }
    }
}

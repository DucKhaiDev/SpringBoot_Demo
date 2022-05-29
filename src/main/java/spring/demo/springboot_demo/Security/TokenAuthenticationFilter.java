package spring.demo.springboot_demo.Security;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Getter
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    @Value("${jwt.header}")
    private String AUTH_HEADER;

    TokenHelper tokenHelper;
    UserDetailsService userDetailsService;

    @Autowired
    public void setTokenHelper(TokenHelper tokenHelper) {
        this.tokenHelper = tokenHelper;
    }

    @Autowired
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    private String getToken(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTH_HEADER);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String error = "";
        String authToken = getToken(request);

        if (authToken != null) {
            String username = tokenHelper.getUsernameFromToken(authToken);

            if (username != null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                TokenBasedAuthentication authentication = new TokenBasedAuthentication(userDetails);
                authentication.setToken(authToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                error = "Username from token can't be found in DB.";
            }
        } else {
            error = "Authentication failed - no Bearer token provided.";
        }

        if (!error.equals("")) {
            System.out.println(error);
            SecurityContextHolder.getContext().setAuthentication(new AnonAuthentication());
        }

        filterChain.doFilter(request, response);
    }
}

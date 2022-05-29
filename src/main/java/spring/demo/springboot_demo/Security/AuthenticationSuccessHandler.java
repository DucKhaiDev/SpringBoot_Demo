package spring.demo.springboot_demo.Security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Value("${jwt.expires_in}")
    private int EXPIRES_IN;

    TokenHelper tokenHelper;
    ObjectMapper objectMapper;

    @Autowired
    public void setTokenHelper(TokenHelper tokenHelper) {
        this.tokenHelper = tokenHelper;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        clearAuthenticationAttributes(request);
        User user = (User) authentication.getPrincipal();
        String jwt = tokenHelper.generateToken(user.getUsername());

        UserTokenState userTokenState = new UserTokenState(jwt, EXPIRES_IN);

        try {
            String jwtResponse = objectMapper.writeValueAsString(userTokenState);
            response.setContentType("application/json");
            response.getWriter().write(jwtResponse);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @AllArgsConstructor
    @Getter
    @Setter
    private static class UserTokenState {
        private String jws;
        private int expires;

    }
}

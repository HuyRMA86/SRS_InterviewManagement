package fa.training.interviewmanagement.controllerAdvice;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;


public class CustomAuthenticationFailureHandler
        implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception)
            throws IOException, ServletException {
        if (exception.getMessage().equals("User is disabled")) {
            request.setAttribute("message", "Account no activated");
        } else if (exception.getMessage().equals("Bad credentials")) {
            request.setAttribute("message", "Invalid username/password. Please try again");
        } else {
            request.setAttribute("message", exception.getMessage());
        }
        request.getRequestDispatcher("/login").forward(request, response);
    }


}
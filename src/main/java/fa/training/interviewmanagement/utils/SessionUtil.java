package fa.training.interviewmanagement.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

@Component
public class SessionUtil {
    public void setTitle(HttpServletRequest req, String title){
        HttpSession httpSession = req.getSession();
        httpSession.setAttribute("title", title);
    }
}

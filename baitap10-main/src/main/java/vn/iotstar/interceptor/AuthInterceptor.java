package vn.iotstar.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import vn.iotstar.entity.User;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        String uri = request.getRequestURI();

        if (uri.startsWith("/admin")) {
            if (user == null || !"admin".equals(user.getRole())) {
                response.sendRedirect("/login");
                return false;
            }
        } else if (uri.startsWith("/user")) {
            if (user == null || !"user".equals(user.getRole())) {
                response.sendRedirect("/login");
                return false;
            }
        }
        return true;
    }
}

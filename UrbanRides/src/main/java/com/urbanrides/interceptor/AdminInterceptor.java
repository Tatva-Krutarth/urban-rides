package com.urbanrides.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.urbanrides.dtos.UserSessionObj;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class AdminInterceptor extends HandlerInterceptorAdapter {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession session = request.getSession();
        UserSessionObj userSessionObj = (UserSessionObj) session.getAttribute("adminSessionObj");

        if (userSessionObj == null) {
            response.sendRedirect(request.getContextPath() + "/no-session");
            return false;
        }

        int accountStatus = userSessionObj.getAccountStatus();
        String currentUri = request.getRequestURI();

        if (currentUri.equals(request.getContextPath() + "/admin/admin-personal-details-submit")) {
            return true;
        }

        switch (accountStatus) {
            case 1:
                if (!currentUri.equals(request.getContextPath() + "/admin/admin-personal-details")) {
                    response.sendRedirect(request.getContextPath() + "/admin/admin-personal-details");
                    return false;
                }
                break;
            case 5:
                return true;

            case 6:
                if (!currentUri.equals(request.getContextPath() + "/admin/blocked")) {
                    response.sendRedirect(request.getContextPath() + "/admin/blocked");
                    return false;
                }
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/no-session");
                return false;
        }

        return true;
    }


}

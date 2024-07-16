package com.urbanrides.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.urbanrides.dtos.UserSessionObj;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class AdminInterceptor extends HandlerInterceptorAdapter {

//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
//            throws Exception {
//        HttpSession session = request.getSession();
//        UserSessionObj userSessionObj = (UserSessionObj) session.getAttribute("adminSessionObj");
//        System.out.println(userSessionObj);
//        if (userSessionObj == null) {
//            response.sendRedirect(request.getContextPath() + "/no-session");
//            return false;
//        }
//
//        int accountStatus = userSessionObj.getAccountStatus();
//
//        switch (accountStatus) {
//            case 1:
//                response.sendRedirect(request.getContextPath() + "/admin/admin-personal-details");
//                System.out.println("personal details");
//                break;
//            case 2:
//                response.sendRedirect(request.getContextPath() + "/rider/rider-dashboard");
//                System.out.println("personal dashboard");
//
//                break;
//            case 3:
//                response.sendRedirect(request.getContextPath() + "/rider/blocked");
//                System.out.println("Blocked");
//
//                break;
//            default:
//                // Handle any other cases if necessary
//                System.out.println("session");
//
//                response.sendRedirect(request.getContextPath() + "/no-session");
//                break;
//        }
//
//        return false; // Return false to stop further handling
//    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession session = request.getSession();
        UserSessionObj userSessionObj = (UserSessionObj) session.getAttribute("adminSessionObj");
        System.out.println(userSessionObj);

        if (userSessionObj == null) {
            response.sendRedirect(request.getContextPath() + "/no-session");
            return false;
        }

        int accountStatus = userSessionObj.getAccountStatus();
        String currentUri = request.getRequestURI();

        if (currentUri.equals(request.getContextPath() + "/admin/admin-personal-details-submit")) {
            return true; // Proceed with form submission
        }

        switch (accountStatus) {
            case 1:
                if (!currentUri.equals(request.getContextPath() + "/admin/admin-personal-details")) {
                    System.out.println("pre handler");
                    response.sendRedirect(request.getContextPath() + "/admin/admin-personal-details");
                    return false;
                }
                break;
            case 2:

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

        return true; // Proceed with the request
    }


}

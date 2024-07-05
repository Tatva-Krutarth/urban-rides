


package com.urbanrides.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.urbanrides.dtos.UserSessionObj;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class CaptainInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession session = request.getSession();
        UserSessionObj userSessionObj = (UserSessionObj) session.getAttribute("captainSessionObj");
        System.out.println(userSessionObj);
        if (userSessionObj == null) {
            response.sendRedirect(request.getContextPath() + "/no-session");
            return false;
        }
//
//        int accountStatus = userSessionObj.getAccountStatus();
//        String currentUri = request.getRequestURI();
//
//        if (currentUri.equals(request.getContextPath() + "/captain/captain-personal-details-submit")) {
//            return true; // Proceed with form submission
//        }
//        if (currentUri.equals(request.getContextPath() + "/captain/captain-document-details-submit")) {
//            return true; // Proceed with form submission
//        }
//
//        switch (accountStatus) {
//            case 1:
//                if (!currentUri.equals(request.getContextPath() + "/captain/captain-personal-details")) {
//                    System.out.println("pre handler1");
//                    response.sendRedirect(request.getContextPath() + "/captain/captain-personal-details");
//                    return false;
//                }
//                break;
//            case 2:
//                if (!currentUri.equals(request.getContextPath() + "/captain/captain-document-details")) {
//                    System.out.println("pre handler2");
//                    response.sendRedirect(request.getContextPath() + "/captain/captain-document-details");
//                    return false;
//                }
//                break;
//            case 3:
//                if (!currentUri.equals(request.getContextPath() + "/captain/captain-waiting-page")) {
//                    System.out.println("pre handler3");
//                    response.sendRedirect(request.getContextPath() + "/captain/captain-waiting-page");
//                    return false;
//                }
//                break;
//            case 4:
//                if (!currentUri.equals(request.getContextPath() + "/captain/captain-document-details-reupload")) {
//                    System.out.println("pre handler4");
//                    response.sendRedirect(request.getContextPath() + "/captain/captain-document-details-reupload");
//                    return false;
//                }
//                break;
//            case 5:
//                System.out.println("5 is skip");
//                return true;
//            case 6:
//                if (!currentUri.equals(request.getContextPath() + "/captain/blocked")) {
//                    response.sendRedirect(request.getContextPath() + "/captain/blocked");
//                    return false;
//                }
//                break;
//            default:
//                response.sendRedirect(request.getContextPath() + "/no-session");
//                return false;
//        }

        return true; // Proceed with the request
    }


}




package com.urbanrides.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.urbanrides.dtos.UserSessionObjDto;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class CaptainInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession session = request.getSession();
        UserSessionObjDto userSessionObj = (UserSessionObjDto) session.getAttribute("captainSessionObj");
        if (userSessionObj == null) {
            response.sendRedirect(request.getContextPath() + "/no-session");
            return false;
        }

        int accountStatus = userSessionObj.getAccountStatus();
        String currentUri = request.getRequestURI();

        if (currentUri.equals(request.getContextPath() + "/captain/captain-personal-details-submit")) {
            return true;
        }
        if (currentUri.equals(request.getContextPath() + "/captain/captain-document-details-submit")) {
            return true;
        }
        if (currentUri.equals(request.getContextPath() + "/captain/captain-reupload-document-details-submit")) {
            return true;
        }
        if (currentUri.equals(request.getContextPath() + "/captain/get-captain-document-reupload-details")) {
            return true;
        }
        if (currentUri.equals(request.getContextPath() + "/captain/captain-logout")) {
            return true;
        }
        switch (accountStatus) {
            case 1:
                if (!currentUri.equals(request.getContextPath() + "/captain/captain-personal-details")) {
                    response.sendRedirect(request.getContextPath() + "/captain/captain-personal-details");
                    return false;
                }
                break;
            case 2:
                if (!currentUri.equals(request.getContextPath() + "/captain/captain-document-details")) {
                    response.sendRedirect(request.getContextPath() + "/captain/captain-document-details");
                    return false;
                }
                break;
            case 3:
                if (!currentUri.equals(request.getContextPath() + "/captain/captain-waiting-page")) {
                    response.sendRedirect(request.getContextPath() + "/captain/captain-waiting-page");
                    return false;
                }
                break;
            case 4:
                if (!currentUri.equals(request.getContextPath() + "/captain/captain-document-details-reupload")) {
                    response.sendRedirect(request.getContextPath() + "/captain/captain-document-details-reupload");
                    return false;
                }
                break;
            case 5:
                return true;
            case 6:
                if (!currentUri.equals(request.getContextPath() + "/captain/blocked")) {
                    response.sendRedirect(request.getContextPath() + "/captain/blocked");
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

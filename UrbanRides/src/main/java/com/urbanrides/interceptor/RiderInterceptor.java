//
//
//
//package com.urbanrides.interceptor;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
//
//public class RiderInterceptor extends HandlerInterceptorAdapter {
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
//            throws Exception {
//
////        HttpSession session = request.getSession();
////        Object riderSessionObj = session.getAttribute("riderSessionObj");
////
////        if (adminSessionObj == null) {
////            response.sendRedirect(request.getContextPath() + "/sessionNotFound");
////            return false;
////        }
//
//        return true;
//    }
//
//
//}

package edu.agh.iet.BSc_Thesis.Controller

import edu.agh.iet.BSc_Thesis.Util.JwtUtils
import edu.agh.iet.BSc_Thesis.Util.JwtUtils.getUsername
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class AuthInterceptor : HandlerInterceptorAdapter() {
    @Throws(Exception::class)
    override fun preHandle(request: HttpServletRequest,
                           response: HttpServletResponse, handler: Any): Boolean {
        val reqUri = request.requestURI
        val serviceName = reqUri.substring(reqUri.lastIndexOf("/") + 1,
                reqUri.length)
        return if (!listOf("login", "register", "mock").contains(serviceName) && request.method != "OPTIONS") {
            val token = request.getHeader("Token")
            JwtUtils.validateToken(token)
        } else true
    }
}
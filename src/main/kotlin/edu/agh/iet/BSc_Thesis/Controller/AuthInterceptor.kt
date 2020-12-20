package edu.agh.iet.BSc_Thesis.Controller

import edu.agh.iet.BSc_Thesis.Config.CustomProperties
import edu.agh.iet.BSc_Thesis.Util.JwtUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class AuthInterceptor : HandlerInterceptorAdapter() {

    @Autowired
    private val customProperties: CustomProperties? = null

    @Throws(Exception::class)
    override fun preHandle(request: HttpServletRequest,
                           response: HttpServletResponse, handler: Any): Boolean {
        val reqUri = request.requestURI
        val serviceName = reqUri.substring(reqUri.lastIndexOf("/") + 1,
                reqUri.length)
        return if (!listOf("login", "register", "mock").contains(serviceName) && request.method != "OPTIONS") {
            val token = request.getHeader("Token")
            if(token.equals("whiteboard_status")) true
            else if (token == "chat_status") true
            else {
                try {
                    JwtUtils.validateToken(token)
                } catch (ex: io.jsonwebtoken.JwtException) {
                    response.status = 401
                    return false;
                }
            }
        } else true
    }
}
package edu.agh.iet.BSc_Thesis.Config

import edu.agh.iet.BSc_Thesis.Repositories.UserRepository
import edu.agh.iet.BSc_Thesis.Util.JwtUtils
import edu.agh.iet.BSc_Thesis.Util.JwtUtils.getUsername
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthorizationFilter : OncePerRequestFilter() {

    @Autowired
    lateinit var userRepository: UserRepository

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val token = request.getHeader("Authorization")
        val username = JwtUtils.getClaimsFromToken(token).getUsername()
        val user = userRepository.getUserByUsername(username)
        if (user != null && JwtUtils.validateToken(user, token)) {
            filterChain.doFilter(request, response)
        }
    }

}
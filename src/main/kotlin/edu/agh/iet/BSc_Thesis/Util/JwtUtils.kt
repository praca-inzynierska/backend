package edu.agh.iet.BSc_Thesis.Util

import edu.agh.iet.BSc_Thesis.Model.Entities.User
import edu.agh.iet.BSc_Thesis.Repositories.UserRepository
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
object JwtUtils {
    private const val secret: String = "secretkeychangeitpls"
    private const val defaultTokenTimeout = 5 * 60 * 60

    lateinit var userRepository: UserRepository

    fun generateToken(userDetails: User): String {
        val claims: Map<String, Any> = HashMap()
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.username)
                .setIssuedAt(Date(System.currentTimeMillis()))
                .setExpiration(Date(System.currentTimeMillis() + defaultTokenTimeout * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact()
    }

    fun validateToken(token: String): Boolean {
        val claims = getClaimsFromToken(token)
        val usernameFromToken = claims.subject
        val expirationDate = claims.expiration
        val userExists = userRepository.getUserByUsername(usernameFromToken) != null

        return (expirationDate.after(Date(System.currentTimeMillis())) && userExists)
    }

    fun getClaimsFromToken(token: String): Claims {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token.trim()).body;
    }

    fun Claims.getUsername(): String {
        return this.subject
    }

    fun getUserFromToken(token: String): User {
        return userRepository.getUserByUsername(getClaimsFromToken(token).getUsername())!!
    }

    fun isTeacher(token: String): Boolean {
        val username = getClaimsFromToken(token).getUsername()
        val user = userRepository.getUserByUsername(username)
        return (user != null && user.isTeacher)
    }
}
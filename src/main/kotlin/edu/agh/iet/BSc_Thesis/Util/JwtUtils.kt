package edu.agh.iet.BSc_Thesis.Util

import edu.agh.iet.BSc_Thesis.Model.Entities.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import java.util.*


object JwtUtils {
    private const val secret: String = "secretkeychangeitpls"
    private const val defaultTokenTimeout = 5 * 60 * 60

    fun generateToken(userDetails: User): String {
        val claims: Map<String, Any> = HashMap()
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.username)
                .setIssuedAt(Date(System.currentTimeMillis()))
                .setExpiration(Date(System.currentTimeMillis() + defaultTokenTimeout * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact()
    }

    fun validateToken(user: User, token: String): Boolean {
        val claims = getClaimsFromToken(token)
        val usernameFromToken = claims.subject
        val expirationDate = claims.expiration

        return (expirationDate.after(Date(System.currentTimeMillis())))
    }

    fun getClaimsFromToken(token: String): Claims {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    fun Claims.getUsername(): String {
        return this.subject
    }
}
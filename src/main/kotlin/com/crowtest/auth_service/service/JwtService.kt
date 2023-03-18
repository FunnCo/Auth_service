package com.crowtest.auth_service.service

import com.crowtest.auth_service.entity.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*
import java.util.function.Function

@Service
class JwtService {

    val SECRET_KEY = "38792F423F4528482B4D6251655468566D597133743677397A24432646294A404E635266556A586E5A7234753778214125442A472D4B6150645367566B597033"

    fun isTokenValid(token: String, userDetails: User): Boolean{
        val username = extractUsername(token)
        return username == userDetails.userId.toString() && !isTokenExpired(token)
    }

    private fun isTokenExpired(token: String): Boolean {
          return extractClaim(token, Claims::getExpiration).before(Date(System.currentTimeMillis()))
    }

    fun extractUsername(token: String): String? {
        return extractClaim(token, Claims::getSubject);
    }

    fun generateToken(extraClaims : MutableMap<String, Any>, userDetails: User):String{
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.userId.toString())
                .setIssuedAt(Date(System.currentTimeMillis()))
                .setExpiration(Date(System.currentTimeMillis() + 1000*60*60*24))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact()
    }

    private fun <T> extractClaim(token: String, claimResolver: Function<Claims, T> ): T{
        val claims = extractAllClaims(token)
        return claimResolver.apply(claims)
    }

    private fun extractAllClaims(token: String): Claims{
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).body
    }

    private fun getSigningKey(): Key {
        val keyBytes = Decoders.BASE64.decode(SECRET_KEY)
        return Keys.hmacShaKeyFor(keyBytes)
    }

}

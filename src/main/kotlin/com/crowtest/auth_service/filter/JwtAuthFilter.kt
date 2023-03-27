package com.crowtest.auth_service.filter

import com.crowtest.auth_service.entity.User
import com.crowtest.auth_service.service.JwtService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter(
        val jwtService: JwtService,
        val userDetailsService: UserDetailsService
        ) : OncePerRequestFilter() {

    private val logger = LoggerFactory.getLogger(this::class.java.simpleName)

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val authHeader = request.getHeader("Authorization")


        if ((authHeader == null || !authHeader.startsWith("Bearer ")) && request.requestURI.contains("auth")) {
            filterChain.doFilter(request,response)
            return
        }
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(HttpStatus.BAD_REQUEST.value(), "Bad auth details")
            return
        }

        val jwt = authHeader.substring(7)
        val userId = jwtService.extractUsername(jwt);

        if (userId != null && SecurityContextHolder.getContext().authentication == null) {
            val userDetails = userDetailsService.loadUserByUsername(userId)
            if(jwtService.isTokenValid(jwt, userDetails as User)){
                val authToken = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authToken
            }
        }
        filterChain.doFilter(request, response)
    }
}
package com.example.library.auth.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.Instant
import java.util.*
import java.util.function.Function
import javax.crypto.SecretKey

@Component
class JwtUtils(
    private val jwtConfiguration: JwtConfiguration,
    private val clock: Clock,
) {

    fun extractUsername(token: String): String? {
        return extractClaim(token, Claims::getSubject)
    }

    fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return username == userDetails.username && !isTokenExpired(token)
    }

    fun generateToken(userDetails: UserDetails): String {
        return generateToken(HashMap(), userDetails)
    }

    private fun isTokenExpired(token: String): Boolean {
        val expirationTimestamp = extractExpiration(token).toInstant()
        val now = Instant.now(clock)
        return now.isAfter(expirationTimestamp)
    }

    private fun extractExpiration(token: String): Date {
        return extractClaim(token, Claims::getExpiration)
    }

    private fun generateToken(extraClaims: Map<String, Any>, userDetails: UserDetails): String {
        val now = Instant.now(clock)
        return Jwts.builder()
            .claims(extraClaims)
            .subject(userDetails.username)
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plus(jwtConfiguration.expiration)))
            .signWith(getSigningKey())
            .compact()
    }

    private fun <T> extractClaim(token: String, claimsResolver: Function<Claims, T>): T {
        val claims = extractAllClaims(token)
        return claimsResolver.apply(claims)
    }

    private fun extractAllClaims(token: String): Claims {
        val jwtClock: io.jsonwebtoken.Clock = io.jsonwebtoken.Clock { Date.from(clock.instant()) }
        return Jwts.parser()
            .clock(jwtClock)
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .payload
    }

    private fun getSigningKey(): SecretKey {
        val key = Decoders.BASE64.decode(jwtConfiguration.secretKey)
        return Keys.hmacShaKeyFor(key)
    }
}

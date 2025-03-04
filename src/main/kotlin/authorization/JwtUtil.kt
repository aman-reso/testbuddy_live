package authorization

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.ktor.server.application.*
import java.util.*


object JwtUtils {

    private const val jwtSecret = "testBuddyApp7564062907ManishSharmaDeveloperOpiniontestBuddyApp7564062907ManishSharmaDeveloperOpinion"
    private const val jwtExpirationMs = 86400000L * 90

    fun generateJwtToken(phoneNumber: String, userId: String): String {
        val claims: MutableMap<String, Any> = HashMap()
        claims["phoneNumber"] = phoneNumber
        claims["userId"] = userId
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(Date())
            .setExpiration(Date(Date().time + jwtExpirationMs))
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact()
    }

    private fun parseJwtToken(token: String?): String? {
        return if (token == null) {
            null
        } else try {
            val claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .body
            val phoneNumber = claims["phoneNumber"] as String?
            claims["userId"] as String?
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private fun extractTokenFromServlet(call: ApplicationCall): String? {
        return try {
            val authHeader = call.request.headers["Authorization"]
            authHeader?.removePrefix("Bearer ")
        } catch (e: Exception) {
            null
        }
    }

    fun getUserId(call: ApplicationCall): String? {
        return parseJwtToken(extractTokenFromServlet(call))
    }

}

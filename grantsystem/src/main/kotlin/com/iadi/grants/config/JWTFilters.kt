package com.iadi.grants.config


import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.iadi.grants.api.controller.ReviewerController
import com.iadi.grants.api.controller.StudentController
import com.iadi.grants.api.data.ReviewerDTO
import com.iadi.grants.api.data.StudentDTO
import com.iadi.grants.services.dao.UserDAO
import com.iadi.grants.services.services.CustomUserDetails
import com.iadi.grants.services.services.SecurityService
import com.iadi.grants.services.services.UserService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.web.filter.GenericFilterBean
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.collections.HashMap


object JWTSecret {
    private const val passphrase = "este ÃƒÂ© um grande segredo que tem que ser mantido escondido"
    val KEY: String = Base64.getEncoder().encodeToString(passphrase.toByteArray())
    const val SUBJECT = "JSON Web Token for CIAI 2019/20"
    const val VALIDITY = 1000 * 60 * 60 * 10 // 10 minutes in microseconds
}

private fun addResponseToken(authentication: Authentication, response: HttpServletResponse) {

    val claims = HashMap<String, Any?>()
    claims["username"] = authentication.name
    claims["roles"] = authentication.authorities

    val token = Jwts
            .builder()
            .setClaims(claims)
            .setSubject(JWTSecret.SUBJECT)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + JWTSecret.VALIDITY))
            .signWith(SignatureAlgorithm.HS256, JWTSecret.KEY)
            .compact()

    response.addHeader("Authorization", "Bearer $token")
}

class UserPasswordAuthenticationFilterToJWT(
        defaultFilterProcessesUrl: String?,
        private val anAuthenticationManager: AuthenticationManager,
        private val sec: SecurityService,
        private val users: UserService
) : AbstractAuthenticationProcessingFilter(defaultFilterProcessesUrl) {

    override fun attemptAuthentication(request: HttpServletRequest?,
                                       response: HttpServletResponse?): Authentication? {
        //getting user from request body
        val user = ObjectMapper().readValue(request!!.inputStream, UserDAO::class.java)

        // perform the "normal" authentication
        val auth = anAuthenticationManager.authenticate(UsernamePasswordAuthenticationToken(user.username,
                user.password,
                sec.getUserRoles(users.getAuths(user.username))))

        return if (auth.isAuthenticated) {
            // Proceed with an authenticated user
            SecurityContextHolder.getContext().authentication = auth
            auth
        } else
            null
    }

    override fun successfulAuthentication(request: HttpServletRequest,
                                          response: HttpServletResponse,
                                          filterChain: FilterChain?,
                                          auth: Authentication) {

        // When returning from the Filter loop, add the token to the response
        addResponseToken(auth, response)
        response.setHeader("Roles", auth.authorities.toString())
    }
}

class UserAuthToken(private var username: String, private var authorities: MutableCollection<out GrantedAuthority>) : Authentication {

    override fun getAuthorities() = authorities

    override fun setAuthenticated(isAuthenticated: Boolean) {}

    override fun getName() = username

    override fun getCredentials() = null

    override fun getPrincipal() = CustomUserDetails(username, "", authorities)

    override fun isAuthenticated() = true

    override fun getDetails() = username
}

class JWTAuthenticationFilter: GenericFilterBean() {

    // To try it out, go to https://jwt.io to generate custom tokens, in this case we only need a name...

    override fun doFilter(request: ServletRequest?,
                          response: ServletResponse?,
                          chain: FilterChain?) {

        val authHeader = (request as HttpServletRequest).getHeader("Authorization")

            if( authHeader != null && authHeader.startsWith("Bearer ") ) {
                val token = authHeader.substring(7) // Skip 7 characters for "Bearer "
                val claims = Jwts.parser().setSigningKey(JWTSecret.KEY).parseClaimsJws(token).body

                // should check for token validity here (e.g. expiration date, session in db, etc.)
                val exp = (claims["exp"] as Int).toLong()
                if ( exp < System.currentTimeMillis()/1000) // in seconds

                    (response as HttpServletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED) // RFC 6750 3.1

                else {
                    val authentication = UserAuthToken(claims["username"] as String, getAuthorities(claims))
                    // Can go to the database to get the actual user information (e.g. authorities)

                    SecurityContextHolder.getContext().authentication = authentication

                    // Renew token with extended time here. (before doFilter)
                    addResponseToken(authentication, response as HttpServletResponse)

                    chain!!.doFilter(request, response)
                }
            } else {
                (response as HttpServletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED)
                //chain!!.doFilter(request, response)

            }
    }
    private fun getAuthorities(map: Map<String, *>): MutableCollection<GrantedAuthority> {

        val authorities = map["roles"]
        val list = mutableListOf<GrantedAuthority>()
        return if (authorities is String) {
            AuthorityUtils.commaSeparatedStringToAuthorityList(authorities)
        } else if (authorities is MutableCollection<*>) {
            for(kv in authorities){
                val key = kv as LinkedHashMap<String, String>
                list.add(SimpleGrantedAuthority(key.get("authority")))
            }
            list
        } else {
            throw IllegalArgumentException("Authorities must be either a String or a Collection")
        }
    }
}

/**
 * Instructions:
 *
 * http POST :8080/login username=user password=password
 *
 * Observe in the response:
 *
 * HTTP/1.1 200
 * Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKU09OIFdlYiBUb2tlbiBmb3IgQ0lBSSAyMDE5LzIwIiwiZXhwIjoxNTcxNzc2MTM4LCJpYXQiOjE1NzE3NDAxMzgsInVzZXJuYW1lIjoidXNlciJ9.Mz18cn5xw-7rBXw8KwlWxUDSsfNCqlliiwoIpvYPDzk
 *
 * http :8080/pets Authorization:"Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKU09OIFdlYiBUb2tlbiBmb3IgQ0lBSSAyMDE5LzIwIiwiZXhwIjoxNTcxNzc2MTM4LCJpYXQiOjE1NzE3NDAxMzgsInVzZXJuYW1lIjoidXNlciJ9.Mz18cn5xw-7rBXw8KwlWxUDSsfNCqlliiwoIpvYPDzk"
 *
 */

class UserPasswordSignUpFilterToJWT(
        defaultFilterProcessesUrl: String?,
        private val users: UserService,
        private val sec: SecurityService,
        private val students: StudentController,
        private val reviewers: ReviewerController
) : AbstractAuthenticationProcessingFilter(defaultFilterProcessesUrl) {

    override fun attemptAuthentication(request: HttpServletRequest?,
                                       response: HttpServletResponse?): Authentication? {

        val mapper = ObjectMapper()
                .findAndRegisterModules()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        //getting user from request body
        val input = request!!.inputStream.readAllBytes()

        val user = try {
            mapper.readValue(input, StudentDTO::class.java)
        }catch (e: Exception){
            e.printStackTrace()
            mapper.readValue(input, ReviewerDTO::class.java)
        }

        if(user is StudentDTO)
            return students.registerStudent(user).let {
                val auth = UserAuthToken(user.username,
                        sec.getUserRoles(users.getAuths(user.username)))
                SecurityContextHolder.getContext().authentication = auth
                auth
            }
        else if(user is ReviewerDTO){
            return reviewers.createReviewer(user).let {
                val auth = UserAuthToken(user.username,
                        sec.getUserRoles(users.getAuths(user.username)))
                SecurityContextHolder.getContext().authentication = auth
                auth
            }
        }
        return null
    }

    override fun successfulAuthentication(request: HttpServletRequest,
                                          response: HttpServletResponse,
                                          filterChain: FilterChain?,
                                          auth: Authentication) {

        addResponseToken(auth, response)
    }
}


class AnonFilterToJWT (defaultFilterProcessesUrl: String?
) : AbstractAuthenticationProcessingFilter(defaultFilterProcessesUrl) {

    override fun attemptAuthentication(request: HttpServletRequest?,
                                       response: HttpServletResponse?): Authentication? {

        val auth = UserAuthToken("ANON",
                mutableListOf(SimpleGrantedAuthority("ROLE_"+"ANON")))
        SecurityContextHolder.getContext().authentication = auth
        return auth

    }

    override fun successfulAuthentication(request: HttpServletRequest,
                                          response: HttpServletResponse,
                                          filterChain: FilterChain?,
                                          auth: Authentication) {

        addResponseToken(auth, response)
    }
}
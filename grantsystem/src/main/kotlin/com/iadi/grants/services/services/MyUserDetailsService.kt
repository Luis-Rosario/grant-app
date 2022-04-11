package com.iadi.grants.services.services


import com.iadi.grants.model.UserRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

class CustomUserDetails(
        private val username: String,
        private val password: String,
        private val authorities: MutableCollection<out GrantedAuthority>) : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = authorities

    override fun isEnabled(): Boolean = true

    override fun getUsername(): String = username

    override fun isCredentialsNonExpired(): Boolean = true

    override fun getPassword(): String = password

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true
}

@Service
class MyUserDetailsService(var users: UserRepository, var sec: SecurityService): UserDetailsService {


    @Transactional
    override fun loadUserByUsername(username: String?): UserDetails? {
       // val user = users.findByUsername(username).orElseThrow{ throw UsernameNotFoundException("Username $username not found") }
        username?.let{
            val user= users.findByUsername(username)

           if(!user.isEmpty){
               val roles = sec.getUserRoles(user.get().roles)
               return CustomUserDetails(user.get().username, user.get().password,roles )}
            else{ throw UsernameNotFoundException("Username $username does not exist")}
        }
        throw UsernameNotFoundException("Username $username does not exist")
    }


}


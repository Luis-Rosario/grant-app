package com.iadi.grants.model


import com.iadi.grants.services.dao.RoleDAO
import com.iadi.grants.services.dao.UserDAO
import org.springframework.data.repository.CrudRepository
import org.springframework.data.jpa.repository.Query
import java.util.*


interface UserRepository: CrudRepository<UserDAO, Long>  {

    @Query("select user from UserDAO user where user.username = :username")
    fun findByUsername(username:String): Optional<UserDAO>

    fun existsByUsername(email:String):Boolean

    @Query("select user.roles from UserDAO user where user.username = :username")
    fun getRolesbyUsername(username: String):MutableList<RoleDAO>

}

package com.iadi.grants.services.services

import com.iadi.grants.model.RoleRepository
import com.iadi.grants.model.UserRepository
import com.iadi.grants.services.Exceptions.AlreadyExistsException
import com.iadi.grants.services.Exceptions.NotFoundException
import com.iadi.grants.services.dao.RoleDAO
import com.iadi.grants.services.dao.UserDAO
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Service
class UserService(val users: UserRepository,val roles: RoleRepository) {

    fun addUser(user: UserDAO) : Optional<UserDAO> {
        val aUser = users.findByUsername(user.username)

        return if ( aUser.isPresent )
            Optional.empty()
        else {

            var rolesList = mutableListOf<RoleDAO>()

            for(role in user.roles){
                val fRole = roles.findByRole(role.role)
                if(fRole.isPresent )
                    rolesList.add(fRole.get())
                else
                    throw NotFoundException("Role was not found")
            }
            user.roles=rolesList
            user.password = BCryptPasswordEncoder().encode(user.password)
            Optional.of(users.save(user))
        }
    }

    @Transactional
    fun getAuths(username:String):MutableList<RoleDAO>{
        return users.getRolesbyUsername(username)
    }
}
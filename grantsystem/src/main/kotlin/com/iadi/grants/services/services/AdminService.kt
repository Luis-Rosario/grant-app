package com.iadi.grants.services.services

import com.iadi.grants.model.RoleRepository
import com.iadi.grants.services.Exceptions.AlreadyExistsException
import com.iadi.grants.services.Exceptions.NotFoundException
import com.iadi.grants.services.dao.RoleDAO
import org.springframework.stereotype.Service

@Service("AdminService")
class AdminService(val roles: RoleRepository) {

    fun getRole(roleId: Long): RoleDAO {
        return roles.findById(roleId).orElseThrow {
            NotFoundException("Role with $roleId was not found")
        }
    }

    fun getAllRoles(): List<RoleDAO> {
        return roles.findAll().toList()
    }

    fun createRole(role: RoleDAO) {
        var name = role.role
        if(roles.existsByRole(role.role)){
            throw AlreadyExistsException("Role with name $name already exists")
        }
        roles.save(role)
    }

    fun updateRole(role: RoleDAO) {
        roles.save(role)
    }

    fun deleteRole(roleId: Long): RoleDAO {
        val rol: RoleDAO = roles.findById(roleId).orElseThrow { NotFoundException("Role with $roleId was not found") }
        roles.deleteById(roleId)
        return rol
    }
}
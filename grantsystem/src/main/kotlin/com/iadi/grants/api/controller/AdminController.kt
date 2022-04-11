package com.iadi.grants.api.controller

import com.iadi.grants.api.data.RoleDTO
import com.iadi.grants.services.dao.RoleDAO
import com.iadi.grants.services.services.AdminService
import com.iadi.grants.services.services.UserService
import org.springframework.web.bind.annotation.RestController

@RestController
class AdminController(var serv: AdminService, var users:UserService) : AdminAPI {

    override fun getRole(roleId: Long): RoleDTO {
        return serv.getRole(roleId).let { RoleDTO(it.id, it.role) }
    }

    override fun getUserRoles(userName:String):List<RoleDTO>{
        return users.getAuths(userName).map { RoleDTO(it.id,it.role) }
    }

    override fun getAllRoles(): List<RoleDTO> {
        return serv.getAllRoles().map { RoleDTO(it.id, it.role) }
    }

    override fun createRole(role: RoleDTO) {
        serv.createRole(role.let {
            RoleDAO(role.id, role.role)
        })
    }

    override fun updateRole(role: RoleDTO) {
        serv.updateRole(role.let {
            RoleDAO(role.id, role.role)
        })
    }

    override fun deleteRole(role: Long): RoleDTO {
      return  serv.deleteRole(role).let {
            RoleDTO(it.id, it.role)
        }
    }
}
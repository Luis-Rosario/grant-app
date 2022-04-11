package com.iadi.grants.model

import com.iadi.grants.services.dao.RoleDAO
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.*

interface RoleRepository: CrudRepository<RoleDAO, Long> {

    fun findByRole(role:String):Optional<RoleDAO>

    fun existsByRole(role:String):Boolean


}
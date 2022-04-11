package com.iadi.grants.model


import com.iadi.grants.services.dao.InstitutionDAO
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.*

interface InstitutionRepository: CrudRepository<InstitutionDAO, Long> {

    fun findByName(name:String):Optional<InstitutionDAO>

    fun existsByNameAndAndEmailAndPhoneNumb(name: String,email:String,phoneNumb:String):Boolean


}
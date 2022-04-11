package com.iadi.grants.model

import com.iadi.grants.services.dao.ReviewerDAO
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.*

interface ReviewerRepository : CrudRepository<ReviewerDAO, Long> {

    @Query("select rev from ReviewerDAO rev where rev.institution.id =:id")
    fun findByInstitutionId(id:Long): MutableIterable<ReviewerDAO>


    @Query("select rev from ReviewerDAO rev where rev.email =: email")
    fun findByEmail(email:String): MutableIterable<ReviewerDAO>

    fun findByNameContaining(name:String):Optional<List<ReviewerDAO>>

    fun findByEmailContaining(name: String): Optional<List<ReviewerDAO>>

    fun findByUsernameContaining(username:String): Optional<List<ReviewerDAO>>

    fun findByNameContainingAndEmailContaining(name: String,email: String): Optional<List<ReviewerDAO>>

    fun findByUsername(username:String):Optional<ReviewerDAO>


}
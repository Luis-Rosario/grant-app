package com.iadi.grants.model

import com.iadi.grants.services.dao.SponsorDAO
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.*

interface SponsorRepository: CrudRepository<SponsorDAO, Long> {


    @Query("select sponsor from SponsorDAO sponsor where sponsor.email =: email")
    fun findByEmail(email:String): MutableIterable<SponsorDAO>

    fun existsByName(name:String):Boolean

    fun findByUsernameContaining(username: String): Optional<List<SponsorDAO>>



}
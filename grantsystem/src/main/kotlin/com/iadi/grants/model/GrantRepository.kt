package com.iadi.grants.model

import com.iadi.grants.services.dao.GrantDAO

import java.util.*
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.time.LocalDate

interface GrantRepository: CrudRepository<GrantDAO,Long> {


    fun findByDeadlineBefore(date: LocalDate): MutableIterable<GrantDAO>

    fun findByDeadlineAfter(date: LocalDate): MutableIterable<GrantDAO>

    fun findByTitleContaining(title: String): Optional<List<GrantDAO>>

    @Query("select grant from GrantDAO grant inner join fetch grant.sponsor where grant.sponsor.id = :id")
    fun findBySponsorId(id: Long): List<GrantDAO>

    fun existsByTitleAndDescription(title: String,description:String):Boolean



}

package com.iadi.grants.model

import com.iadi.grants.services.dao.EvaluationDAO
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.*

interface EvaluationRepository: CrudRepository<EvaluationDAO, Long>{

    @Query("select eval from EvaluationDAO eval inner join fetch eval.rev where eval.rev.id = :id")
    fun findByReviewerId(id:Long):MutableIterable<EvaluationDAO>

    fun findByAppApplicationID(id: Long):Optional<List<EvaluationDAO>>

    fun findByGrantGrantId(id:Long):Optional<List<EvaluationDAO>>

    fun existsByAppApplicationIDAndRevId(appId:Long,revId:Long): Boolean

    fun findByAppApplicationIDAndGrantGrantId(id: Long,grantId:Long):Optional<List<EvaluationDAO>>



}
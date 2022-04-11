package com.iadi.grants.model


import com.iadi.grants.services.dao.PanelDAO
import com.iadi.grants.services.dao.ReviewerDAO
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.*

interface PanelRepository: CrudRepository<PanelDAO, Long> {

    @Query("select panel from PanelDAO panel inner join fetch panel.grant where panel.grant.grantId = :id")
    fun findByGrantId(id:Long): Optional<PanelDAO>

    fun existsByGrantGrantId(id:Long):Boolean

    fun findByGrantGrantId(id:Long):Optional<PanelDAO>

    @Query("select panel from PanelDAO panel where :rev member of panel.reviewers")
    fun findByReviewerId(rev:ReviewerDAO):MutableIterable<PanelDAO>

    @Query("select panel from PanelDAO panel where :rev member of panel.reviewers and panel.grant.grantId = :grantId")
    fun findByReviewerAndGrantGrantId(rev:ReviewerDAO,grantId:Long):Optional<PanelDAO>

}
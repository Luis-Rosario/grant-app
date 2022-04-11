package com.iadi.grants.services.services

import com.iadi.grants.model.GrantRepository
import javassist.NotFoundException


import com.iadi.grants.model.PanelRepository
import com.iadi.grants.model.ReviewerRepository
import com.iadi.grants.services.Exceptions.AlreadyExistsException
import com.iadi.grants.services.Exceptions.InvalidPanelException
import com.iadi.grants.services.dao.PanelDAO

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class PanelService(val panels: PanelRepository, val grants: GrantRepository, val reviewers: ReviewerRepository)
{
    @Transactional
    fun createPanel(panel: PanelDAO) {
        if(panels.existsByGrantGrantId(panel.grant.grantId))
            throw AlreadyExistsException("Panel for that grant already exists")

        panel.panelID = 0

        verifyPanel(panel)

        panels.save(panel)
    }


    fun getPanels(): List<PanelDAO> {
       return panels.findAll().toList()
    }


    fun getPanel(id: Long): PanelDAO {
        return panels.findById(id).orElseThrow{
            NotFoundException("Panel with $id was not found")
        }
    }

    @Transactional
    fun updatePanel(id: Long, panel: PanelDAO) {
        verifyPanel(panel)
       panels.save(panel)
    }

    @Transactional
    fun deletePanel(id: Long): PanelDAO {
        val panel: PanelDAO = getPanel(id)
        panels.delete(panel)
        return panel
    }

   private fun chairmanBelongsToReviewers(panel: PanelDAO):Boolean{
       val chairman = panel.panelChair
       val it = panel.reviewers.iterator()
       while(it.hasNext()){
           val rev = it.next()
            if(rev.id == chairman.id)
                return true
        }
        return false
    }


    private fun verifyPanel(panel: PanelDAO){
        val grantID = panel.grant.grantId
        val panelChairID = panel.panelChair.id

        val chairId = panel.panelID

        grants.findById(grantID).orElseThrow{ NotFoundException("Grant with $grantID id was not found")}
        reviewers.findById(panelChairID).orElseThrow{ NotFoundException("Reviewer with $chairId id was not found")}

        val it = panel.reviewers.iterator()
        while(it.hasNext()){
            val rev = it.next()
            val revID= rev.id
            reviewers.findById(revID).orElseThrow{ NotFoundException("Reviewer with $revID id was not found")}
        }

       if(!chairmanBelongsToReviewers(panel)){ throw InvalidPanelException() }
    }


}
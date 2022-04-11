package com.iadi.grants.services.services

import com.iadi.grants.api.data.Status
import com.iadi.grants.model.*
import com.iadi.grants.services.Exceptions.AlreadyExistsException
import com.iadi.grants.services.Exceptions.NotAllowedException
import com.iadi.grants.services.dao.ApplicationDAO
import com.iadi.grants.services.dao.GrantDAO
import com.iadi.grants.services.dao.PanelDAO
import com.iadi.grants.services.Exceptions.NotFoundException
import com.iadi.grants.services.dao.EvaluationDAO
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Service
class GrantService(val grantsRep: GrantRepository,
                   val applications: ApplicationRepository,
                   val panels: PanelRepository,
                   val sponsors: SponsorRepository,
                   val evals: EvaluationRepository,
                   val revs: ReviewerService,
                   val panelService: PanelService) {


    fun getGrants(name: Optional<String>): List<GrantDAO> {
        if (name.isPresent) {
            return grantsRep.findByTitleContaining(name.get()).orElseThrow {
                NotFoundException("Grant with that keyword was not Found")
            }
        } else
            return grantsRep.findAll().toList()
    }


    fun getGrant(id: Long): GrantDAO = grantsRep.findById(id).orElseThrow {
        NotFoundException("Grant with $id was not found")
    }

    @Transactional
    fun addGrant(grant: GrantDAO) {
        val sponsorId = grant.sponsor.id
        var sponsor = sponsors.findById(sponsorId).orElseThrow {
            NotFoundException("Sponsor with $sponsorId was not found")
        }
        grant.sponsor = sponsor
        grant.grantId = 0
        if (grantsRep.existsByTitleAndDescription(grant.title, grant.description)) {
            throw AlreadyExistsException("Grant Already Exists")
        }

        var listRevs = revs.getReviewers(Optional.empty(), Optional.empty(), Optional.empty());

        if (listRevs.size<4) {
            throw NotAllowedException("Not enough Reviewers to configure a Panel")
        }

        listRevs = listRevs.shuffled()

        var panel = PanelDAO()

        panel.grant=grant

        panel.panelChair=listRevs[0]

        for (i in 0..3){
            panel.reviewers.add(listRevs[i])
        }

        grantsRep.save(grant)

        panelService.createPanel(panel)


    }


    fun getGrantApplications(id: Long,status:Optional<Status>): List<ApplicationDAO> {
        notFoundGrant(id)
        if(status.isPresent){
         return applications.findByGrantIdAndStatus(id, status.get()).toList()
        }
        return applications.findByGrantIdNotDrafts(id).toList()
    }


    fun getPanel(id: Long): PanelDAO {
        notFoundGrant(id)
        return panels.findByGrantId(id).get()
    }

    @Transactional
    fun updateGrant(grant: GrantDAO) {
        notFoundGrant(grant.grantId)
        val sponsorId = grant.sponsor.id
        var sponsor = sponsors.findById(sponsorId).orElseThrow {
            NotFoundException("Sponsor with $sponsorId was not found")
        }
        grant.sponsor = sponsor
        grantsRep.save(grant)
    }

    @Transactional
    fun deleteGrant(id: Long): GrantDAO {
        val grant = grantsRep.findById(id).orElseThrow {
            NotFoundException("Grant with $id was not found")
        }
        grantsRep.deleteById(id)
        return grant
    }

    fun notFoundGrant(id: Long) {
        if (!grantsRep.existsById(id)) {
            throw NotFoundException("Grant with $id id not found")
        }
    }

    fun getGrantEvaluations(grantId: Long): List<EvaluationDAO> {
        return evals.findByGrantGrantId(grantId).orElseThrow {
            NotFoundException("Evals of Grant $grantId were not found")
        }
    }

}
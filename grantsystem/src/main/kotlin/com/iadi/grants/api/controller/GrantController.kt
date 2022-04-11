package com.iadi.grants.api.controller

import com.iadi.grants.api.data.*
import com.iadi.grants.services.dao.GrantDAO
import com.iadi.grants.services.dao.GrantQuestionDAO
import com.iadi.grants.services.dao.SponsorDAO
import com.iadi.grants.services.services.GrantService
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.persistence.Tuple


@RestController
class GrantController(val service: GrantService):GrantAPI{



   @ResponseStatus(HttpStatus.CREATED)
   @PreAuthorize("hasAnyRole('ADMIN', 'SPONSOR')")
   override fun createGrant(grant:GrantDTO ) {
      service.addGrant( GrantDAO(
              grant.grantId,
              grant.title,
              grant.openingDate,
              grant.deadline,
              grant.description,
              grant.funding,
              grant.applicationQuestions.map {
                 GrantQuestionDAO(it.fieldDescription,it.type,it.mandatory)
              }, mutableListOf(), SponsorDAO(grant.sponsorId,grant.sponsorName)) )
   }



   @ResponseStatus(HttpStatus.OK)
   override fun getGrants(name: Optional<String>): List<GrantDTO> {
     return service.getGrants(name).map { GrantDTO(
             it.grantId,
             it.title,
             it.openingDate,
             it.deadline,
             it.description,
             it.funding,
             it.applicationQuestions.map {
                GrantQuestion(
                        it.fieldDescription,
                        it.type,
                        it.mandatory) },
             it.applications.size,it.sponsor.id,it.sponsor.name)  }
   }


   @PreAuthorize("hasAnyRole('ADMIN','STUDENT','REVIEWER','CHAIRMAN','SPONSOR')")
   @ResponseStatus(HttpStatus.OK)
   override fun getGrant(id:Long): GrantDTO {
      return service.getGrant(id).let{
         GrantDTO(
                 it.grantId,
                 it.title,
                 it.openingDate,
                 it.deadline,
                 it.description,
                 it.funding,
                 it.applicationQuestions.map {
                    GrantQuestion(
                            it.fieldDescription,
                            it.type,
                            it.mandatory) },
                 it.applications.size,it.sponsor.id,it.sponsor.name) }
   }


    @PreAuthorize( "@SecurityService.canGetApplicationsGrant(principal, #id,#status)")  //desde que seja o grant do sponsor
   @ResponseStatus(HttpStatus.OK)
   override fun getGrantApplications(id: Long,status: Optional<Status>): List<ApplicationSafeDTO> {
     return service.getGrantApplications(id,status).map {ApplicationSafeDTO(it.applicationID,
             it.submissionDate,
             it.status,
             it.grant.grantId,
             it.student.id,
             it.reviews.map {EvaluationDTO(it.evalId,
                     it.status,
                     it.rev.let { it.id },
                     it.app.let { it.applicationID },
                     it.grant.let { it.grantId },it.textField) }) }

     }


    @PreAuthorize("hasAnyRole('ADMIN','REVIEWER','CHAIRMAN','SPONSOR')")
   @ResponseStatus(HttpStatus.OK)
   override fun getPanel(id: Long): PanelDTO {
      return service.getPanel(id).let { PanelDTO(it.panelID,
              it.panelChair.id,
              it.reviewers.map { IdAndName(it.id,it.name)},
              it.grant.grantId) }
   }



    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN','SPONSOR')")
   override fun updateGrant(grant: GrantDTO) {
        service.updateGrant(grant.let {
            GrantDAO(it.grantId,
                    it.title,
                    it.openingDate,
                    it.deadline,
                    it.description,
                    it.funding,
                    it.applicationQuestions.map { GrantQuestionDAO(
                            it.fieldDescription,
                            it.type,
                            it.mandatory) }, mutableListOf(), SponsorDAO(grant.sponsorId)) })
   }


    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN','SPONSOR')")
   override fun deleteGrant(id: Long): GrantDTO {
      return service.deleteGrant(id).let {
          GrantDTO(it.grantId,
                  it.title,
                  it.openingDate,
                  it.deadline,
                  it.description,
                  it.funding,
                  it.applicationQuestions.map {
                      GrantQuestion(it.fieldDescription,
                              it.type,
                              it.mandatory) },
                  it.applications.size,it.sponsor.id,it.sponsor.name) }
   }


    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole( 'ADMIN') or @SecurityService.belongsToPanel(principal, #grantId)")
    override fun getGrantEvaluations(grantId: Long): List<EvaluationDTO> {
        return service.getGrantEvaluations(grantId).map{
            EvaluationDTO(
                    it.evalId,
                    it.status,
                    it.rev.id,
                    it.app.applicationID,
                    it.grant.grantId,
                    it.textField
            )
        }
    }

}
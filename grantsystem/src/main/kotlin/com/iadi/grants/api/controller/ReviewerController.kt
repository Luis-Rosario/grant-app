package com.iadi.grants.api.controller

import com.iadi.grants.api.data.*
import com.iadi.grants.services.dao.InstitutionDAO
import com.iadi.grants.services.dao.ReviewerDAO
import com.iadi.grants.services.dao.RoleDAO
import com.iadi.grants.services.services.ReviewerService
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class ReviewerController(val service: ReviewerService):ReviewerAPI {

    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    override fun createReviewer(reviewer: ReviewerDTO) {
        service.addReviewer(ReviewerDAO(
                reviewer.id,
                reviewer.address,
                reviewer.birthDate,
                InstitutionDAO(reviewer.institutionId),
                reviewer.name,
                reviewer.email,
                mutableListOf(),
               reviewer.username,
                reviewer.password,
                reviewer.roles.map{ RoleDAO(it) }.toMutableList()


        ))
    }


    @PreAuthorize("hasRole('ADMIN') or @SecurityService.canGetReviewer(principal, #username)")
    @ResponseStatus(HttpStatus.OK)
    override fun getReviewers(name: Optional<String>, email: Optional<String>, username: Optional<String>): List<SafeReviewerDTO> {
        return service.getReviewers(name,email,username).map{
            SafeReviewerDTO(
                it.id,
                it.address,
                it.birthDate,
                it.institution.id,
                it.name,
                it.email,
                it.evals.map { it.evalId },
                    it.username)}
    }



    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    override fun getReviewer(id: Long): ReviewerDTO {
        return service.getReviewer(id).let {
            ReviewerDTO(
                    it.id,
                    it.address,
                    it.birthDate,
                    it.institution.id,
                    it.name,
                    it.email,
                    it.evals.map { it.evalId },
                    it.username,
                    it.password,
                    it.roles.map{ it.role }.toMutableList())}
    }


    @PreAuthorize("hasAnyRole('ADMIN') or @SecurityService.canEditReviewer(principal, #id)")
    @ResponseStatus(HttpStatus.OK)
    override fun getReviewerEvaluations(id: Long): List<EvaluationDTO> {
        return service.getReviewerEvals(id).map { EvaluationDTO(
                it.evalId,
                it.status,
                it.rev.id,it.app.applicationID,it.grant.grantId,it.textField)}
    }

    @PreAuthorize("hasAnyRole('ADMIN') or @SecurityService.canEditReviewer(principal, #id)")
    @ResponseStatus(HttpStatus.OK)
    override fun getReviewerPanels(id: Long): List<PanelDTO> {
        return service.getReviewerPanels(id).map { PanelDTO(
                it.panelID,
                it.panelChair.id,
                it.reviewers.map { IdAndName(it.id,it.name) },
                it.grant.grantId
        )}
    }


    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN') or @SecurityService.canEditReviewer(principal, #reviewer)")
    override fun updateReviewer(reviewer: SafeReviewerDTO) {
        service.updateReviewer(reviewer.let {
            ReviewerDAO(it.id,
                    it.address,
                    it.birthDate,
                    InstitutionDAO(),
                    it.name,
                    it.email,
                    mutableListOf(),
                    it.username,
                    "",
                    mutableListOf())
        })
    }



    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or @SecurityService.canEditReviewer(principal, #id)")
    override fun deleteReviewer(id: Long): ReviewerDTO {
        return service.deleteReviewer(id).let {
            ReviewerDTO(it.id,
                        it.address,
                        it.birthDate,
                        it.institution.id,
                        it.name,
                        it.email,
                        it.evals.map{ it.evalId},
                        it.username,
                        it.password,
                    it.roles.map{ it.role }.toMutableList()
                    )

        }
    }

}
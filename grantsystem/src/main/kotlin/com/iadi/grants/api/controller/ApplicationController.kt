package com.iadi.grants.api.controller

import com.iadi.grants.api.data.*
import com.iadi.grants.services.dao.*
import com.iadi.grants.services.services.ApplicationService
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class ApplicationController(val service: ApplicationService):ApplicationAPI {


    @PreAuthorize("hasRole('ADMIN') or @SecurityService.canGetApplication(principal,#appId)")
    @ResponseStatus(HttpStatus.OK)
    override fun getApplication(appId: Long): ApplicationDTO {
        return service.getApplication(appId).let { ApplicationDTO(it.applicationID,
                it.submissionDate,
                it.status,
                it.responses.map {
                    GrantResponse(it.question.let { GrantQuestion(it.fieldDescription,
                            it.type,
                            it.mandatory) }
                            , it.response) },
                it.grant.grantId,
                it.student.id,
                it.reviews.map {EvaluationDTO(it.evalId,
                        it.status,
                        it.rev.let { it.id },
                        appId,
                        it.grant.let { it.grantId },it.textField) }) }
    }


    @PreAuthorize("hasRole('ADMIN' )")
    @ResponseStatus(HttpStatus.OK)
    override fun getAllApplication(): List<ApplicationDTO> {
        return service.getAllApplications().map { ApplicationDTO(it.applicationID,
                it.submissionDate,
                it.status,
                it.responses.map { GrantResponse(it.question.let { GrantQuestion(it.fieldDescription,
                        it.type,
                        it.mandatory) }
                        , it.response) },
                it.grant.grantId,
                it.student.id,
                it.reviews.map {EvaluationDTO(it.evalId,
                        it.status,
                        it.rev.let { it.id },
                        it.app.let { it.applicationID },
                        it.grant.let { it.grantId },it.textField) }) }
    }



    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN') or @SecurityService.canPostApplication(principal,#app.studentID)")
    override fun createApplication(app: ApplicationDTO) {
        return service.addApplication(app.let { ApplicationDAO(it.applicationID,
                it.submissionDate,
                it.status,
                it.responses.map {
                    GrantResponseDAO(GrantQuestionDAO(),it.response) },
                GrantDAO(),
                StudentDAO(),
                mutableListOf())},app.studentID,app.grantID)
    }


    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN') or @SecurityService.canEditApplication(principal,#app.applicationID,#app.status)")
    override fun updateApplication(app: ApplicationDTO) {
        service.updateApplication(app.let { ApplicationDAO(it.applicationID,
                it.submissionDate,
                it.status,
                it.responses.map {
                    GrantResponseDAO(GrantQuestionDAO(),it.response)},
                GrantDAO(),
                StudentDAO(), mutableListOf())
        } )
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN') or @SecurityService.canDeleteApplication(principal,#appId)")
    override fun deleteApplication(appId: Long): ApplicationDTO {
        return service.deleteApplication(appId).let { ApplicationDTO(it.applicationID,
                it.submissionDate,
                it.status,
                it.responses.map { GrantResponse(it.question.let { GrantQuestion(it.fieldDescription,
                        it.type,
                        it.mandatory) }
                        , it.response) },
                it.grant.grantId,
                it.student.id,
                it.reviews.map {EvaluationDTO(it.evalId,
                        it.status,
                        it.rev.let { it.id },
                        it.app.let { it.applicationID },
                        it.grant.let { it.grantId },it.textField) }) }
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole( 'ADMIN') or @SecurityService.belongsToPanelFromApplication(principal, #appId) or @SecurityService.canGetApplicationEvals(principal, #appId)")
    override fun getApplicationEvaluations(appId:Long): List<EvaluationDTO> {
        return service.getApplicationEvaluations(appId).map{
                EvaluationDTO(
                        it.evalId,
                        it.status,
                        it.rev.id,
                        it.app.applicationID,
                        it.grant.grantId,
                        it.textField)
        }
    }
}
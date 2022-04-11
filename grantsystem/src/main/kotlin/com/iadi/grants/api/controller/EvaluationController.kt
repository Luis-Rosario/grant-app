package com.iadi.grants.api.controller

import com.iadi.grants.api.data.EvaluationDTO
import com.iadi.grants.services.dao.ApplicationDAO
import com.iadi.grants.services.dao.EvaluationDAO
import com.iadi.grants.services.dao.GrantDAO
import com.iadi.grants.services.dao.ReviewerDAO
import com.iadi.grants.services.services.EvaluationService
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class EvaluationController(val service: EvaluationService) : EvaluationAPI {


    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN') or @SecurityService.belongsToPanel(principal, #evaluation.grantId)")
    override fun createEvaluation(evaluation: EvaluationDTO) {
        service.addEvaluation(EvaluationDAO(
                evaluation.id,
                evaluation.status,
                ReviewerDAO(evaluation.revId),
                ApplicationDAO(evaluation.applicationId),
                GrantDAO(evaluation.grantId),evaluation.textField
        ))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    override fun getAllEvaluations(grantId: Optional<Long>, applicationId: Optional<Long>): List<EvaluationDTO> {
        return service.getAllEvals(grantId, applicationId).map {
            EvaluationDTO(
                    it.evalId,
                    it.status,
                    it.rev.id,
                    it.app.applicationID,
                    it.grant.grantId,
                    it.textField)
        }
    }


    @PreAuthorize("hasRole('ADMIN') or" +
            " @SecurityService.belongsToPanel(principal, #eval.revId, #eval.grantId) or" +
            " @SecurityService.isApplicationOwner(principal, #id)")
    @ResponseStatus(HttpStatus.OK)
    override fun getEvaluation(id: Long): EvaluationDTO {
        return service.getEval(id).let {
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

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or @SecurityService.belongsToPanel(principal, #eval.grantId)")
    override fun updateEvaluation(evaluation: EvaluationDTO) {
        service.updateEval(evaluation.let {
            EvaluationDAO(
                    it.id,
                    it.status,
                    ReviewerDAO(evaluation.revId),
                    ApplicationDAO(evaluation.applicationId),
                    GrantDAO(evaluation.grantId),
                    it.textField
            )
        })

    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN') or @SecurityService.isPanelChairmanOfEval(principal, #id)")
    override fun deleteEvaluation(id: Long): EvaluationDTO {
        return service.deleteEval(id).let {
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
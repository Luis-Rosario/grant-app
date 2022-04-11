package com.iadi.grants.api.controller

import com.iadi.grants.api.data.IdAndName
import com.iadi.grants.api.data.PanelDTO
import com.iadi.grants.services.dao.GrantDAO
import com.iadi.grants.services.dao.PanelDAO
import com.iadi.grants.services.dao.ReviewerDAO
import com.iadi.grants.services.services.PanelService
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class PanelController(val service: PanelService) : PanelAPI {

    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    override fun createPanel(panel: PanelDTO) {
        service.createPanel(PanelDAO(
                panel.panelID,
                ReviewerDAO(panel.panelChair),
                panel.reviewer.map { ReviewerDAO(it.id,it.name) }.toMutableList(),
                GrantDAO(panel.grant)
        ))
    }


    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    override fun getPanels(): List<PanelDTO> {
        return service.getPanels().map {
            PanelDTO(
                    it.panelID,
                    it.panelChair.id,
                    it.reviewers.map { IdAndName(it.id,it.name) },
                    it.grant.grantId
            )
        }
    }


    @PreAuthorize("hasRole('ADMIN' or @SecurityService.belongsToSponsorGrant(principal, #id))")
    @ResponseStatus(HttpStatus.OK)
    override fun getPanel(id: Long): PanelDTO {
        return service.getPanel(id).let {
            PanelDTO(
                    it.panelID,
                    it.panelChair.id,
                    it.reviewers.map { IdAndName(it.id,it.name) },
                    it.grant.grantId
            )
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or @SecurityService.isPanelChairman(principal, #id)")
    override fun updatePanel(id: Long, panel: PanelDTO) {
        service.updatePanel(id, panel.let {
            PanelDAO(
                    panel.panelID,
                    ReviewerDAO(),
                    mutableListOf(),
                    GrantDAO()
            )
        })
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    override fun deletePanel(id: Long): PanelDTO {
        return service.deletePanel(id).let {
            PanelDTO(
                    it.panelID,
                    it.panelChair.id ,
                    it.reviewers.map { IdAndName(it.id,it.name) },
                    it.grant.grantId
            )
        }
    }
}
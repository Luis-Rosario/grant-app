package com.iadi.grants.api.controller

import com.iadi.grants.api.data.*
import com.iadi.grants.services.dao.RoleDAO
import com.iadi.grants.services.dao.SponsorDAO
import com.iadi.grants.services.services.SponsorService
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class SponsorController(val service: SponsorService) : SponsorAPI {

    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    override fun createSponsor(sponsor: SponsorDTO) {
        service.createSponsor(sponsor.let {
            SponsorDAO(
                    it.sponsorID,
                    it.name,
                    it.email,
                    it.phoneNumber,
                    it.username,
                    it.password,
                    it.roles.map{ RoleDAO(it) }.toMutableList()
            )
        })
    }


    //@PreAuthorize("hasAnyRole('STUDENT', 'CHAIRMAN', 'ADMIN', 'SPONSOR', 'REVIEWER' or @SecurityService.canGetSponsor(principal, #username)")
    @ResponseStatus(HttpStatus.OK)
    override fun getSponsors(username: Optional<String>): List<SafeSponsorDTO> {
        return service.getSponsors(username).map {
            SafeSponsorDTO(it.id,
                    it.name,
                    it.email,
                    it.phoneNumber,
                    it.username
            )
        }
    }


    @PreAuthorize("hasRole('ADMIN') or @SecurityService.isLoggedSponsor(principal,#id)")
    @ResponseStatus(HttpStatus.OK)
    override fun getSponsor(id: Long): SponsorDTO {
        return service.getSponsor(id).let {
            SponsorDTO(it.id,
                    it.name,
                    it.email,
                    it.phoneNumber,
                    it.username,
                    it.password,
                    it.roles.map{ it.role }.toMutableList()
                    )
        }
    }


    @PreAuthorize("hasAnyRole('STUDENT', 'CHAIRMAN', 'ADMIN', 'SPONSOR', 'Reviewer')")
    @ResponseStatus(HttpStatus.OK)
    override fun getGrantSponsor(id: Long): List<SafeGrantDTO> {
        return service.getGrantsFromSponsor(id).map {
            SafeGrantDTO(
                    it.grantId,
                    it.title,
                    it.deadline,
                    it.openingDate,
                    it.description,
                    it.funding,
                    it.applicationQuestions.map {
                        GrantQuestion(
                                it.fieldDescription,
                                it.type,
                                it.mandatory)
                    },
                    it.sponsor.id)
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN') or @SecurityService.canEditSponsor(principal,#sponsor)")
    override fun updateSponsor(sponsor: SafeSponsorDTO) {
        return service.updateSponsor(sponsor.let {
            SponsorDAO(it.sponsorID,
                    it.name,
                    it.email,
                    it.phoneNumber,
                    it.username,
                    "",
                    mutableListOf()
                   )
        })
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or @SecurityService.isLoggedSponsor(principal,#id)")
    override fun deleteSponsor(id: Long): SponsorDTO {
        return service.deleteSponsor(id).let {
            SponsorDTO(it.id,
                    it.name,
                    it.email,
                    it.phoneNumber,
                    it.username,
                    it.password,
                    it.roles.map{ it.role }.toMutableList()
                   )
        }
    }


}
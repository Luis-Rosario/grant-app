package com.iadi.grants.api.controller

import com.iadi.grants.api.data.*
import com.iadi.grants.services.dao.InstitutionDAO
import com.iadi.grants.services.services.InstitutionService
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class InstitutionController(val service: InstitutionService):InstitutionAPI {

    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    override fun registerInstitution(institution: InstitutionDTO) {
        service.addInstitution(InstitutionDAO(
                institution.id,
                institution.name,
                institution.email,
                institution.phoneNumb,
                mutableListOf(),
                mutableListOf()
        ))
    }


   // @PreAuthorize("hasAnyRole('STUDENT', 'CHAIRMAN', 'ADMIN', 'SPONSOR', 'Reviewer')")
    @ResponseStatus(HttpStatus.OK)
    override fun getInstitutions(): List<InstitutionDTO> {
       return service.getInstitutions().map{
           InstitutionDTO(
                   it.id,
                   it.name,
                   it.email,
                   it.phoneNumb//,
             //      it.students.map{it.id},
             //      it.reviewers.map{it.id}
           )
       }
    }


   // @PreAuthorize("hasAnyRole('STUDENT', 'CHAIRMAN', 'ADMIN', 'SPONSOR', 'Reviewer')")
    @ResponseStatus(HttpStatus.OK)
    override fun getInstitution(id: Long): InstitutionDTO {
        return service.getInstitution(id).let{
            InstitutionDTO(
                    it.id,
                    it.name,
                    it.email,
                    it.phoneNumb//,
                 //   it.students.map{it.id},
               //     it.reviewers.map{it.id}
            )
        }
    }


    @PreAuthorize("hasAnyRole('STUDENT', 'CHAIRMAN', 'REVIEWER','ADMIN', 'SPONSOR')")
    @ResponseStatus(HttpStatus.OK)
    override fun getInstitutionStudents(id: Long):List<SafeStudentDTO> {
        return service.getInstitutionStudents(id).map{
            SafeStudentDTO(
                    it.id,
                    it.address,
                    it.birthDate,
                    it.institution.id,
                    it.name,
                    it.email,
                    it.course,
                    it.cv.id,
                    it.applications.map { it.applicationID },
                            it.username

            )
        }
    }


    @PreAuthorize("hasAnyRole('CHAIRMAN', 'REVIEWER','ADMIN', 'SPONSOR')")
    @ResponseStatus(HttpStatus.OK)
    override fun getInstitutionReviewers(id: Long):List<SafeReviewerDTO> {
        return service.getInstitutionReviewers(id).map{
            SafeReviewerDTO(
                    it.id,
                    it.address,
                    it.birthDate,
                    it.institution.id,
                    it.name,
                    it.email,
                    it.evals.map{it.evalId},
                    it.username

            )
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    override fun updateInstitution(institution: InstitutionDTO) {
       service.updateInstitution(institution.let {
           InstitutionDAO(
                   institution.id,
                   institution.name,
                   institution.email,
                   institution.phoneNumb,
                   mutableListOf(),
                   mutableListOf()
           )
       })
    }


    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    override fun deleteInstitution(id: Long): InstitutionDTO {
        return service.deleteInstitution(id).let{
            InstitutionDTO(
                    it.id,
                    it.name,
                    it.email,
                    it.phoneNumb//,
                 //   it.students.map{it.id},
                 //   it.reviewers.map{it.id}
            )
        }
    }

}
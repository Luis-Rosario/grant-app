package com.iadi.grants.api.controller

import com.iadi.grants.api.data.*
import com.iadi.grants.services.dao.*
import com.iadi.grants.services.services.StudentService
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize

import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class StudentController(val service: StudentService) : StudentAPI {

    //@PreAuthorize("hasRole('ROLE_ADMIN') or @SecurityService.canEditStudent(principal,#id)")
    @ResponseStatus(HttpStatus.CREATED)
    override fun registerStudent(student: StudentDTO) {
        service.registerStudent(StudentDAO(
                student.id,
                student.address,
                student.birthDate,
                InstitutionDAO(student.institutionId),
                student.name,
                student.email,
                student.course,
                CvDAO(0),
                mutableListOf(),
                student.username,
                student.password,
                student.roles.map{RoleDAO(it)}.toMutableList()
        ))
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'SPONSOR', 'REVIEWER', 'SPONSOR') or @SecurityService.canGetStudent(principal, #username)")
    @ResponseStatus(HttpStatus.OK)
    override fun getAllStudents(name: Optional<String>, email: Optional<String>, username: Optional<String>): List<SafeStudentDTO> {     //MUDAR TIPO DE DTO PARA UM SEM PASSWORD
        return service.getAllStudents(name, email, username).map {
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


    @PreAuthorize("hasAnyRole('ADMIN', 'SPONSOR', 'REVIEWER', 'SPONSOR','STUDENT')")
    @ResponseStatus(HttpStatus.OK)
    override fun getStudent(id: Long): SafeStudentDTO {
        return service.getStudent(id).let {
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


    @PreAuthorize("hasRole('ADMIN') or @SecurityService.canEditStudent(principal, #id)") // STUDENT E O PROPRIO
    @ResponseStatus(HttpStatus.OK)
    override fun getStudentApplications(id: Long): List<ApplicationDTO> {
        return service.getStudentApplications(id).map {
            ApplicationDTO(
                    it.applicationID,
                    it.submissionDate,
                    it.status,
                    it.responses.map { GrantResponse(
                            it.question.let { GrantQuestion(it.fieldDescription,
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


    @PreAuthorize("hasAnyRole('ADMIN') or @SecurityService.canEditStudent(principal,#student)")
    @ResponseStatus(HttpStatus.OK)
    override fun updateStudent(student: SafeStudentDTO) {
        service.updateStudent(student.let {
            StudentDAO(
                    it.id,
                    it.address,
                    it.birthDate,
                    InstitutionDAO(it.institutionId),
                    it.name,
                    it.email,
                    it.course,
                    CvDAO(it.cvId),
                    mutableListOf(),
                    it.username,
                    "",
                    mutableListOf()
            )
        })
    }


    @PreAuthorize("hasRole('ADMIN') or @SecurityService.canEditStudent(principal,#id)")
    @ResponseStatus(HttpStatus.OK)
    override fun deleteStudent(id: Long): StudentDTO {
        return service.deleteStudent(id).let {
            StudentDTO(
                    it.id,
                    it.address,
                    it.birthDate,
                    it.institution.id,
                    it.name,
                    it.email,
                    it.course,
                    it.cv.id,
                    it.applications.map { it.applicationID },
                    it.username,
                    it.password,
                    it.roles.map{ it.role }.toMutableList()

            )
        }
    }


    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN', 'SPONSOR', 'REVIEWER', 'SPONSOR','STUDENT')")
    override fun getCV(id: Long): CV {
        return service.getCv(id).let {
            CV(it.id,
                    it.fields.map {
                        Field(it.mandatory,
                                it.type,
                                it.content)
                    })
        }
    }

    @PreAuthorize("hasRole('ADMIN') or @SecurityService.canEditStudent(principal, #id)")
    @ResponseStatus(HttpStatus.CREATED)
    override fun createCV(id: Long, cv: CV) {
        service.createCV(id, CvDAO(cv.fields.map { FieldDAO(it.mandatory, it.type, it.content) }.toMutableList()))
    }

    @PreAuthorize("hasRole('ADMIN') or @SecurityService.canEditStudent(principal,#id)")
    @ResponseStatus(HttpStatus.OK)
    override fun updateCV(id: Long, cv: CV) {
        service.updateCV(id, CvDAO(cv.fields.map { FieldDAO(it.mandatory, it.type, it.content) }.toMutableList()))
    }

    @PreAuthorize("hasRole('ADMIN') or @SecurityService.canEditStudent(principal,#id)")
    @ResponseStatus(HttpStatus.OK)
    override fun deleteCV(id: Long): CV {
        return service.deleteCV(id).let { CV(it.id, it.fields.map { Field(it.mandatory, it.type, it.content) }) }
    }


}
package com.iadi.grants.services.services

import com.iadi.grants.model.InstitutionRepository
import com.iadi.grants.model.ReviewerRepository
import com.iadi.grants.model.StudentRepository
import com.iadi.grants.services.dao.InstitutionDAO
import com.iadi.grants.services.dao.ReviewerDAO
import com.iadi.grants.services.dao.StudentDAO
import com.iadi.grants.services.Exceptions.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class InstitutionService(val institutionRepository: InstitutionRepository,
                         val studentRepository: StudentRepository,
                         val reviewerRepository: ReviewerRepository) {

    @Transactional
    fun addInstitution(institution: InstitutionDAO) {
        institution.id = 0
        if(institutionRepository.existsByNameAndAndEmailAndPhoneNumb(institution.name,institution.email,institution.phoneNumb)){
            throw AlreadyExistsException("Institution already exists")
        }else
            institutionRepository.save(institution)
    }


    fun getInstitutions(): List<InstitutionDAO> {
        val inst = institutionRepository.findAll().toList()
        return inst;
    }

    @Transactional
    fun getInstitution(id: Long): InstitutionDAO {
        return institutionRepository.findById(id).orElseThrow{
            NotFoundException("Institution with $id was not found")
        }
    }


    fun getInstituitionByName(name:String):InstitutionDAO{
        return institutionRepository.findByName(name).orElseThrow{
            NotFoundException("Institution with $name name was not found")
        }
    }


    fun getInstitutionStudents(id: Long): List<StudentDAO> {
        if(!institutionRepository.existsById(id)){
            throw NotFoundException("Institution with $id id was not found")
        }else
            return studentRepository.findByInstitutionId(id).toList()
    }


    fun getInstitutionReviewers(id: Long): List<ReviewerDAO> {
        if(!institutionRepository.existsById(id)){
            throw NotFoundException("Institution with $id id was not found")
        }else
            return reviewerRepository.findByInstitutionId(id).toList()
    }

    @Transactional
    fun updateInstitution(institution: InstitutionDAO) {
        if(!institutionRepository.existsById(institution.id)){
            throw NotFoundException("Institution with" +institution.id +"id was not found")
        }else
            institutionRepository.save(institution)
    }

    @Transactional
    fun deleteInstitution(id: Long): InstitutionDAO {
        val inst = getInstitution(id)
        inst.students= getInstitutionStudents(id).toMutableList()
        inst.reviewers=getInstitutionReviewers(id).toMutableList()
        institutionRepository.deleteById(id)
        return inst
    }

}
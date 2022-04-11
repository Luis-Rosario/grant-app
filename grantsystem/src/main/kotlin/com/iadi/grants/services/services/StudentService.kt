package com.iadi.grants.services.services


import com.iadi.grants.model.*
import com.iadi.grants.services.Exceptions.AlreadyExistsException
import com.iadi.grants.services.dao.ApplicationDAO
import com.iadi.grants.services.dao.StudentDAO
import com.iadi.grants.services.Exceptions.NotFoundException
import com.iadi.grants.services.dao.CvDAO
import com.iadi.grants.services.dao.RoleDAO
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class StudentService(val students: StudentRepository,
                     val application: ApplicationRepository,
                     val institutions:InstitutionRepository,
                     val users:UserRepository,val roles:RoleRepository) {



    @Transactional
    fun registerStudent(student: StudentDAO):StudentDAO {
        if(students.existsByEmail(student.email)){
            throw AlreadyExistsException("Student with that email already exists in the system.")
        }

        val instID = student.institution.id
        val inst = institutions.findById(instID).orElseThrow{
            NotFoundException("Institution Not Found")
        }

        var rolesList = mutableListOf<RoleDAO>()

        for(role in student.roles){
            val fRole = roles.findByRole(role.role)
            if(fRole.isPresent )
                rolesList.add(fRole.get())
            else
                throw NotFoundException("Role was not found")
        }

        val encoder = BCryptPasswordEncoder()

        student.password=encoder.encode(student.password)
        student.institution=inst
        student.roles=rolesList
        student.id = 0
        return students.save(student)
    }


    fun getAllStudents(name: Optional<String>, email: Optional<String>, username: Optional<String>):List<StudentDAO>{
       if(name.isPresent) {
           if (email.isPresent) {
               return students.findByNameContainingAndEmailContaining(name.get(), email.get()).orElseThrow {
                   NotFoundException("Students with that combination were not found")
               }
           }/** else if (username.isPresent) {

           */ else {
               return students.findByNameContaining(name.get()).orElseThrow {
                   NotFoundException("Students with that keyword ${name.get()} were not found")
               }
           }
       }
       else if (email.isPresent) {
          /* if(username.isPresent) {

           } else {*/
               return students.findByEmailContaining(email.get()).orElseThrow {
                   NotFoundException("Students with that keyword ${email.get()} were not found")
               }
       }
       else if(username.isPresent) {
           return students.findByUsernameContaining(username.get()).orElseThrow {
               NotFoundException("Students with that keyword ${username.get()} were not found")
           }
       }
       else {
           return students.findAll().toList()
       }
    }


    fun getStudent(id: Long): StudentDAO = students.findById(id).orElseThrow {
        NotFoundException("Student with $id was not found")
    }


    fun getStudentApplications(id: Long): List<ApplicationDAO> {
        val std :StudentDAO = getStudent(id)

        val res = application.findByStudentId(std.id).toList()
        return res


    }

    @Transactional
    fun updateStudent(student: StudentDAO) {
        val oldStud: StudentDAO = getStudent(student.id)

        oldStud.email=student.email
        oldStud.name=student.name
        oldStud.address=student.address
        oldStud.birthDate=student.birthDate
        oldStud.course=student.course

        students.save(oldStud)
    }

    @Transactional
    fun deleteStudent(id: Long): StudentDAO {
        val student: StudentDAO = getStudent(id)
        students.deleteById(id)
        return student
    }


    fun getCv(id:Long):CvDAO{
        val cv = getStudent(id).cv
        if(cv.id==0L){
            throw NotFoundException("The student with id $id doesn't have a CV")
        }
        else
            return cv

    }

    fun createCV(id: Long, cv: CvDAO) {
        val student = getStudent(id)
        if(student.cv.fields.isNotEmpty()){
            throw AlreadyExistsException("This student already has a CV")
        }
        else
            student.cv.fields=cv.fields

        students.save(student)
        users.save(student)
    }

    fun updateCV(id: Long, cv: CvDAO) {
        val student = getStudent(id)
        student.cv.fields=cv.fields
        students.save(student)
    }


    fun deleteCV(id:Long):CvDAO{
        val student = getStudent(id)
        val cv = student.cv
        if(cv.fields.isEmpty())
            throw NotFoundException("The Student with id $id doesn't have a CV")
        student.cv= CvDAO(0)
        students.save(student)
        return cv
    }



}

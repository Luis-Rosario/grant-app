package com.iadi.grants.model



import com.iadi.grants.services.dao.StudentDAO
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.*

interface StudentRepository: CrudRepository<StudentDAO, Long> {

    @Query("select student from StudentDAO student where student.institution.id =:id")
    fun findByInstitutionId(id:Long): MutableIterable<StudentDAO>


    @Query("select student from StudentDAO student where student.email =: email")
    fun findByEmail(email:String): MutableIterable<StudentDAO>


    fun existsByEmail(email:String):Boolean

    fun findByNameContaining(name:String):Optional<List<StudentDAO>>

    fun findByEmailContaining(email: String): Optional<List<StudentDAO>>

    fun findByUsernameContaining(username:String): Optional<List<StudentDAO>>

    fun findByNameContainingAndEmailContaining(name: String,email: String): Optional<List<StudentDAO>>


}
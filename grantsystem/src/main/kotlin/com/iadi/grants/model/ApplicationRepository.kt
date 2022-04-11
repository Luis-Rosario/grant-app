package com.iadi.grants.model

import com.iadi.grants.api.data.Status
import com.iadi.grants.services.dao.ApplicationDAO
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.*

interface ApplicationRepository: CrudRepository<ApplicationDAO, Long> {

    @Query("select app from ApplicationDAO app inner join fetch app.grant where app.grant.grantId = :id")
    fun findByGrantId(id:Long):MutableIterable<ApplicationDAO>

    @Query("select app from ApplicationDAO app inner join fetch app.grant where app.grant.grantId = :id and app.status <> 'DRAFT'")
    fun findByGrantIdNotDrafts(id:Long):MutableIterable<ApplicationDAO>

    @Query("select app from ApplicationDAO app inner join fetch app.grant where app.grant.grantId = :id and app.status = :status")
    fun findByGrantIdAndStatus(id:Long,status: Status):MutableIterable<ApplicationDAO>

    @Query("select app from ApplicationDAO app inner join fetch app.student where app.student.id = :id")
    fun findByStudentId(id:Long):MutableIterable<ApplicationDAO>

    @Query("select app from ApplicationDAO app inner join fetch app.student where app.student.id = :studentID and app.grant.grantId= :grantID")
    fun findByGrantAndStudent(grantID:Long,studentID:Long): Optional<ApplicationDAO>

}
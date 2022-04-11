package com.iadi.grants.services.services

import com.iadi.grants.api.data.Status
import com.iadi.grants.model.*
import com.iadi.grants.services.Exceptions.AlreadyExistsException
import com.iadi.grants.services.Exceptions.NotAllowedException
import com.iadi.grants.services.dao.ApplicationDAO
import com.iadi.grants.services.Exceptions.NotFoundException
import com.iadi.grants.services.dao.EvaluationDAO
import com.iadi.grants.services.dao.GrantDAO
import com.iadi.grants.services.dao.StudentDAO


import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service("appService")
class ApplicationService(val apps:ApplicationRepository, val students:StudentRepository,val grants:GrantRepository, val evals:EvaluationRepository, val panels:PanelRepository) {



    fun getApplication(appId: Long): ApplicationDAO {
        val app =  apps.findById(appId).orElseThrow{
            NotFoundException("Application with $appId was not found")
        }
        if(app.status == Status.SUBMITTED || app.status == Status.DRAFT){
            app.reviews= emptyList()
        }
        return app
    }

    fun getAllApplications(): List<ApplicationDAO> {
        val appList =  apps.findAll().toList()
        for(app in appList){
            if(app.status == Status.SUBMITTED || app.status == Status.DRAFT){
                app.reviews= emptyList()
            }
        }
        return appList
    }

    @Transactional
    fun addApplication(app: ApplicationDAO, studentId:Long, grantId:Long) {
        app.applicationID=0
        val student = students.findById(studentId).orElseThrow{
             NotFoundException("Student with $studentId was not found")
        }
        val grant = grants.findById(grantId).orElseThrow{
            NotFoundException("Grant with $grantId was not found")
        }

        if(!apps.findByGrantAndStudent(grantId,studentId).isEmpty){
            throw AlreadyExistsException("This student already applied for this Grant")
        }

        if(app.grant.deadline.isBefore(LocalDate.now())){
            throw NotAllowedException("Submission Date is after Deadline")
        }

        app.grant = grant
        for(i in grant.applicationQuestions.indices){
            if(grant.applicationQuestions[i].mandatory && app.responses[i].equals(""))
                throw NotAllowedException("Submission Date is after Deadline")
            app.responses[i].question=grant.applicationQuestions[i]
        }

        if(app.status.equals(Status.SUBMITTED)){
            generateSameInstEvaluations(grantId,student,app,grant)
        }

        app.student = student
        apps.save(app)

    }

    @Transactional
    fun updateApplication(app: ApplicationDAO) {
        val oldApp = apps.findById(app.applicationID).orElseThrow{
            NotFoundException("Application with ${app.applicationID} was not found")
        }

        if(oldApp.status.equals(Status.DRAFT)){
            for(i in oldApp.responses.indices){
                oldApp.responses[i].response = app.responses[i].response
            }
            if(app.status.equals(Status.SUBMITTED) && oldApp.reviews.isEmpty()){
                if(app.grant.deadline.isBefore(LocalDate.now())){
                    throw NotAllowedException("Submission Date is after Deadline")
                }
                generateSameInstEvaluations(oldApp.grant.grantId,oldApp.student,app,oldApp.grant)
            }
        }

        oldApp.status = app.status

        oldApp.submissionDate = app.submissionDate
        apps.save(oldApp)
    }

    @Transactional
    fun deleteApplication(appId: Long): ApplicationDAO {
        val oldApp = apps.findById(appId).orElseThrow{
            NotFoundException("Application with $appId was not found")
        }
        apps.deleteById(appId)
        return oldApp
    }

    fun getApplicationEvaluations(appId: Long): List<EvaluationDAO> {
        return evals.findByAppApplicationID(appId).orElseThrow {
            NotFoundException("Evals of application $appId were not found")
        }
    }


    private fun generateSameInstEvaluations(grantId: Long,student:StudentDAO,app:ApplicationDAO,grant:GrantDAO){
        val panel = panels.findByGrantGrantId(grantId).get()
        for(rev in panel.reviewers){
            if(student.institution.equals(rev.institution)){
                val eval = EvaluationDAO(0,false,rev,app,grant,"Could not Review (same institution)")
                evals.save(eval)
            }
        }
    }


}
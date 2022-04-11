package com.iadi.grants.services.services


import com.iadi.grants.api.data.SafeSponsorDTO
import com.iadi.grants.api.data.SafeStudentDTO
import com.iadi.grants.api.data.Status
import com.iadi.grants.api.data.StudentDTO
import com.iadi.grants.model.*
import com.iadi.grants.services.dao.ReviewerDAO
import com.iadi.grants.services.dao.RoleDAO
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.*


@Service("SecurityService")
class SecurityService(val revs: ReviewerRepository,
                      val students: StudentRepository,
                      val sponsors: SponsorRepository,
                      val grants: GrantRepository,
                      val apps: ApplicationRepository,
                      val panels: PanelRepository,
                      val users: UserRepository,
                      val evals: EvaluationRepository
) {


    fun getUserRoles(roles: MutableList<RoleDAO>): MutableList<GrantedAuthority> {
        var auths: MutableList<GrantedAuthority> = mutableListOf()

        roles.forEach { p ->
            val auth = SimpleGrantedAuthority("ROLE_"+p.role)
            auths.add(auth)
        }

        return auths
    }

    fun canEditGrant(principal: CustomUserDetails, id: Long): Boolean {
        val grant = grants.findById(id)
        return (grant.isPresent && grant.get().sponsor.username.equals(principal.username))
    }

    fun canEditPanel(principal: UserDetails, id: Long): Boolean {
        val panel = panels.findById(id)
        return (panel.isPresent && panel.get().panelChair.username.equals(principal.username))
    }


    fun canEditStudent(principal: UserDetails, id: Long): Boolean {
        val student = students.findById(id)
        return (student.isPresent && student.get().username.equals(principal.username))
    }

    fun canEditStudent(principal: UserDetails, st: SafeStudentDTO):Boolean{
        val student = students.findById(st.id)
        return (student.isPresent && student.get().username.equals(principal.username))
    }

    fun canGetStudent(principal: UserDetails, username: Optional<String>): Boolean {
        if(username.isPresent)
            return principal.username.equals(username.get())
        else
            return false
    }


    fun canEditReviewer(principal: UserDetails, reviewer: ReviewerDAO): Boolean {
        val rev = revs.findById(reviewer.id)
        return (rev.isPresent && rev.get().username.equals(principal.username))
    }

    fun canEditReviewer(principal: UserDetails, reviewerId: Long): Boolean {
        val rev = revs.findById(reviewerId)
        return (rev.isPresent && rev.get().username.equals(principal.username))
    }

    fun canGetReviewer(principal: UserDetails, username: Optional<String>): Boolean {
        if(username.isPresent)
            return principal.username.equals(username.get())
        else
            return false
    }

    fun canGetSponsor(principal: UserDetails, username: Optional<String>): Boolean {
        if(username.isPresent) {
            return principal.username.equals(username.get())
        }
        else
            return false
    }

    fun canEditSponsor(principal: UserDetails, sponsor: SafeSponsorDTO): Boolean {
        val sp = sponsors.findById(sponsor.sponsorID)
        return (sp.isPresent && sp.get().username.equals(principal.username))
    }


    fun canEditEvaluation(principal: UserDetails, id: Long): Boolean {
        val eval = evals.findById(id)
        return (eval.isPresent && eval.get().rev.username.equals(principal.username))
    }

    fun canGetApplicationsGrant(principal: UserDetails, id: Long, status:Optional<Status>): Boolean {
        val grant = grants.findById(id)
        val auth = principal.authorities

        //REVIEWER OF GRANT
        if(auth.contains(SimpleGrantedAuthority("ROLE_REVIEWER"))){
            val rev = revs.findByUsername(principal.username)
            if(rev.isPresent){
                val panel = panels.findByReviewerAndGrantGrantId(rev.get(),id)
                return panel.isPresent
            }
        }

        if (grant.isPresent) {
            for(app in grant.get().applications){
                //OWNER OF APP
                if(app.student.username.equals(principal.username)){
                    return true
                }
            }
            //GRANT SPONSOR
            if(grant.get().sponsor.username.equals(principal.username)){
                return true
            }

            //ANON AND OTHERS
            if(grant.get().deadline.isBefore(LocalDate.now())) {
                    if (status.isPresent){
                        if(status.get().equals(Status.GRANTED)){
                            return status.get().equals(Status.GRANTED)
                        }
                    }
            }
        }
        return false

    }

    fun canGetApplication(principal: UserDetails, id: Long): Boolean {
        val app = apps.findById(id)
        if (app.isPresent) {
            val panel = panels.findByGrantGrantId(app.get().grant.grantId)
            if (panel.isPresent){
                for (rev in panel.get().reviewers) {
                    if (rev.username.equals(principal.username))
                        return true
                }
            }

            val sponsor = sponsors.findById(app.get().grant.sponsor.id)
            if(principal.authorities.contains(SimpleGrantedAuthority("ROLE_SPONSOR"))){
                if (sponsor.isPresent)
                    return sponsor.get().username.equals(principal.username)
            }
            else if (principal.authorities.contains(SimpleGrantedAuthority("ROLE_STUDENT")))
                return app.get().student.username.equals(principal.username)
        }
        return false

    }

    fun canGetApplicationEvals(principal: UserDetails, id: Long): Boolean {
        val app = apps.findById(id)
        if (app.isPresent) {
            val panel = panels.findByGrantGrantId(app.get().grant.grantId)
            if (panel.isPresent){
                for (rev in panel.get().reviewers) {
                    if (rev.username.equals(principal.username))
                        return true
                }
            }

            val sponsor = sponsors.findById(app.get().grant.sponsor.id)
            if(principal.authorities.contains(SimpleGrantedAuthority("ROLE_SPONSOR"))){
                if (sponsor.isPresent)
                    return sponsor.get().username.equals(principal.username)
            }
            else if (principal.authorities.contains(SimpleGrantedAuthority("ROLE_STUDENT")) && (app.get().status.equals(Status.GRANTED) || app.get().status.equals(Status.NOT_GRANTED)))
                return app.get().student.username.equals(principal.username)
        }
        return false

    }

    fun canEditApplication(principal: UserDetails, id: Long,status:String): Boolean {
        val app = apps.findById(id)
        if (app.isPresent) {
             if(app.get().student.username.equals(principal.username) && (app.get().status.equals(Status.DRAFT) || status.equals(Status.SUBMITTED)))
                 return true
            val panel = panels.findByGrantId(app.get().grant.grantId)
            if(panel.isPresent){


                return panel.get().panelChair.username.equals(principal.username) && app.get().status.equals(Status.SUBMITTED)
                        && app.get().reviews.size==4
            }
        }
        return false
    }

    fun canDeleteApplication(principal: UserDetails, id: Long): Boolean {
        val app = apps.findById(id)
        if (app.isPresent) {
            if(app.get().student.username.equals(principal.username))
                return true
        }
        return false
    }

    fun canPostApplication(principal: UserDetails, userId: Long): Boolean {
        val student = students.findById(userId)

        if (student.isPresent) {
            return student.get().username.equals(principal.username)
        }
        return false
    }

    fun belongsToPanel(principal: UserDetails, grantID: Long): Boolean {
        val grant = grants.findById(grantID)
        if (grant.isPresent) {
            val panel = panels.findByGrantGrantId((grantID))
            if (panel.isPresent) {
                panel.get().reviewers.forEach { e ->
                    if (e.username.equals(principal.username))
                        return true
                }
                if (panel.get().panelChair.username.equals(principal.username))
                    return true
            }
            return false
        }
        return false
    }

    fun isApplicationOwner(principal: UserDetails, evalID: Long):Boolean {
        val eval = evals.findById(evalID)
        if (eval.isPresent) {
            return eval.get().app.student.username.equals(principal.username)
        }
        return false
    }

    fun isPanelChairmanOfEval(principal:UserDetails, evalID:Long):Boolean{
        val eval = evals.findById(evalID)
        if (eval.isPresent) {
            val panel = panels.findByGrantGrantId(eval.get().grant.grantId)
            if(panel.isPresent){
                return panel.get().panelChair.username.equals(principal.username)
            }
            return false
        }
        return false
    }

    fun belongsToSponsorGrant(principal: UserDetails, id:Long):Boolean{
        val panel = panels.findById(id)
        if(panel.isPresent){
           return panel.get().grant.sponsor.username.equals(principal.username)
        }
        return false
    }
    

    fun isPanelChairman(principal:UserDetails, panelID:Long):Boolean {
        val panel = panels.findById(panelID)
        if (panel.isPresent) {
            return panel.get().panelChair.username.equals(principal.username)
        }
        return false
    }


    fun isLoggedSponsor(principal:UserDetails ,sponsorID:Long):Boolean{
        val sponsor = sponsors.findById(sponsorID)
        if(sponsor.isPresent){
            return sponsor.get().username.equals(principal.username)
        }
        return false
    }

    fun belongsToPanelFromApplication(principal: UserDetails, appId: Long): Boolean {
        val app = apps.findById(appId)
        if (app.isPresent) {
            val grantID = app.get().grant.grantId
            val panel = panels.findByGrantGrantId((grantID))
            if (panel.isPresent) {
                panel.get().reviewers.forEach { e ->
                    if (e.username.equals(principal.username))
                        return true
                }
                if (panel.get().panelChair.username.equals(principal.username))
                    return true
            }
            return false
        }
        return false
    }


}
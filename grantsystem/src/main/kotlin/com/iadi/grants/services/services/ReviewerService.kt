package com.iadi.grants.services.services

import com.iadi.grants.model.*
import com.iadi.grants.services.Exceptions.AlreadyExistsException
import com.iadi.grants.services.dao.EvaluationDAO
import com.iadi.grants.services.dao.ReviewerDAO
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.iadi.grants.services.Exceptions.NotFoundException
import com.iadi.grants.services.dao.PanelDAO
import com.iadi.grants.services.dao.RoleDAO

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.awt.Panel

import java.util.*

@Service
class ReviewerService(val reviewers: ReviewerRepository,
                      val evals: EvaluationRepository,
                      val insts: InstitutionRepository,
                      val users: UserRepository,
                      val roles:RoleRepository,
                      val panels:PanelRepository) {

    @Transactional
    fun addReviewer(reviewer: ReviewerDAO):ReviewerDAO {
        reviewer.id = 0
        val instID = reviewer.institution.id
        val inst = insts.findById(instID).orElseThrow {
            NotFoundException("Institution with id $instID was not found.")
        }


        if(users.existsByUsername(reviewer.username)){
            throw AlreadyExistsException("Eeviewer with the given username ${reviewer.username} already exists in the system.")
        }

        val encoder = BCryptPasswordEncoder()

        var rolesList = mutableListOf<RoleDAO>()

        for(role in reviewer.roles){
            val fRole = roles.findByRole(role.role)
            if(fRole.isPresent )
                rolesList.add(fRole.get())
            else
                throw NotFoundException("Role was not found")
        }

        reviewer.password=encoder.encode(reviewer.password)
        reviewer.institution = inst
        reviewer.roles=rolesList
        return reviewers.save(reviewer)
    }


    fun getReviewers(name: Optional<String>, email: Optional<String>, username: Optional<String>):List<ReviewerDAO> {
        if (name.isPresent) {
            if (email.isPresent) {
                return reviewers.findByNameContainingAndEmailContaining(name.get(), email.get()).orElseThrow {
                    NotFoundException("Reviewers with that combination were not found")
                }
            }
            /**else if (username.isPresent) {

            }*/
            else {
                return reviewers.findByNameContaining(name.get()).orElseThrow {
                    NotFoundException("Reviewers with that keyword ${name.get()} were not found")
                }
            }
        }
        else if (email.isPresent) {
            /**if(username.isPresent) {

            } else {*/
            return reviewers.findByEmailContaining(email.get()).orElseThrow {
                NotFoundException("Reviewers with that keyword ${email.get()} were not found")
            }
        }
        else if(username.isPresent) {
            return reviewers.findByUsernameContaining(username.get()).orElseThrow {
                NotFoundException("Reviewers with that keyword ${username.get()} were not found")
            }
        }
        else {
            return reviewers.findAll().toList()
        }
    }


    fun getReviewer(id: Long): ReviewerDAO = reviewers.findById(id).orElseThrow{
        NotFoundException("Reviewer with id $id was not found.")
    }


    fun getReviewerEvals(id:Long): List<EvaluationDAO> {
        reviewers.findById(id).orElseThrow{ NotFoundException("Reviewer with id $id was not found.")}
        return evals.findByReviewerId(id).toList()
    }

    fun getReviewerPanels(id:Long): List<PanelDAO> {
        var rev = reviewers.findById(id).orElseThrow{ NotFoundException("Reviewer with id $id was not found.")}
        return panels.findByReviewerId(rev).toList()
    }


    @Transactional
    fun updateReviewer(newReviewer: ReviewerDAO) {

        val oldRev =getReviewer(newReviewer.id)

        oldRev.email=newReviewer.email
        oldRev.name=newReviewer.name
        oldRev.address=newReviewer.address
        oldRev.birthDate=newReviewer.birthDate

        reviewers.save(oldRev)
    }

    @Transactional
    fun deleteReviewer(id: Long): ReviewerDAO {
        val reviewer = getReviewer(id)
        reviewers.deleteById(id)
        users.deleteById(id)
        return reviewer
    }





}
package com.iadi.grants.services.services

import com.iadi.grants.model.ApplicationRepository
import com.iadi.grants.model.EvaluationRepository
import com.iadi.grants.model.PanelRepository
import com.iadi.grants.services.Exceptions.AlreadyExistsException
import com.iadi.grants.services.Exceptions.NotAllowedException
import com.iadi.grants.services.dao.EvaluationDAO
import com.iadi.grants.services.Exceptions.NotFoundException
import com.iadi.grants.services.dao.ReviewerDAO
import net.bytebuddy.implementation.bytecode.Throw
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class EvaluationService (val evals: EvaluationRepository, val panels: PanelRepository,val apps:ApplicationRepository) {

    @Transactional
    fun addEvaluation(eval: EvaluationDAO) {

        val panel = panels.findByGrantId(eval.grant.grantId)
        var belongs = false
        var reviewer = eval.rev

        if(panel.isPresent){

            for(rev in panel.get().reviewers){
                if(rev.id == (eval.rev.id)){
                    eval.rev=rev
                    reviewer=rev
                    belongs=true
                }
            }

        }else{
            throw NotFoundException("Panel was not Found")
        }

        if(belongs){
            if(evals.existsByAppApplicationIDAndRevId(eval.app.applicationID,eval.rev.id)){
                throw AlreadyExistsException("Reviewer's ${eval.rev.id} Evaluation for Application ${eval.app.applicationID} already Exists")
            }

            val appl = apps.findById(eval.app.applicationID).get()
            eval.app=appl

            if(reviewer.institution.id==eval.app.student.institution.id){
                throw NotAllowedException("This reviewer can't post an evaluation for this application")
            }

            eval.grant=panel.get().grant

            eval.evalId=0

            evals.save(eval)

        }
        else{
            throw NotAllowedException("This reviewer can't post an evaluation for this application")
        }
    }

    fun getAllEvals(grantId: Optional<Long>, applicationId: Optional<Long>): List<EvaluationDAO>{
        if(grantId.isPresent)
            if(applicationId.isPresent)
                return evals.findByAppApplicationIDAndGrantGrantId(applicationId.get(),grantId.get()).orElseThrow {
                    NotFoundException("Evaluations with that combination were not found")
                }
            else
                return evals.findByGrantGrantId(grantId.get()).orElseThrow {
                    NotFoundException("Evaluations with that keyword ${grantId.get()} were not found")
                }
        else {
            if(applicationId.isPresent)
                return evals.findByAppApplicationID(applicationId.get()).orElseThrow {
                    NotFoundException("Evaluations with that keyword ${applicationId.get()} were not found")
                }
            else
                return evals.findAll().toList()
        }
    }

    fun getEval(id: Long): EvaluationDAO = evals.findById(id).orElseThrow{
        NotFoundException("Evaluation with $id was not found")
    }

    @Transactional
    fun updateEval(eval: EvaluationDAO) {
        val oldEvalOp = evals.findById(eval.evalId)
        if(oldEvalOp.isPresent){
            var oldEval = oldEvalOp.get()
            oldEval.status=eval.status
            evals.save(oldEval)
        }
    }

    @Transactional
    fun deleteEval(id: Long): EvaluationDAO {
        val eval: EvaluationDAO = getEval(id)
        evals.deleteById(id)
        return eval
    }
}
package com.iadi.grants.services.dao

import com.iadi.grants.api.data.Status
import java.time.LocalDate
import javax.persistence.*

@Entity
data class ApplicationDAO(@Id
                          @GeneratedValue(strategy = GenerationType.AUTO,generator = "app_generator")
                          @SequenceGenerator(name="app_generator", sequenceName = "applicationSeq")
                          var applicationID:Long,
                          var submissionDate: LocalDate,
                          @Enumerated(EnumType.STRING)
                          var status:Status,
                          @OneToMany(cascade = [CascadeType.ALL])
                          var responses: List<GrantResponseDAO>,
                          @ManyToOne()
                          var grant: GrantDAO,
                          @ManyToOne
                          var student: StudentDAO,
                          @OneToMany(mappedBy="app",cascade = [CascadeType.ALL])
                          var reviews:List<EvaluationDAO>)
{



    constructor() : this(0,LocalDate.now(),Status.DRAFT,
            mutableListOf<GrantResponseDAO>(), GrantDAO(), StudentDAO(),mutableListOf<EvaluationDAO>()) {

    }

    constructor(id:Long) : this(id,LocalDate.now(),Status.DRAFT,
            mutableListOf<GrantResponseDAO>(), GrantDAO(), StudentDAO(),mutableListOf<EvaluationDAO>()) {

    }




    override fun equals(other: Any?): Boolean {
        if(!(other is ApplicationDAO ))
            return false
        return(applicationID.equals(other.applicationID) &&
                submissionDate.equals(other.submissionDate) &&
                status.equals(other.status) && responses.toString().equals(other.responses.toString()) && grant.grantId.equals(other.grant.grantId) && student.id.equals(other.student.id))

    }

    override fun hashCode(): Int {
        var result = applicationID.hashCode()
        result = 31 * result + submissionDate.hashCode()
        result = 31 * result + status.hashCode()
        result = 31 * result + responses.hashCode()
        result = 31 * result + grant.hashCode()
        result = 31 * result + student.hashCode()
        return result
    }


}
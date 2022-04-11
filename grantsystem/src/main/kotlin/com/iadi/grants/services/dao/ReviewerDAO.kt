package com.iadi.grants.services.dao

import java.time.LocalDate
import javax.persistence.*

@Entity
data class ReviewerDAO(@Id
                       @GeneratedValue(strategy = GenerationType.AUTO,generator = "app_generator")
                       @SequenceGenerator(name="app_generator", sequenceName = "userSeq")
                       override var id:Long,
                       var address:String,
                       var birthDate:LocalDate,
                       @ManyToOne
                       var institution: InstitutionDAO,
                       override var name:String,
                       override var email:String,
                       @OneToMany(mappedBy = "rev", cascade = [CascadeType.ALL])
                       var evals:MutableList<EvaluationDAO>,
                       override var username:String,
                       override var password:String,
                       @ManyToMany
                       override var roles: MutableList<RoleDAO>
                       ):UserDAO(
        0, "", "", "", "", mutableListOf(RoleDAO("REVIEWER"))
){

    constructor() : this(0,"", LocalDate.now(), InstitutionDAO(),"","",mutableListOf(),"","",mutableListOf(RoleDAO("REVIEWER"))) {

    }

    constructor(id:Long) : this(id,"", LocalDate.now(), InstitutionDAO(),"","", mutableListOf(),"","",mutableListOf(RoleDAO("REVIEWER"))) {

    }
    constructor(id:Long,role:String) : this(id,"", LocalDate.now(), InstitutionDAO(),"","", mutableListOf(),"","",
            mutableListOf(RoleDAO("REVIEWER"),RoleDAO(role))) {

    }
}
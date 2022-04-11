package com.iadi.grants.services.dao

import javax.persistence.*

@Entity
data class InstitutionDAO(
        @Id @GeneratedValue(strategy = GenerationType.AUTO,generator = "app_generator")
        @SequenceGenerator(name="app_generator", sequenceName = "instSeq")
        var id:Long,
        var name:String,
        var email:String,
        var phoneNumb: String,
        @OneToMany(mappedBy = "institution")
        var students: MutableList<StudentDAO>,
        @OneToMany(mappedBy = "institution")
        var reviewers: MutableList<ReviewerDAO>

) {
    constructor() : this(0, "","","", mutableListOf<StudentDAO>(), mutableListOf<ReviewerDAO>()) {

    }

    constructor(institutionId:Long) : this(institutionId,"","","", mutableListOf<StudentDAO>(), mutableListOf<ReviewerDAO>())
}
package com.iadi.grants.services.dao

import java.time.LocalDate
import javax.persistence.*


@Entity
data class GrantDAO(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO,generator = "app_generator")
        @SequenceGenerator(name="app_generator", sequenceName = "grantSeq")
        var grantId:Long,
        var title:String,
        var openingDate:LocalDate,
        var deadline: LocalDate,
        var description:String,
        var funding:Int,
        @OneToMany(cascade = [CascadeType.ALL])
        var applicationQuestions: List<GrantQuestionDAO>,
        @OneToMany(mappedBy = "grant", cascade = [CascadeType.ALL])
        var applications:MutableList<ApplicationDAO>,
        @ManyToOne
        var sponsor: SponsorDAO)
{

    constructor() : this(0,  "", LocalDate.now(), LocalDate.now(), "", 0,   listOf() , mutableListOf<ApplicationDAO>(), SponsorDAO()) {

    }

    constructor(grantId:Long) : this(grantId,"", LocalDate.now(), LocalDate.now(), "", 0,   listOf() , mutableListOf<ApplicationDAO>(), SponsorDAO())  {
    }


}
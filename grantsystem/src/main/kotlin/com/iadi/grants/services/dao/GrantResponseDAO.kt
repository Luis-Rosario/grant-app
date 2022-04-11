package com.iadi.grants.services.dao

import javax.persistence.*

@Entity
data class GrantResponseDAO(@ManyToOne var question: GrantQuestionDAO,
                            var response:String) {

    constructor() : this(GrantQuestionDAO(),"") {

    }


    @Id @GeneratedValue(generator = "app_generator")
    @SequenceGenerator(name="app_generator", sequenceName = "grantResponseSeq")
    var id: Long = 0
}

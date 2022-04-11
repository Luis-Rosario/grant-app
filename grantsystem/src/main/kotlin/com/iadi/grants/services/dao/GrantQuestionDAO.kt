package com.iadi.grants.services.dao

import javax.persistence.*

@Entity
data class GrantQuestionDAO(var fieldDescription:String,
                            var type:String,
                            var mandatory:Boolean)
{

    @Id @GeneratedValue(generator = "app_generator")
    @SequenceGenerator(name="app_generator", sequenceName = "grantQuestSeq")
    var id: Long = 0

    constructor() : this("","",true) {

    }
}
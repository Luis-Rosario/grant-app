package com.iadi.grants.services.dao

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.SequenceGenerator

@Entity
data class FieldDAO(
        var mandatory:Boolean,
        var type:String,
        var content:String
) {
    constructor() : this(true, "","") {

    }

    @Id
    @GeneratedValue(generator = "app_generator")
    @SequenceGenerator(name="app_generator", sequenceName = "fieldSeq")
    var id: Long = 0
}
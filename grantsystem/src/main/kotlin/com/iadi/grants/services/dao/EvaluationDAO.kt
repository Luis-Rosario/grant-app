package com.iadi.grants.services.dao

import javax.persistence.*
import javax.swing.JFormattedTextField

@Entity
data class EvaluationDAO (@Id
                          @GeneratedValue(generator = "app_generator")
                          @SequenceGenerator(name="app_generator", sequenceName = "evalSeq")
                          var evalId: Long,
                          var status: Boolean,
                          @ManyToOne
                          var rev: ReviewerDAO,
                          @ManyToOne
                          var app: ApplicationDAO,
                          @ManyToOne
                          var grant: GrantDAO,var textField: String) {

    constructor() : this(0, false, ReviewerDAO(), ApplicationDAO(),GrantDAO(),""){

    }
}

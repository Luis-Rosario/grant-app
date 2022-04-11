package com.iadi.grants.services.dao

import javax.persistence.*

@Entity
data class PanelDAO(@Id
                    @GeneratedValue(generator = "app_generator")
                    @SequenceGenerator(name="app_generator", sequenceName = "panelSeq")
                    var panelID:Long,
                    @ManyToOne
                    var panelChair: ReviewerDAO,
                    @ManyToMany
                    var reviewers:MutableList<ReviewerDAO>,
                    @OneToOne
                    var grant: GrantDAO) {
    constructor() : this(0, ReviewerDAO(), mutableListOf<ReviewerDAO>(), GrantDAO()) {

    }

}
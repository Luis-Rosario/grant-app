package com.iadi.grants.services.dao

import javax.persistence.*


@Entity
data class SponsorDAO(@Id
                      @GeneratedValue(strategy = GenerationType.AUTO,generator = "app_generator")
                      @SequenceGenerator(name="app_generator", sequenceName = "userSeq")
                      override var id:Long,
                      override var name: String,
                      override var email: String,
                      var phoneNumber:String,
                      override var username: String,
                      override var password: String,
                      @ManyToMany
                      override var roles: MutableList<RoleDAO>
                     ): UserDAO(0, "", "", "", "", mutableListOf(RoleDAO("SPONSOR"))) {

    constructor() : this(0,"","","","","",mutableListOf(RoleDAO("SPONSOR"))) {

    }

    constructor(sponsorID:Long) : this(sponsorID,"","","","","",mutableListOf<RoleDAO>
    (RoleDAO("SPONSOR")))

    constructor(sponsorID:Long, role:String) : this(sponsorID,"","","","","",mutableListOf<RoleDAO>
    (RoleDAO("SPONSOR"),RoleDAO(role)))

}
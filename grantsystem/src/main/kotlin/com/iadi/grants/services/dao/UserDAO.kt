package com.iadi.grants.services.dao

import javax.persistence.*

@Entity
open class UserDAO(
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_generator")
        @SequenceGenerator(name = "app_generator", sequenceName = "userSeq")
        open var id: Long,
        open var name: String,
        open var email: String,
        open var username: String ,
        open var password: String,
        @ManyToMany
        open var roles: MutableList<RoleDAO>
) {
    constructor() : this(0, "", "", "", "", mutableListOf<RoleDAO>()) {

    }

    constructor(role:String) : this(0, "", "", "", "", mutableListOf<RoleDAO>(RoleDAO(role))) {

    }

}


package com.iadi.grants.services.dao

import javax.persistence.*

@Entity
data class RoleDAO(
        @Id @GeneratedValue(generator = "app_generator")
        @SequenceGenerator(name="app_generator", sequenceName = "roleSeq")
        var id: Long = 0,
        var role:String

) {
    constructor(role: String):this(0,role) {}
    constructor() : this(0,"ADMIN") {}
}
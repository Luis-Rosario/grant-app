package com.iadi.grants.api.data

import com.iadi.grants.services.dao.EvaluationDAO
import com.iadi.grants.services.dao.GrantDAO
import com.iadi.grants.services.dao.GrantResponseDAO
import com.iadi.grants.services.dao.StudentDAO
import java.time.LocalDate

data class ReviewerDTO(
        val id: Long,
        var address: String,
        var birthDate: LocalDate,
        var institutionId: Long,
        var name: String,
        var email: String,
        var evals: List<Long>,
        var username: String,
        var password: String,
        var roles:   List<String>
) {

    constructor() : this(0,"",LocalDate.now(),0,"","", mutableListOf<Long>(),
            "","",mutableListOf<String>()) {

    }


}
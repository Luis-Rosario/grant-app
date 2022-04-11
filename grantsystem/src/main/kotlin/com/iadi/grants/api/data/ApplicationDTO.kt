package com.iadi.grants.api.data

import java.time.LocalDate


data class ApplicationDTO (val applicationID:Long,
                           var submissionDate:LocalDate,
                           var status:Status,
                           var responses: List<GrantResponse>,
                           var grantID:Long,
                           var studentID: Long, var reviews:List<EvaluationDTO>) {

}
package com.iadi.grants.api.data

import java.time.LocalDate

data class ApplicationSafeDTO(val applicationID:Long,
                              var submissionDate: LocalDate,
                              var status:Status,
                              var grantID:Long,
                              var studentID: Long, var reviews:List<EvaluationDTO>
) {
}
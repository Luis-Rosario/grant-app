package com.iadi.grants.api.data

import java.time.LocalDate

data class SafeReviewerDTO(val id: Long,
                           var address: String,
                           var birthDate: LocalDate,
                           var institutionId: Long,
                           var name: String,
                           var email: String,
                           var evals: List<Long>,
                           var username: String
                           ) {
}
package com.iadi.grants.api.data

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import java.time.LocalDate

data class SafeStudentDTO(val id:Long,
                          var address:String,
                          @JsonDeserialize(using = LocalDateDeserializer::class)
                          @JsonSerialize(using = LocalDateSerializer::class)
                          var birthDate: LocalDate,
                          var institutionId:Long,
                          var name:String,
                          var email:String,
                          var course:String,
                          var cvId:Long,
                          var applications:  List<Long>,
                          var username: String
                          ) {
}
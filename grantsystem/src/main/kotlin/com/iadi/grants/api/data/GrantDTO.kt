package com.iadi.grants.api.data


import java.time.LocalDate



data class GrantDTO( val grantId:Long,
                     var title:String,
                     var openingDate:LocalDate,
                     var deadline: LocalDate,
                     var description:String,
                     var funding:Int,
                     var applicationQuestions: List<GrantQuestion>,
                     var applications: Int,
                     var sponsorId:Long,
                     var sponsorName:String){


}

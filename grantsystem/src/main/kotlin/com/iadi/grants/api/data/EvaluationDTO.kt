package com.iadi.grants.api.data


data class EvaluationDTO(val id:Long,
                         var status:Boolean,
                         var revId: Long,
                         var applicationId:Long,
                         var grantId:Long,
                         var textField:String) {
}
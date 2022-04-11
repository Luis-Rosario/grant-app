package com.iadi.grants.api.data

data class SafeSponsorDTO(val sponsorID: Long,
                          var name: String,
                          var email: String,
                          var phoneNumber: String,
                          var username: String
                         ){
}
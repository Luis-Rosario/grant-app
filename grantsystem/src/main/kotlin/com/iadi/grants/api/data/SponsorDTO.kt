package com.iadi.grants.api.data

data class SponsorDTO(val sponsorID: Long,
                      var name: String,
                      var email: String,
                      var phoneNumber: String,
                      var username: String,
                      var password: String,
                      var roles:   List<String>) {

}
package com.iadi.grants

import com.iadi.grants.model.InstitutionRepository
import com.iadi.grants.model.RoleRepository
import com.iadi.grants.model.UserRepository
import com.iadi.grants.services.dao.*
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@SpringBootApplication
class GrantsApplication {

    @Bean
    fun init(roles: RoleRepository,insts:InstitutionRepository,usrs:UserRepository) = CommandLineRunner {
        val admin = RoleDAO(1, "ADMIN")
        val student = RoleDAO(2, "STUDENT")
        val reviewer = RoleDAO(3, "REVIEWER")
        val sponsor = RoleDAO(4, "SPONSOR")
        val list = mutableListOf(admin, student, reviewer, sponsor)
        roles.saveAll(list)
      val inst = InstitutionDAO(1,"TCF","email","999", mutableListOf<StudentDAO>(), mutableListOf<ReviewerDAO>())
         insts.save(inst)

        val u = UserDAO(1,"","","a",BCryptPasswordEncoder().encode("pw"), mutableListOf(admin))
        usrs.save(u)
    }
}
fun main(args: Array<String>) {
    runApplication<GrantsApplication>(*args)

}

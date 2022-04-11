package com.iadi.grants.services.services

import com.iadi.grants.model.GrantRepository
import com.iadi.grants.model.RoleRepository
import com.iadi.grants.model.SponsorRepository
import com.iadi.grants.model.UserRepository
import com.iadi.grants.services.Exceptions.AlreadyExistsException
import com.iadi.grants.services.dao.GrantDAO
import com.iadi.grants.services.dao.SponsorDAO
import com.iadi.grants.services.Exceptions.NotFoundException
import com.iadi.grants.services.dao.RoleDAO
import com.iadi.grants.services.dao.StudentDAO

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*



@Service
class SponsorService(val sponsorRep: SponsorRepository, val grants:GrantRepository,
                     val users:UserRepository,val roles: RoleRepository) {

    @Transactional
    fun createSponsor(sponsor: SponsorDAO) {
        if(sponsorRep.existsByName(sponsor.name)) {
            throw AlreadyExistsException("Sponsor already exists")
        }else{

            if(users.existsByUsername(sponsor.username)){
                throw AlreadyExistsException("Sponsor with the given username ${sponsor.username} already exists in the system.")
            }

            sponsor.id=0
            val encoder = BCryptPasswordEncoder()


            var rolesList = mutableListOf<RoleDAO>()

            for(role in sponsor.roles){
                val fRole = roles.findByRole(role.role)
                if(fRole.isPresent )
                    rolesList.add(fRole.get())
                else
                    throw NotFoundException("Role was not found")
            }

            sponsor.password=encoder.encode(sponsor.password)
            sponsor.roles=rolesList
            users.save(sponsor)
            sponsorRep.save(sponsor)}
    }

    fun getSponsors(username: Optional<String>): List<SponsorDAO> {
        if(username.isPresent) {
            return sponsorRep.findByUsernameContaining(username.get()).orElseThrow {
                NotFoundException("Sponsor with that keyword ${username.get()} were not found")
            }
        }
        else {
            return sponsorRep.findAll().toList()
        }
    }


    fun getSponsor(sponsorId:Long): SponsorDAO {
        return sponsorRep.findById(sponsorId).orElseThrow{
            NotFoundException("Sponsor with $sponsorId was not found")
        }
    }


    fun getGrantsFromSponsor(id: Long): List<GrantDAO> {
        sponsorRep.findById(id).orElseThrow{   NotFoundException("Sponsor with $id was not found")}
        return grants.findBySponsorId(id)
    }

    @Transactional
    fun updateSponsor(sponsor: SponsorDAO) {
        val oldSponsor = getSponsor(sponsor.id)

        oldSponsor.email=sponsor.email
        oldSponsor.name=sponsor.name
        oldSponsor.phoneNumber=sponsor.phoneNumber

        sponsorRep.save(oldSponsor)
    }

    @Transactional
    fun deleteSponsor(sponsorId: Long): SponsorDAO {
        var oldSponsor = sponsorRep.findById(sponsorId).orElseThrow{
            NotFoundException("Sponsor with $sponsorId was not found")
        }
        sponsorRep.deleteById(sponsorId)
        return oldSponsor
    }
}
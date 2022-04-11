package com.iadi.grants.api.controller


import com.iadi.grants.api.data.SafeGrantDTO
import com.iadi.grants.api.data.SafeSponsorDTO
import com.iadi.grants.api.data.SponsorDTO
import io.swagger.annotations.*
import org.springframework.web.bind.annotation.*
import java.util.*

@RequestMapping("/sponsor")
interface SponsorAPI {

    @ApiOperation(value = "Create a Sponsor.", consumes = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 201, message = "Successfully created a Sponsor."),
        ApiResponse(code = 401, message = "Not Authorized"),
        ApiResponse(code = 409, message = "Sponsor already in the System.")
    ])
    @PostMapping("")
    fun createSponsor(@RequestBody sponsor:SponsorDTO)

    ///////////////////////////////////////////////////////////////////////////

    @ApiOperation(value = "Return all Sponsors.", produces = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Success."),
        ApiResponse(code = 401, message = "Not Authorized")
    ])
    @GetMapping("")
    fun getSponsors(@RequestParam(required = false, name = "username") username:Optional<String>): List<SafeSponsorDTO>

    ///////////////////////////////////////////////////////////////////////////

    @ApiOperation(value = "Return a Sponsor.", produces = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Success."),
        ApiResponse(code = 401, message = "Not Authorized"),
        ApiResponse(code = 404, message = "Sponsor does not exist")
    ])
    @GetMapping("/{id}")
    fun getSponsor(@PathVariable id:Long): SponsorDTO

    //////////////////////////////////////////////////////////////////////////

    @ApiOperation(value = "Return all Grants of a Sponsor.", produces = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Success."),
        ApiResponse(code = 401, message = "Not Authorized"),
        ApiResponse(code = 404, message = "Resource does not exist")
    ])
    @GetMapping("/{id}/grant")
    fun getGrantSponsor(@PathVariable id: Long):List<SafeGrantDTO>

    ///////////////////////////////////////////////////////////////////////////

    @ApiOperation(value = "Update a Sponsor.", consumes = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully updated Sponsor."),
        ApiResponse(code = 401, message = "Not Authorized"),
        ApiResponse(code = 404, message = "Resource does not exist")
    ])
    @PutMapping("")
    fun updateSponsor(@RequestBody sponsor: SafeSponsorDTO)

    /////////////////////////////////////////////////////////////////////////


    @ApiOperation(value = "Delete a Sponsor.", produces = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Success."),
        ApiResponse(code = 401, message = "Not Authorized"),
        ApiResponse(code = 404, message = "Sponsor does not exist.")
    ])
    @DeleteMapping("/{id}")
    fun deleteSponsor(@PathVariable id: Long): SponsorDTO

    ////////////////////////////////////////////////////////////////////////
}
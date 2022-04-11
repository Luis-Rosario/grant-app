package com.iadi.grants.api.controller

import com.iadi.grants.api.data.*
import io.swagger.annotations.*
import org.springframework.web.bind.annotation.*
import java.util.*


@RequestMapping("/grant")
interface GrantAPI {

    @ApiOperation(value="Create a grant.", consumes = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 201, message = "Successfully created Grant."),
        ApiResponse(code = 401, message = "Not Authorized."),
        ApiResponse(code = 409, message = "Grant already exists.")
    ])
    @PostMapping("")
    fun createGrant(@RequestBody grant:GrantDTO)

    //////////////////////////////////////////////////////////////////////


    @ApiOperation(value="Return all Grants", produces = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Success."),
        ApiResponse(code = 401, message = "Not Authorized.")
    ])
    @GetMapping("")
    fun getGrants(@RequestParam(required = false,name = "name") name: Optional<String>): List<GrantDTO>

    //////////////////////////////////////////////////////////////////////

    @ApiOperation(value="Return a Grant.", produces = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Success."),
        ApiResponse(code = 401, message = "Not Authorized."),
        ApiResponse(code = 404, message = "Grant does not exist.")
    ])
    @GetMapping( "/{id}")
    fun getGrant(@PathVariable id:Long): GrantDTO

    ////////////////////////////////////////////////////////////////

    @ApiOperation(value="Return all applications for a Grant.", produces = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Success."),
        ApiResponse(code = 401, message = "Not Authorized."),
        ApiResponse(code = 404, message = "Resource does not exist.")
    ])
    @GetMapping("{id}/application")
    fun getGrantApplications(@PathVariable id: Long,@RequestParam(required = false,name = "status") status: Optional<Status>): List<ApplicationSafeDTO>

    ///////////////////////////////////////////////////////////////



    @ApiOperation(value="Returns the Panel info for the Grant.", produces = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Success."),
        ApiResponse(code = 401, message = "Not Authorized."),
        ApiResponse(code = 404, message = "Resource does not exist.")
    ])
    @GetMapping("/{id}/panel")
    fun getPanel(@PathVariable id: Long): PanelDTO

    /////////////////////////////////////////////////////////////

    @ApiOperation(value="Update Grant.", consumes = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully updated Grant."),
        ApiResponse(code = 401, message = "Not Authorized."),
        ApiResponse(code = 404, message = "Grant does not exist.")
    ])
    @PutMapping("")
    fun updateGrant(@RequestBody grant: GrantDTO)

    //////////////////////////////////////////////////


    @ApiOperation(value = "Delete a Grant.", produces = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Success."),
        ApiResponse(code = 401, message = "Not Authorized"),
        ApiResponse(code = 404, message = "Resource does not exist")
    ])
    @DeleteMapping("/{id}")
    fun deleteGrant(@PathVariable id: Long): GrantDTO

    @ApiOperation(value="Returns all evaluations of a given grant.", produces = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Success."),
        ApiResponse(code = 401, message = "Not Authorized.")
    ])
    @GetMapping("/{appId}/evaluations")
    fun getGrantEvaluations(@PathVariable appId:Long): List<EvaluationDTO>

}
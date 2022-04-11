package com.iadi.grants.api.controller

import com.iadi.grants.api.data.*
import io.swagger.annotations.*
import org.springframework.web.bind.annotation.*

@RequestMapping("/institution")
interface InstitutionAPI {

    @ApiOperation(value="Register an institution in the platform.", consumes = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 201, message = "Successfully registered institution."),
        ApiResponse(code = 401, message = "Not Authorized."),
        ApiResponse(code = 409, message = "Institution already exists.")
    ])
    @PostMapping("")
    fun registerInstitution(@RequestBody institution: InstitutionDTO)


    @ApiOperation(value="Return all institutions.", produces = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Success."),
        ApiResponse(code = 401, message = "Not Authorized.")
    ])
    @GetMapping("")
    fun getInstitutions():List<InstitutionDTO>


    @ApiOperation(value="Return a requested institution.", produces = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Success."),
        ApiResponse(code = 401, message = "Not Authorized."),
        ApiResponse(code = 404, message = "Institution does not exist.")
    ])
    @GetMapping( "/{id}")
    fun getInstitution(@PathVariable id:Long): InstitutionDTO


    @ApiOperation(value="Return all students of a given institution.", produces = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Success."),
        ApiResponse(code = 401, message = "Not Authorized."),
        ApiResponse(code = 404, message = "The given institution has no students enrolled in the platform.")
    ])
    @GetMapping( "/{id}/students")
    fun getInstitutionStudents(@PathVariable id:Long) :List<SafeStudentDTO>


    @ApiOperation(value="Return all reviewers of a given institution.", produces = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Success."),
        ApiResponse(code = 401, message = "Not Authorized."),
        ApiResponse(code = 404, message = "The given institution has no reviewers enrolled in the platform.")
    ])
    @GetMapping( "/{id}/reviewers")
    fun getInstitutionReviewers(@PathVariable id:Long): List<SafeReviewerDTO>


    @ApiOperation(value="Update the information of a given institution", consumes = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Success."),
        ApiResponse(code = 401, message = "Not Authorized."),
        ApiResponse(code = 404, message = "Institution does not exist.")
    ])
    @PutMapping( "")
    fun updateInstitution( institution: InstitutionDTO)


    @ApiOperation(value="Delete an institution from the platform", produces = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Success."),
        ApiResponse(code = 401, message = "Not Authorized."),
        ApiResponse(code = 404, message = "Institution does not exist.")
    ])
    @DeleteMapping("/{id}")
    fun deleteInstitution(@PathVariable id:Long): InstitutionDTO

}
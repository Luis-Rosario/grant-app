package com.iadi.grants.api.controller

import com.iadi.grants.api.data.ApplicationDTO
import com.iadi.grants.api.data.EvaluationDTO
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.web.bind.annotation.*

@RequestMapping("/application")
interface ApplicationAPI {


    @ApiOperation(value="Returns the info of an application.", produces = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Success."),
        ApiResponse(code = 401, message = "Not Authorized."),
        ApiResponse(code = 404, message = "Application Not Found")
    ])
    @GetMapping("/{appId}")
    fun getApplication(@PathVariable appId: Long): ApplicationDTO

    @ApiOperation(value="Returns all applications.", produces = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Success."),
        ApiResponse(code = 401, message = "Not Authorized.")
    ])
    @GetMapping("")
    fun getAllApplication(): List<ApplicationDTO>


    @ApiOperation(value="Create a Application for a Grant.", consumes = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 201, message = "Successfully created Application."),
        ApiResponse(code = 401, message = "Not Authorized."),
        ApiResponse(code = 404, message = "Resource does not exist.")
    ])
    @PostMapping("")
    fun createApplication(@RequestBody app: ApplicationDTO)


    @ApiOperation(value = "Update Application.", consumes = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully updated Application."),
        ApiResponse(code = 401, message = "Not Authorized."),
        ApiResponse(code = 404, message = "Resource does not exist.")
    ])
    @PutMapping("")
    fun updateApplication(@RequestBody app: ApplicationDTO)


    @ApiOperation(value = "Delete an Application.", produces = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Success."),
        ApiResponse(code = 401, message = "Not Authorized"),
        ApiResponse(code = 404, message = "Resource does not exist")
    ])
    @DeleteMapping("/{appId}")
    fun deleteApplication(@PathVariable appId:Long): ApplicationDTO

    @ApiOperation(value="Returns all evaluations of a given Application.", produces = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Success."),
        ApiResponse(code = 401, message = "Not Authorized.")
    ])
    @GetMapping("/{appId}/evaluations")
    fun getApplicationEvaluations(@PathVariable appId:Long): List<EvaluationDTO>

}
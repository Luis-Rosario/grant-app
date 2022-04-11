package com.iadi.grants.api.controller


import com.iadi.grants.api.data.EvaluationDTO
import com.iadi.grants.api.data.PanelDTO
import com.iadi.grants.api.data.ReviewerDTO
import com.iadi.grants.api.data.SafeReviewerDTO
import io.swagger.annotations.*
import org.springframework.web.bind.annotation.*
import java.util.*

@RequestMapping("/reviewer")
interface ReviewerAPI {

    @ApiOperation(value="Create a Reviewer.",consumes = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 201, message = "Successfully created a Reviewer."),
        ApiResponse(code = 401, message = "Not Authorized."),
        ApiResponse(code = 409, message = "Reviewer already in thr System.")
    ])
    @PostMapping("")
    fun createReviewer(@RequestBody reviewer:ReviewerDTO)

    //////////////////////////////////////////////////////////////////////

    @ApiOperation(value="Return all Reviewers.",produces = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Success."),
        ApiResponse(code = 401, message = "Not Authorized."),
        ApiResponse(code = 404, message = "No Reviewers in the system.")
    ])
    @GetMapping("")
    fun getReviewers(@RequestParam(required = false, name = "name") name:Optional<String>,
                     @RequestParam(required = false, name = "email") email:Optional<String>,
                     @RequestParam(required = false, name = "username") username:Optional<String>):List<SafeReviewerDTO>

    //////////////////////////////////////////////////////////////////////

    @ApiOperation(value="Return a Reviewer.",produces = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Success."),
        ApiResponse(code = 401, message = "Not Authorized."),
        ApiResponse(code = 404, message = "Reviewer does not exist.")
    ])
    @GetMapping( "/{id}")
    fun getReviewer(@PathVariable id:Long):ReviewerDTO

    ////////////////////////////////////////////////////////////////

    @ApiOperation(value="Return all evaluations of a Reviewer",produces = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Success."),
        ApiResponse(code = 401, message = "Not Authorized."),
        ApiResponse(code = 404, message = "Resource does not exist.")
    ])
    @GetMapping("{id}/evaluation")
    fun getReviewerEvaluations(@PathVariable id: Long):List<EvaluationDTO>

    @ApiOperation(value="Return all panels of a Reviewer",produces = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Success."),
        ApiResponse(code = 401, message = "Not Authorized."),
        ApiResponse(code = 404, message = "Resource does not exist.")
    ])
    @GetMapping("{id}/panels")
    fun getReviewerPanels(@PathVariable id: Long):List<PanelDTO>

    ///////////////////////////////////////////////////////////////

    @ApiOperation(value = "Update Reviewer info.",consumes = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully updated Reviewer."),
        ApiResponse(code = 401, message = "Not Authorized."),
        ApiResponse(code = 404, message = "Resource does not exist.")])
    @PutMapping("")
    fun updateReviewer(@RequestBody reviewer:SafeReviewerDTO)

    ///////////////////////////////////////////////////////////////

    @ApiOperation(value="Delete a Reviewer.",produces = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Success."),
        ApiResponse(code = 401, message = "Not Authorized."),
        ApiResponse(code = 404, message = "Reviewer does not exist.")
    ])
    @DeleteMapping( "/{id}")
    fun deleteReviewer(@PathVariable id:Long):ReviewerDTO


}


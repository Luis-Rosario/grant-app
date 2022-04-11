package com.iadi.grants.api.controller

import com.iadi.grants.api.data.EvaluationDTO
import io.swagger.annotations.*
import org.springframework.web.bind.annotation.*
import java.util.*

@RequestMapping("/eval")
interface EvaluationAPI {

    @ApiOperation(value="Create a evaluation.", consumes = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 201, message = "Successfully create evaluation."),
        ApiResponse(code = 401, message = "Not Authorized."),
        ApiResponse(code = 409, message = "Evaluation already exists.")
    ])
    @PostMapping("")
    fun createEvaluation(@RequestBody evaluation: EvaluationDTO)

    @ApiOperation(value="Return all evaluations.", produces = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Success."),
        ApiResponse(code = 401, message = "Not Authorized."),
        ApiResponse(code = 404, message = "Evaluation doesn't exist.")
    ])    @GetMapping("")
    fun getAllEvaluations(@RequestParam(required = false,name = "grantId") grantId: Optional<Long>,
                          @RequestParam(required = false,name = "appId") applicationId: Optional<Long>):List<EvaluationDTO>

    @ApiOperation(value="Return the requested evaluation.", produces = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Success."),
        ApiResponse(code = 401, message = "Not Authorized."),
        ApiResponse(code = 404, message = "Evaluation doesn't exist.")
    ])
    @GetMapping( "/{id}")
    fun getEvaluation(@PathVariable id:Long): EvaluationDTO

    @ApiOperation(value="Update evaluation information.", consumes = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Success."),
        ApiResponse(code = 401, message = "Not Authorized."),
        ApiResponse(code = 404, message = "Evaluation doesn't exist.")
    ])
    @PutMapping("")
    fun updateEvaluation(@RequestBody evaluation:EvaluationDTO)

    @ApiOperation(value="Delete a evaluation.", produces = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Success."),
        ApiResponse(code = 401, message = "Not Authorized."),
        ApiResponse(code = 404, message = "Evaluation doesn't exist.")
    ])
    @DeleteMapping("/{id}")
    fun deleteEvaluation(@PathVariable id:Long):EvaluationDTO

}
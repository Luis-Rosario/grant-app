package com.iadi.grants.api.controller

import com.iadi.grants.api.data.*
import io.swagger.annotations.*
import org.springframework.web.bind.annotation.*
import java.util.*


@RequestMapping("/student")
interface StudentAPI {

    @ApiOperation(value="Enroll a student in the platform.", consumes = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 201, message = "Successfully enrolled student."),
        ApiResponse(code = 401, message = "Not Authorized."),
        ApiResponse(code = 409, message = "Student already exists.")
    ])
    @PostMapping("")
    fun registerStudent(@RequestBody student: StudentDTO)

    //////////////////////////////////////////////////////////////////////

    @ApiOperation(value="Return all enrolled students.", produces = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Success."),
        ApiResponse(code = 401, message = "Not Authorized."),
        ApiResponse(code = 404, message = "No students exist.")
    ])
    @GetMapping("")
    fun getAllStudents(@RequestParam(required = false,name = "name") name:Optional<String>,
                       @RequestParam(required = false,name = "email") email:Optional<String>,
                       @RequestParam(required = false,name = "username") username:Optional<String>):List<SafeStudentDTO>

    //////////////////////////////////////////////////////////////////////

    @ApiOperation(value="Return the requested student.", produces = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Success."),
        ApiResponse(code = 401, message = "Not Authorized."),
        ApiResponse(code = 404, message = "Student doesn't exist.")
    ])
    @GetMapping( "/{id}")
    fun getStudent(@PathVariable id:Long): SafeStudentDTO


    //////////////////////////////////////////////////////////////////////

    @ApiOperation(value="Return a student's grant applications.", produces = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Success."),
        ApiResponse(code = 401, message = "Not Authorized."),
        ApiResponse(code = 404, message = "Student doesn't exist.")
    ])
    @GetMapping( "/{id}/applications")
    fun getStudentApplications(@PathVariable id:Long): List<ApplicationDTO>

    //////////////////////////////////////////////////////////////////////

    @ApiOperation(value="Update student information.", consumes = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Success."),
        ApiResponse(code = 401, message = "Not Authorized."),
        ApiResponse(code = 404, message = "Student doesn't exist.")
    ])
    @PutMapping("")
    fun  updateStudent(@RequestBody student:SafeStudentDTO)

    //////////////////////////////////////////////////////////////////////


    @ApiOperation(value="Delete a student.", produces = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Success."),
        ApiResponse(code = 401, message = "Not Authorized."),
        ApiResponse(code = 404, message = "Student doesn't exist.")
    ])
    @DeleteMapping("/{id}")
    fun deleteStudent(@PathVariable id:Long): StudentDTO

    //////////////////////////////////////////////////////////////////////


    @ApiOperation(value="Get a Student's CV", produces = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Sucess."),
        ApiResponse(code = 401, message = "Not Authorized."),
        ApiResponse(code = 404, message = "Student doesn't exist.")
    ])
    @GetMapping("/{id}/CV")
    fun getCV(@PathVariable id:Long):CV

    @ApiOperation(value="Create a Student's CV", consumes = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 201, message = "Success."),
        ApiResponse(code = 401, message = "Not Authorized."),
        ApiResponse(code = 404, message = "Student doesn't exist.")
    ])
    @PostMapping("/{id}/CV")
    fun createCV(@PathVariable id:Long,@RequestBody cv:CV)

    @ApiOperation(value="Change a Student's CV", consumes = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Success."),
        ApiResponse(code = 401, message = "Not Authorized."),
        ApiResponse(code = 404, message = "Student doesn't exist.")
    ])
    @PutMapping("/{id}/CV")
    fun updateCV(@PathVariable id:Long,@RequestBody cv:CV)

    @ApiOperation(value="Delete a Student's CV", produces = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Success."),
        ApiResponse(code = 401, message = "Not Authorized."),
        ApiResponse(code = 404, message = "Student doesn't exist.")
    ])
    @DeleteMapping("/{id}/CV")
    fun deleteCV(@PathVariable id:Long):CV

}

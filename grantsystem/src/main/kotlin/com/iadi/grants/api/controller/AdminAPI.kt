package com.iadi.grants.api.controller


import com.iadi.grants.api.data.RoleDTO
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.web.bind.annotation.*

@RequestMapping("/admin")
interface AdminAPI {


    @ApiOperation(value="Returns the info of a Role.", produces = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Success."),
        ApiResponse(code = 401, message = "Not Authorized."),
        ApiResponse(code = 404, message = "Role Not Found")
    ])
    @GetMapping("/{roleId}")
    fun getRole(@PathVariable roleId: Long): RoleDTO

    @ApiOperation(value="Returns the Roles for an User", produces = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Success."),
        ApiResponse(code = 401, message = "Not Authorized."),
        ApiResponse(code = 404, message = "User Not Found")
    ])
    @GetMapping("/user/{userName}")
    fun getUserRoles(@PathVariable userName: String): List<RoleDTO>



    @ApiOperation(value="Returns all roles.", produces = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Success."),
        ApiResponse(code = 401, message = "Not Authorized.")
    ])
    @GetMapping("")
    fun getAllRoles(): List<RoleDTO>


    @ApiOperation(value="Create a role.", consumes = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 201, message = "Successfully created role."),
        ApiResponse(code = 401, message = "Not Authorized."),
        ApiResponse(code = 404, message = "Resource does not exist.")
    ])
    @PostMapping("")
    fun createRole(@RequestBody role: RoleDTO)


    @ApiOperation(value = "Update role.", consumes = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully updated role."),
        ApiResponse(code = 401, message = "Not Authorized."),
        ApiResponse(code = 404, message = "Resource does not exist.")
    ])
    @PutMapping("")
    fun updateRole(@RequestBody role: RoleDTO)


    @ApiOperation(value = "Delete a Role.", produces = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Success."),
        ApiResponse(code = 401, message = "Not Authorized"),
        ApiResponse(code = 404, message = "Resource does not exist")
    ])
    @DeleteMapping("/{role}")
    fun deleteRole(@PathVariable role:Long): RoleDTO

}
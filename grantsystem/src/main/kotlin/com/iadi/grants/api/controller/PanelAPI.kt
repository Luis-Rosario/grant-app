package com.iadi.grants.api.controller


import com.iadi.grants.api.data.PanelDTO
import io.swagger.annotations.*
import org.springframework.web.bind.annotation.*


@RequestMapping("/panel")
interface PanelAPI {

    @ApiOperation(value="Create a Panel.",consumes = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 201, message = "Successfully created a Reviewer."),
        ApiResponse(code = 401, message = "Not Authorized."),
        ApiResponse(code = 409, message = "Panel already in the System.")
    ])
    @PostMapping("")
    fun createPanel(@RequestBody panel: PanelDTO)


    @ApiOperation(value="Return all Panels",produces = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Success."),
        ApiResponse(code = 401, message = "Not Authorized.")
    ])
    @GetMapping("")
    fun getPanels():List<PanelDTO>


    @ApiOperation(value="Return a Panel.",produces = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Success."),
        ApiResponse(code = 401, message = "Not Authorized."),
        ApiResponse(code = 404, message = "Panel does not exist.")
    ])
    @GetMapping( "/{id}")
    fun getPanel(@PathVariable id:Long):PanelDTO


    @ApiOperation(value = "Update Panel info",consumes = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully updated Panel"),
        ApiResponse(code = 401, message = "Not Authorized"),
        ApiResponse(code = 404, message = "Resource does not exist")])
    @PutMapping("/{id}")
    fun updatePanel(@PathVariable id:Long,@RequestBody panel:PanelDTO)


    @ApiOperation(value = "Delete a Panel.", produces = "application/json")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Success."),
        ApiResponse(code = 401, message = "Not Authorized."),
        ApiResponse(code = 404, message = "Resource does not exist.")
    ])
    @DeleteMapping("/{id}")
    fun deletePanel(@PathVariable id:Long):PanelDTO
}
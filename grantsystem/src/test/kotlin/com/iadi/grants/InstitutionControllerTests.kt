package com.iadi.grants

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.iadi.grants.api.data.InstitutionDTO
import com.iadi.grants.services.Exceptions.NotFoundException
import com.iadi.grants.services.dao.CvDAO
import com.iadi.grants.services.dao.InstitutionDAO
import com.iadi.grants.services.dao.StudentDAO
import com.iadi.grants.services.services.InstitutionService
import junit.framework.Assert.assertEquals
import org.hamcrest.Matchers.hasSize
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate

@RunWith(SpringRunner::class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class InstitutionControllerTests {

    @Autowired
    lateinit var mvc: MockMvc

    @MockBean
    lateinit var service:InstitutionService

    companion object {


        val FCT = InstitutionDAO(1,"FCT","fct","99", mutableListOf(), mutableListOf())
        val John = StudentDAO(1,"", LocalDate.now(),FCT,"JOHN","","", CvDAO(), mutableListOf(),"","", mutableListOf())
        val IST = InstitutionDAO(2,"IST","","", mutableListOf(), mutableListOf())
        val insts = listOf(FCT, IST)

        val instsDTO = insts.map {  InstitutionDTO(it.id,it.name,it.email,it.phoneNumb/*,it.students.map { it.id },it.reviewers.map { it.id }*/)}

        val url = "http://localhost:8080/institution"

    }

    val mapper: ObjectMapper = ObjectMapper().registerModule(KotlinModule())

    @Test
    @WithMockUser(username = "admin", password = "pw",roles =["ADMIN"])
    fun `1 GET ONE INST`(){
        Mockito.`when`(service.getInstitution(1)).thenReturn(FCT)

        val result = mvc.perform(get("$url/1")).andExpect(status().isOk).andReturn()

        val responseString = result.response.contentAsString

        assertEquals(mapper.readValue<InstitutionDTO>(responseString), instsDTO[0])

    }

    @Test
    @WithMockUser(username = "admin", password = "pw",roles =["ADMIN"])
    fun `2 GET ONE INST NOT FOUND()`(){
        Mockito.`when`(service.getInstitution(2)).thenThrow(NotFoundException("Not Found"))

        mvc.perform(get("$url/2")).andExpect(status().is4xxClientError)
    }

    @Test
    @WithMockUser(username = "admin", password = "pw",roles =["ADMIN"])
    fun `3 GET ALL INSTS`(){
        Mockito.doReturn(insts).`when`(service).getInstitutions()

        var result = mvc.perform(get("$url"))
        result.andExpect(status().isOk).andExpect(jsonPath("$",hasSize<Any>(instsDTO.size)))
    }






}
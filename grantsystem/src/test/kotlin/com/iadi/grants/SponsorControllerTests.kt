package com.iadi.grants

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.iadi.grants.api.data.SponsorDTO
import com.iadi.grants.services.dao.SponsorDAO
import com.iadi.grants.services.services.SponsorService
import org.junit.Assert
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import kotlin.test.assertEquals
import com.iadi.grants.services.Exceptions.NotFoundException
import org.hamcrest.Matchers.hasSize
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SponsorControllerTests {

    @Autowired
    lateinit var mvc: MockMvc

    @MockBean
    lateinit var service: SponsorService

    companion object {
        val sponsor1 = SponsorDAO(1, "Sponsor1", "", "","","", mutableListOf())
        val sponsor2 = SponsorDAO(0, "Sponsor2", "", "","","", mutableListOf())

        val sponsorEmpty = mutableListOf<SponsorDAO>()
        val sps = listOf(sponsor1, sponsor2)

        val spDTO = sps.map {SponsorDTO(it.id, it.name, it.email, it.phoneNumber,it.username,it.password, mutableListOf())}

        val url = "http://localhost:8080/sponsor"
    }

    val mapper: ObjectMapper = ObjectMapper().registerModule(KotlinModule())

    @Test
    @WithMockUser(username = "admin", password = "pw",roles =["ADMIN"])
    fun `GET ONE SPONSOR`() {
        Mockito.`when`(service.getSponsor(1)).thenReturn(sponsor1)

        val result = mvc.perform(get("$url/1")).andExpect(status().isOk).andReturn()

        val responseString = result.response.contentAsString

        val sp = mapper.readValue<SponsorDTO>(responseString)

        Assert.assertEquals(sp, spDTO[0])
    }

    @Test
    @WithMockUser(username = "admin", password = "pw",roles =["ADMIN"])
    fun `GET ONE SPONSOR NOT FOUND`() {
        Mockito.`when`(service.getSponsor(2)).thenThrow(NotFoundException("Not Found"))

        mvc.perform(get("$url/2")).andExpect(status().is4xxClientError)
    }

    @Test
    @WithMockUser(username = "admin", password = "pw",roles =["ADMIN"])
    fun `GET ALL SPONSORS`() {
        Mockito.`when`(service.getSponsors(Optional.empty())).thenReturn(listOf(sponsor1, sponsor2))

        var result = mvc.perform(get("$url"))
        result.andExpect(status().isOk).andExpect(jsonPath("$",hasSize<Any>(spDTO.size)))
    }

    @Test
    @WithMockUser(username = "admin", password = "pw",roles =["ADMIN"])
    fun `ADD SPONSOR`() {
        val sp1 = mapper.writeValueAsString(spDTO[0])

        Mockito.`when`(service.createSponsor(sponsor1))
                .then { assertEquals(it.getArgument(0), sponsor1)}

        mvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(sp1)).andExpect(status().isCreated)
    }
}
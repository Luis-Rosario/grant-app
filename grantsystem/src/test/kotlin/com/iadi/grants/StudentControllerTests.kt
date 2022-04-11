package com.iadi.grants
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.iadi.grants.api.data.*
import com.iadi.grants.model.RoleRepository
import com.iadi.grants.model.StudentRepository
import com.iadi.grants.model.UserRepository
import com.iadi.grants.services.Exceptions.AlreadyExistsException
import com.iadi.grants.services.Exceptions.NotFoundException
import com.iadi.grants.services.dao.*
import com.iadi.grants.services.services.StudentService
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
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class StudentControllerTests {

    @Autowired
    lateinit var mvc: MockMvc


    @MockBean
    lateinit var service:StudentService

    @MockBean
    lateinit var stdRep:StudentRepository

    @MockBean
    lateinit var userRep:UserRepository

    @MockBean
    lateinit var roleRep:RoleRepository


    companion object {

        val sponsor = SponsorDAO(0, "rebook", "mail", "989989898","sp","pw", mutableListOf())
        val grant = GrantDAO(0, "asd", LocalDate.now(), LocalDate.now(), "upsy", 69420, listOf(), mutableListOf<ApplicationDAO>(), sponsor)


        val studEmpty = mutableListOf<StudentDAO>()

        val cvJ = CvDAO(mutableListOf(FieldDAO(true,"CURSO","MIEI")))

        val gr1 = GrantResponseDAO(GrantQuestionDAO(),"gosto")




        val FCT = InstitutionDAO(1,"FCT","fct","99", mutableListOf(), mutableListOf())
        val John = StudentDAO(1,"", LocalDate.now(),FCT,"JOHN","j@j","", cvJ, mutableListOf(),"JOHN","j", mutableListOf(RoleDAO(1,"STUDENT")))
        val JOAO = StudentDAO(1,"", LocalDate.now(),FCT,"JOAO","J@¯","", cvJ, mutableListOf(),"JOAO","J", mutableListOf(RoleDAO(1,"STUDENT")))


        val IST = InstitutionDAO(2,"IST","ist","22", mutableListOf(), mutableListOf())
        val insts = listOf(FCT, IST)
        val studs = listOf(John, JOAO)

        val cvs = listOf(cvJ)

        val app1 = ApplicationDAO(0, LocalDate.now(), Status.SUBMITTED, mutableListOf<GrantResponseDAO>(gr1), grant, John, emptyList())

        val instsDTO = insts.map {  InstitutionDTO(it.id,it.name,it.email,it.phoneNumb/*,it.students.map { it.id },it.reviewers.map { it.id }*/)}
        val studsDTO = studs.map {  StudentDTO(it.id,it.address,it.birthDate,it.institution.id,it.name,it.email,it.course,it.cv.id,it.applications.map { it.applicationID },
                it.username,it.password, mutableListOf())}


        val studsSafeDTO = studs.map {  SafeStudentDTO(it.id,it.address,it.birthDate,it.institution.id,it.name,it.email,it.course,it.cv.id,it.applications.map { it.applicationID },
                it.username)}

        val cvsDTO = cvs.map { CV(it.id,it.fields.map { Field(it.mandatory,it.type,it.content) }) }

        val url = "http://localhost:8080/student"

    }

    val mapper: ObjectMapper = ObjectMapper().registerModule(KotlinModule())


    /***************    GET ONE     ***************/

    @Test
    @WithMockUser(username = "admin", password = "pw",roles =["ADMIN"])
    fun `GET ONE STUDENT`(){
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        Mockito.`when`(service.getStudent(1)).thenReturn(John)

        val result = mvc.perform(get("$url/1")).andExpect(status().isOk).andReturn()

        val responseString = result.response.contentAsString

        val stud = mapper.readValue<SafeStudentDTO>(responseString)

        val safeJohn = John.let {SafeStudentDTO(it.id,it.address,it.birthDate,it.institution.id,it.name,it.email,it.course,it.cv.id,it.applications.map
        { it.applicationID }
                ,it.username)  }


        assertEquals(stud, safeJohn)

    }

    @Test
    @WithMockUser(username = "JOHN", password = "j",roles =["STUDENT"])
    fun `GET ONE STUDENT AUTH_STUDENT`(){
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        Mockito.`when`(service.getStudent(1)).thenReturn(John)

        Mockito.`when`(stdRep.findById(1)).thenReturn(Optional.of(John))

        val result = mvc.perform(get("$url/1")).andExpect(status().isOk).andReturn()

        val responseString = result.response.contentAsString

        val stud = mapper.readValue<SafeStudentDTO>(responseString)

        val safeJohn = John.let {SafeStudentDTO(it.id,it.address,it.birthDate,it.institution.id,it.name,it.email,it.course,it.cv.id,it.applications.map
        { it.applicationID }
                ,it.username)  }

        assertEquals(stud, safeJohn)

    }

    @Test
    fun `GET ONE STUDENT UNAUTH`(){

        Mockito.`when`(stdRep.findById(1)).thenReturn(Optional.of(JOAO))

        val result = mvc.perform(get("$url/1")).andExpect(status().isUnauthorized)

    }

    @Test
    @WithMockUser(username = "admin", password = "pw",roles =["ADMIN"])
    fun `GET ONE STUDENT NOT FOUND()`(){
        Mockito.`when`(service.getStudent(2)).thenThrow(NotFoundException("Not Found"))

        mvc.perform(get("$url/2")).andExpect(status().is4xxClientError)
    }

    /***************    GET ALL     ***************/

    @Test
    @WithMockUser(username = "admin", password = "pw",roles =["ADMIN"])
    fun `GET ALL STUDENTS`(){
        Mockito.doReturn(studs).`when`(service).getAllStudents(Optional.empty(), Optional.empty(), Optional.empty())

        var result = mvc.perform(get("$url"))
        result.andExpect(status().isOk).andExpect(jsonPath("$",hasSize<Any>(studsDTO.size)))
    }

    @Test
    @WithMockUser(username = "admin", password = "pw",roles =["ADMIN"])
    fun `GET ALL STUDENTS WITH EMAIL`(){
        Mockito.doReturn(studs).`when`(service).getAllStudents(Optional.empty(), Optional.of("@"), Optional.empty())

        var result = mvc.perform(get("$url").param("email","@"))
        result.andExpect(status().isOk).andExpect(jsonPath("$",hasSize<Any>(studsDTO.size)))
    }

    @Test
    @WithMockUser(username = "admin", password = "pw",roles =["ADMIN"])
    fun `GET ALL STUDENTS WITH Name`(){
        Mockito.doReturn(studs).`when`(service).getAllStudents(Optional.of("J"), Optional.empty(), Optional.empty())

        var result = mvc.perform(get("$url").param("name","J"))
        result.andExpect(status().isOk).andExpect(jsonPath("$",hasSize<Any>(studsDTO.size)))
    }

    @Test
    @WithMockUser(username = "admin", password = "pw",roles =["ADMIN"])
    fun `GET ALL STUDENTS WITH EMAIL and Name`(){
        Mockito.doReturn(listOf(JOAO)).`when`(service).getAllStudents(Optional.of("A"), Optional.of("@"), Optional.empty())

        var result = mvc.perform(get("$url").param("email","@").param("name","A"))
        result.andExpect(status().isOk).andExpect(jsonPath("$",hasSize<Any>(1)))
    }

    @Test
    @WithMockUser(username = "admin", password = "pw",roles =["ADMIN"])
    fun `GET ALL STUDENTS WITH Name Not Found`(){
        Mockito.doThrow(NotFoundException()).`when`(service).getAllStudents(Optional.of("J"), Optional.empty(), Optional.empty())

        var result = mvc.perform(get("$url").param("name","J"))
        result.andExpect(status().is4xxClientError)
    }

    @Test
    fun `GET ALL STUDENTS WITH Name UNAUTH`(){
        Mockito.doThrow(NotFoundException()).`when`(service).getAllStudents(Optional.of("J"), Optional.empty(), Optional.empty())

        mvc.perform(get("$url").param("name","J")).andExpect(status().isUnauthorized)
    }

    @Test
    @WithMockUser(username = "admin", password = "pw",roles =["STUDENT"])
    fun `GET ALL STUDENTS WITH Name FORBIDDEN`(){
        Mockito.doThrow(NotFoundException()).`when`(service).getAllStudents(Optional.of("J"), Optional.empty(), Optional.empty())

        mvc.perform(get("$url").param("name","J")).andExpect(status().isForbidden)
    }

    @Test
    @WithMockUser(username = "admin", password = "pw",roles =["REVIEWER"])
    fun `GET ALL STUDENTS WITH Name REVAUTH`(){
        Mockito.doReturn(studs).`when`(service).getAllStudents(Optional.empty(), Optional.empty(), Optional.empty())

        var result = mvc.perform(get("$url"))
        result.andExpect(status().isOk).andExpect(jsonPath("$",hasSize<Any>(studsDTO.size)))
    }

    /***************    ADD ONE     ***************/

    @Test
    @WithMockUser(username = "admin", password = "pw",roles =["ADMIN"])
    fun `ADD STUDENT`() {

        val stdJ = mapper.writeValueAsString(studsDTO[0])

        Mockito.`when`(service.registerStudent(John))
                .then { assertEquals(it.getArgument(0), John) }

        mvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(stdJ)).andExpect(status().isCreated)
    }


    /***************    GET STUDENT APPS     ***************/

    @Test
    @WithMockUser(username = "admin", password = "pw",roles =["ADMIN"])
    fun `GET STUDENT APPLICATIONS`() {

        Mockito.`when`(service.getStudentApplications(1)).thenReturn(listOf(app1))

        mvc.perform(get(url+"/1/applications")).andExpect(status().isOk).andExpect(jsonPath("$", hasSize<Any>(1)))
    }

    @Test
    @WithMockUser(username = "JOHN", password = "j",roles =["STUDENT"])
    fun `GET STUDENT APPLICATIONS STAUTH`() {

        Mockito.`when`(stdRep.findById(1)).thenReturn(Optional.of(John))

        Mockito.`when`(service.getStudentApplications(1)).thenReturn(listOf(app1))

        mvc.perform(get(url+"/1/applications")).andExpect(status().isOk).andExpect(jsonPath("$", hasSize<Any>(1)))
    }

    @Test
    @WithMockUser(username = "sp", password = "pw",roles =["SPONSOR"])
    fun `GET STUDENT APPLICATIONS SPAUTH`() {

        Mockito.`when`(service.getStudentApplications(1)).thenReturn(listOf(app1))

        mvc.perform(get(url+"/1/applications")).andExpect(status().isForbidden)
    }

    @Test
    @WithMockUser(username = "Joao", password = "J",roles =["STUDENT"])
    fun `GET STUDENT APPLICATIONS FORBIDDEN`() {

        Mockito.`when`(stdRep.findById(1)).thenReturn(Optional.of(JOAO))

        Mockito.`when`(service.getStudentApplications(1)).thenReturn(listOf(app1))

        mvc.perform(get(url+"/1/applications")).andExpect(status().isForbidden)
    }

    @Test
    fun `GET STUDENT APPLICATIONS UNAUTH`() {

        Mockito.`when`(service.getStudentApplications(1)).thenReturn(listOf(app1))

        mvc.perform(get(url+"/1/applications")).andExpect(status().isUnauthorized)
    }

    /***************    GET STUDENT's CV     ***************/

    @Test
    @WithMockUser(username = "admin", password = "pw",roles =["ADMIN"])
    fun `GET STUDENT CV`() {

        val newCV = CvDAO(1)

        val newCVS = CV(newCV.id,newCV.fields.map { Field(it.mandatory,it.type,it.content) })

        Mockito.doReturn(newCV).`when`(service).getCv(1)

        val cvS = mvc.perform(get(url+"/1/CV")).andExpect(status().isOk).andReturn().response.contentAsString

        val cv = mapper.readValue<CV>(cvS)

        assertEquals(cv, newCVS)

    }

    @Test
    @WithMockUser(username = "admin", password = "pw",roles =["ADMIN"])
    fun `GET STUDENT CV Not found`() {


        Mockito.doThrow(NotFoundException()).`when`(service).getCv(1)

        mvc.perform(get(url+"/1/CV")).andExpect(status().is4xxClientError)


    }

    @Test
    @WithMockUser(username = "John", password = "j",roles =["STUDENT"])
    fun `GET STUDENT CV STAUTH`() {

        Mockito.`when`(stdRep.findById(1)).thenReturn(Optional.of(John))

        val newCV = CvDAO(1)

        val newCVS = CV(newCV.id,newCV.fields.map { Field(it.mandatory,it.type,it.content) })

        Mockito.doReturn(newCV).`when`(service).getCv(1)

        val cvS = mvc.perform(get(url+"/1/CV")).andExpect(status().isOk).andReturn().response.contentAsString

        val cv = mapper.readValue<CV>(cvS)

        assertEquals(cv, newCVS)

    }

    @Test
    fun `GET STUDENT CV UNAUTH`() {

        mvc.perform(get(url+"/1/CV")).andExpect(status().isUnauthorized)

    }

    @Test
    @WithMockUser(username = "admin", password = "pw",roles =["REVIEWER"])
    fun `GET STUDENT CV REVAUTH`() {

        val newCV = CvDAO(1)

        val newCVS = CV(newCV.id,newCV.fields.map { Field(it.mandatory,it.type,it.content) })

        Mockito.doReturn(newCV).`when`(service).getCv(1)

        val cvS = mvc.perform(get(url+"/1/CV")).andExpect(status().isOk).andReturn().response.contentAsString

        val cv = mapper.readValue<CV>(cvS)

        assertEquals(cv, newCVS)

    }


    /***************    UPDATE STUDENT     ***************/

    @Test
    @WithMockUser(username = "admin", password = "pw",roles =["ADMIN"])
    fun `CHANGE STUDENT`() {

        val stdJ = mapper.writeValueAsString(studsDTO[1])

        Mockito.`when`(service.updateStudent(JOAO))
                .then { assertEquals(it.getArgument(0), JOAO) }

        mvc.perform(put(url).contentType(MediaType.APPLICATION_JSON).content(stdJ)).andExpect(status().isOk)
    }

    @Test
    @WithMockUser(username = "JOAO", password = "pw",roles =["Student"])
    fun `CHANGE STUDENT STAUTH`() {

        val stdJ = mapper.writeValueAsString(studsSafeDTO[1])

        Mockito.doReturn(Optional.of(JOAO)).`when`(stdRep).findById(1)

        Mockito.`when`(service.updateStudent(JOAO))
                .then { assertEquals(it.getArgument(0), JOAO) }

        mvc.perform(put(url).contentType(MediaType.APPLICATION_JSON).content(stdJ)).andExpect(status().isOk)
    }

    @Test
    @WithMockUser(username = "JOHN", password = "pw",roles =["Student"])
    fun `CHANGE STUDENT STAUTH FORBIDDEN`() {

        val stdJ = mapper.writeValueAsString(studsDTO[1])

        Mockito.doReturn(Optional.of(JOAO)).`when`(stdRep).findById(1)

        Mockito.`when`(service.updateStudent(JOAO))
                .then { assertEquals(it.getArgument(0), JOAO) }

        mvc.perform(put(url).contentType(MediaType.APPLICATION_JSON).content(stdJ)).andExpect(status().isForbidden)
    }

    @Test
    fun `CHANGE STUDENT STAUTH UNAUTH`() {

        val stdJ = mapper.writeValueAsString(studsDTO[1])

        Mockito.doReturn(Optional.of(JOAO)).`when`(stdRep).findById(1)

        Mockito.`when`(service.updateStudent(JOAO))
                .then { assertEquals(it.getArgument(0), JOAO) }

        mvc.perform(put(url).contentType(MediaType.APPLICATION_JSON).content(stdJ)).andExpect(status().isUnauthorized)
    }


    /***************    ADD STUDENT CV    ***************/

    @Test
    @WithMockUser(username = "admin", password = "pw",roles =["ADMIN"])
    fun `ADD STUDENT CV`() {

        val cvString = mapper.writeValueAsString(cvsDTO[0])

        Mockito.`when`(service.createCV(1, cvs[0]))
                .then { assertEquals(it.getArgument(1), cvs[0]) }

        mvc.perform(post(url+"/1/CV").contentType(MediaType.APPLICATION_JSON).content(cvString)).andExpect(status().isCreated)
    }

    @Test
    @WithMockUser(username = "admin", password = "pw",roles =["ADMIN"])
    fun `ADD STUDENT CV conflit`() {

        val cvString = mapper.writeValueAsString(cvsDTO[0])

        Mockito.`when`(service.createCV(1, cvs[0])).thenThrow(AlreadyExistsException())

        mvc.perform(post(url+"/1/CV").contentType(MediaType.APPLICATION_JSON).content(cvString)).andExpect(status().isConflict)
    }

    @Test
    @WithMockUser(username = "JOHN", password = "pw",roles =["STUDENT"])
    fun `ADD STUDENT CV STAUTH`() {

        val cvString = mapper.writeValueAsString(cvsDTO[0])

        Mockito.doReturn(Optional.of(John)).`when`(stdRep).findById(1)

        Mockito.`when`(service.createCV(1, cvs[0]))
                .then { assertEquals(it.getArgument(1), cvs[0]) }

        mvc.perform(post(url+"/1/CV").contentType(MediaType.APPLICATION_JSON).content(cvString)).andExpect(status().isCreated)
    }

    @Test
    @WithMockUser(username = "JOHN", password = "pw",roles =["STUDENT"])
    fun `ADD STUDENT CV STAUTH FORBIDDEN`() {

        val cvString = mapper.writeValueAsString(cvsDTO[0])

        Mockito.doReturn(Optional.of(JOAO)).`when`(stdRep).findById(1)

        Mockito.`when`(service.createCV(1, cvs[0]))
                .then { assertEquals(it.getArgument(1), cvs[0]) }

        mvc.perform(post(url+"/1/CV").contentType(MediaType.APPLICATION_JSON).content(cvString)).andExpect(status().isForbidden)
    }

    /***************    UPDATE STUDENT CV    ***************/

    @Test
    @WithMockUser(username = "JOHN", password = "pw",roles =["STUDENT"])
    fun `CHANGE STUDENT CV STAUTH`() {

        Mockito.doReturn(Optional.of(John)).`when`(stdRep).findById(1)

        val cv = CvDAO(mutableListOf(FieldDAO(true,"CURSO","MIEI")))

        cv.fields[0].content="NOVO"

        val cvString = mapper.writeValueAsString(cv)

        Mockito.`when`(service.createCV(1, cv))
                .then { assertEquals(it.getArgument(1), cv) }

        mvc.perform(put(url+"/1/CV").contentType(MediaType.APPLICATION_JSON).content(cvString)).andExpect(status().isOk)
    }

    @Test
    @WithMockUser(username = "admin", password = "pw",roles =["ADMIN"])
    fun `CHANGE STUDENT CV`() {

        val cv = CvDAO(mutableListOf(FieldDAO(true,"CURSO","MIEI")))

        cv.fields[0].content="NOVO"

        val cvString = mapper.writeValueAsString(cv)

        Mockito.`when`(service.createCV(1, cv))
                .then { assertEquals(it.getArgument(1), cv) }

        mvc.perform(put(url+"/1/CV").contentType(MediaType.APPLICATION_JSON).content(cvString)).andExpect(status().isOk)
    }

    @Test
    @WithMockUser(username = "JOHN", password = "pw",roles =["STUDENT"])
    fun `CHANGE STUDENT CV STAUTH FORBIDDEN`() {

        Mockito.doReturn(Optional.of(JOAO)).`when`(stdRep).findById(1)

        val cv = CvDAO(mutableListOf(FieldDAO(true,"CURSO","MIEI")))

        cv.fields[0].content="NOVO"

        val cvString = mapper.writeValueAsString(cv)

        Mockito.`when`(service.createCV(1, cv))
                .then { assertEquals(it.getArgument(1), cv) }

        mvc.perform(put(url+"/1/CV").contentType(MediaType.APPLICATION_JSON).content(cvString)).andExpect(status().isForbidden)
    }

    /***************    DELETE STUDENT CV    ***************/

    @Test
    @WithMockUser(username = "admin", password = "pw",roles =["ADMIN"])
    fun `DELETE STUDENT CV`() {

        Mockito.`when`(service.deleteCV(1)).thenReturn(cvs[0])

        val result = mvc.perform(delete(url+"/1/CV")).andExpect(status().isOk).andReturn().response.contentAsString

        val cv = mapper.readValue<CV>(result)

        assertEquals(cv, cvsDTO[0])
    }

    @Test
    @WithMockUser(username = "JOHN", password = "j",roles =["STUDENT"])
    fun `DELETE STUDENT CV STAUTH`() {

        Mockito.doReturn(Optional.of(John)).`when`(stdRep).findById(1)

        Mockito.`when`(service.deleteCV(1)).thenReturn(cvs[0])

        val result = mvc.perform(delete(url+"/1/CV")).andExpect(status().isOk).andReturn().response.contentAsString

        val cv = mapper.readValue<CV>(result)

        assertEquals(cv, cvsDTO[0])
    }





}
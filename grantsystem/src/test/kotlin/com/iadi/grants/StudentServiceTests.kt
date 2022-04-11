package com.iadi.grants

import com.iadi.grants.api.data.Status
import com.iadi.grants.model.*
import com.iadi.grants.services.Exceptions.AlreadyExistsException
import com.iadi.grants.services.Exceptions.NotFoundException
import com.iadi.grants.services.dao.*
import com.iadi.grants.services.services.StudentService

import org.junit.Assert
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner

import java.time.LocalDate
import java.util.*
import kotlin.test.assertEquals


@RunWith(SpringRunner::class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ActiveProfiles("test")
class StudentServiceTests {

    @Autowired
    lateinit var stud: StudentService
    @MockBean
    lateinit var studMock: StudentRepository
    @MockBean
    lateinit var instMock: InstitutionRepository



    companion object {

        val sponsor = SponsorDAO(0,"rebook","mail","989989898","","", mutableListOf())
        val grant = GrantDAO(0,  "asd", LocalDate.now(), LocalDate.now(), "upsy", 69420,   listOf() , mutableListOf<ApplicationDAO>(), sponsor)
        val FCT = InstitutionDAO(0,"FCT","","", mutableListOf(), mutableListOf())
        val IST = InstitutionDAO(0,"IST","","", mutableListOf(), mutableListOf())

        val cvJ = CvDAO(mutableListOf<FieldDAO>())

        val john = StudentDAO(0,"Lisboa", LocalDate.now(), FCT,"JOHN","a@jphn.com","Physics", cvJ, mutableListOf(),"","", mutableListOf())
        val mary = StudentDAO(0,"Faro", LocalDate.now(), IST,"MARY","a@mary.com","Nursing", CvDAO(), mutableListOf(),"","", mutableListOf())

        val studEmpty = mutableListOf<StudentDAO>()


        val gr1 = GrantResponseDAO(GrantQuestionDAO(),"gosto")

        val app1 = ApplicationDAO(0,LocalDate.now(), Status.DRAFT, mutableListOf<GrantResponseDAO>(gr1), grant, john, emptyList())
    }

    @Test
    fun `1_getAllEmpty`(){
        Mockito.`when`(studMock.findAll()).thenReturn(studEmpty)

        Assert.assertEquals(stud.getAllStudents(Optional.empty(), Optional.empty(), Optional.empty()), studEmpty)
    }

    @Test
    fun `Get All`(){
        Mockito.`when`(studMock.findAll()).thenReturn(listOf(john,mary))

        Assert.assertTrue(stud.getAllStudents(Optional.empty(), Optional.empty(), Optional.empty()).size==2 )
    }

    @Test
    fun `Get All With Email`(){
        Mockito.`when`(studMock.findByEmailContaining("jmp")).thenReturn(Optional.of(listOf(john)))

        Assert.assertTrue(stud.getAllStudents(Optional.empty(), Optional.of("jmp"), Optional.empty()).size==1 )
    }

    @Test
    fun `Get All With Email2`(){
        Mockito.`when`(studMock.findByEmailContaining("@")).thenReturn(Optional.of(listOf(john,mary)))

        Assert.assertTrue(stud.getAllStudents(Optional.empty(), Optional.of("@"), Optional.empty()).size==2 )
    }

    @Test(expected = NotFoundException::class)
    fun `Get All With Email Not Found`(){
        stud.getAllStudents(Optional.empty(), Optional.of("jmp"), Optional.empty())
    }

    @Test
    fun `Get All With Name`(){
        Mockito.`when`(studMock.findByNameContaining("John")).thenReturn(Optional.of(listOf(john)))

        Assert.assertTrue(stud.getAllStudents(Optional.of("John"),Optional.empty(), Optional.empty()).size==1 )
    }

    @Test(expected = NotFoundException::class)
    fun `Get All With Name Not Found`(){
        stud.getAllStudents(Optional.of("John"), Optional.empty(), Optional.empty())
    }

    @Test
    fun `Get All With Email and Name`(){
        Mockito.`when`(studMock.findByNameContainingAndEmailContaining("John","jmp")).thenReturn(Optional.of(listOf(john)))

        Assert.assertTrue(stud.getAllStudents(Optional.of("John"), Optional.of("jmp"), Optional.empty()).size==1 )
    }

    @Test(expected = NotFoundException::class)
    fun `Get All With Email and Name Not Found`(){

        stud.getAllStudents(Optional.of("John"), Optional.of("jmp"), Optional.empty())
    }

    @Test(expected = NotFoundException::class)
    fun `Get One NotFound`(){
        Mockito.`when`(studMock.findById(1)).thenReturn(Optional.empty())

        stud.getStudent(1)
    }

    @Test
    fun `Get Student`(){
        Mockito.`when`(studMock.findById(1)).thenReturn(Optional.of(john))

        val s = stud.getStudent(1)

        assertEquals(s, john)
    }


    @Test(expected = NotFoundException::class)
    fun `Add One Student InstituitionNotFound`(){
        stud.registerStudent(john)
    }


    @Test(expected = AlreadyExistsException::class)
    fun `Add One AlreadyExists`() {
        Mockito.`when`(studMock.existsByEmail(john.email)).thenReturn(true)
        stud.registerStudent(john)
    }

    @Test(expected = NotFoundException::class)
    fun `Update One No Inst`() {
        Mockito.doReturn(false).`when`(instMock).existsById(1)
        stud.updateStudent(john)
    }


    @Test
    fun `Delete Student`() {
        Mockito.`when`(studMock.findById(1)).thenReturn(Optional.of(john))
        val res = stud.deleteStudent(1)
        Assert.assertEquals(res, john)
    }

    @Test(expected = NotFoundException::class)
    fun `Delete Student Not Found`() {
        val res = stud.deleteStudent(1)
        Assert.assertEquals(res, john)
    }

    @Test(expected = AlreadyExistsException::class)
    fun `Add CV AlreadyExists`() {
        john.cv.fields.add(FieldDAO(true,"CURSO","MIEI"))
        Mockito.`when`(studMock.findById(1)).thenReturn(Optional.of(john))
        stud.createCV(1, cvJ)
    }

    @Test
    fun `Delete CV`() {
        Mockito.`when`(studMock.findById(1)).thenReturn(Optional.of(john))
        val res = stud.deleteCV(1)
        Assert.assertEquals(res, cvJ)
    }

    @Test(expected = NotFoundException::class)
    fun `Delete CV Not Found`() {
        Mockito.`when`(studMock.findById(1)).thenReturn(Optional.of(mary))
        stud.deleteCV(1)
    }








}
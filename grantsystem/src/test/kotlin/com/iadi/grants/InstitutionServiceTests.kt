package com.iadi.grants


import com.iadi.grants.model.InstitutionRepository
import com.iadi.grants.services.Exceptions.AlreadyExistsException
import com.iadi.grants.services.Exceptions.NotFoundException
import com.iadi.grants.services.dao.CvDAO
import com.iadi.grants.services.dao.InstitutionDAO
import com.iadi.grants.services.dao.StudentDAO
import com.iadi.grants.services.services.InstitutionService
import org.junit.Assert.*
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

@RunWith(SpringRunner::class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ActiveProfiles("test")
class InstitutionServiceTests {


    @Autowired
    lateinit var inst: InstitutionService

    @MockBean
    lateinit var instRep: InstitutionRepository


    companion object {
        val FCT = InstitutionDAO(1,"FCT","fct","99", mutableListOf(), mutableListOf())
        val IST = InstitutionDAO(1,"IST","","", mutableListOf(), mutableListOf())
        val john = StudentDAO(1,"", LocalDate.now(), FCT,"JOHN","","",CvDAO(), mutableListOf(),"","", mutableListOf())
    }

    @Test
    fun `1_getAllEmpty`(){
        Mockito.doReturn(emptyList<InstitutionDAO>()).`when`(instRep).findAll()
        assertEquals(inst.getInstitutions(), emptyList<InstitutionDAO>())
    }


    @Test(expected = NotFoundException::class)
    fun `2_getOneNotFound`(){
        Mockito.doThrow(NotFoundException()).`when`(instRep).findById(1)
        inst.getInstitution(1)
    }



    @Test(expected = AlreadyExistsException::class)
    fun `5_addOneAlreadyExists`(){
        Mockito.doThrow(AlreadyExistsException())
                .`when`(instRep).existsByNameAndAndEmailAndPhoneNumb(FCT.name, FCT.email,FCT.phoneNumb)
        inst.addInstitution(FCT)
    }

    @Test
    fun `6_getOneByName`(){
        Mockito.doReturn(Optional.of(FCT)).`when`(instRep).findByName("FCT")
        var fct = inst.getInstituitionByName("FCT")
        assertEquals(fct,FCT)
    }

    @Test(expected = NotFoundException::class)
    fun `7_getOneByNameException`(){
        inst.getInstituitionByName("FCT")
    }
/*
    @Test
    fun `8_updateInst`(){
        inst.addInstitution(FCT)
        FCT.name="FCT-1"
        inst.updateInstitution(FCT)
        assertEquals(inst.getInstitution(1).name,"FCT-1")
    }

    @Test
    fun `9_deleteOneAfterAdd`(){
        inst.addInstitution(FCT)
        var fct = inst.deleteInstitution(1)
        assertEquals(inst.getInstitutions(), emptyList<InstitutionDAO>())
    }

    @Test(expected = NotFoundException::class)
    fun `10_deleteOneNotFound`(){
        inst.deleteInstitution(1)
    }*/











}

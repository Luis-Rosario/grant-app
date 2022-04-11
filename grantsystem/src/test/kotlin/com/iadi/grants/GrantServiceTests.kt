package com.iadi.grants

import com.iadi.grants.api.data.Status
import com.iadi.grants.model.*
import com.iadi.grants.services.Exceptions.AlreadyExistsException
import com.iadi.grants.services.Exceptions.NotFoundException
import com.iadi.grants.services.dao.*
import com.iadi.grants.services.services.GrantService
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



@SpringBootTest
@RunWith(SpringRunner::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ActiveProfiles("test")
class GrantServiceTests {

    @Autowired
    lateinit var grants:GrantService

    @MockBean
    lateinit var grMock: GrantRepository

    @MockBean
    lateinit var spMock: SponsorRepository


    companion object {
        val sponsor1 = SponsorDAO(1, "Sponsor1", "", "","","",mutableListOf())

        val q1 = GrantQuestionDAO("1", "", true)
        val q2 = GrantQuestionDAO("2", "", true)

        val grant1 = GrantDAO(1, "First Grant", LocalDate.now(), LocalDate.now(), "", 0, listOf(q1, q2), mutableListOf<ApplicationDAO>(), sponsor1)

        val FCT = InstitutionDAO(1,"FCT","fct","99", mutableListOf(), mutableListOf())

        val IST = InstitutionDAO(2,"IST","","", mutableListOf(), mutableListOf())
        val john = StudentDAO(1,"", LocalDate.now(), FCT,"JOHN","","",CvDAO(), mutableListOf(),"","",mutableListOf())

        val r1 = GrantResponseDAO(q1,"OLA")
        val appl = ApplicationDAO(1, LocalDate.now(), Status.SUBMITTED, mutableListOf(r1), grant1, john, emptyList())

        val rev1 = ReviewerDAO(1, "Rua do Monte", LocalDate.now(), FCT, "João Seco", "js@gmail.com", mutableListOf<EvaluationDAO>(),"","",mutableListOf())

        val pan = PanelDAO(1, rev1, mutableListOf(rev1), grant1)

    }

    @Test
    fun `1_getAllEmpty`(){
        Assert.assertEquals(grants.getGrants(Optional.empty()), emptyList<GrantDAO>())
    }

    @Test
    fun `getAllByName`(){
        Mockito.doReturn(Optional.of(listOf(grant1))).`when`(grMock).findByTitleContaining("F")
        Assert.assertEquals(grants.getGrants(Optional.of("F")), listOf(grant1))
    }

    @Test(expected = NotFoundException::class)
    fun `getAllByName Not Found`(){
        grants.getGrants(Optional.of("F"))
    }

    @Test(expected = NotFoundException::class)
    fun `2_getOneNotFound`(){
        grants.getGrant(1)
    }

    @Test(expected = NotFoundException::class)
    fun `5_addOneSponsorNotFound`(){
        Mockito.doThrow(NotFoundException()).`when`(spMock).findById(grant1.sponsor.id)
        grants.addGrant(grant1)
    }

    @Test(expected = AlreadyExistsException::class)
    fun `6_addOneAlreadyExists`(){
        Mockito.doReturn(Optional.of(sponsor1)).`when`(spMock).findById(grant1.sponsor.id)
        Mockito.`when`(grMock.existsByTitleAndDescription(grant1.title, grant1.description)).thenReturn(true)
        grants.addGrant(grant1)
    }






}
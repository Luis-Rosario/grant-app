package com.iadi.grants

import com.iadi.grants.model.SponsorRepository
import com.iadi.grants.services.Exceptions.NotFoundException
import com.iadi.grants.services.dao.SponsorDAO
import com.iadi.grants.services.services.SponsorService
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
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ActiveProfiles("test")
class SponsorServiceTests {

    @Autowired
    lateinit var sp: SponsorService

    @MockBean
    lateinit var spMock: SponsorRepository

    companion object {
        val sponsor1 = SponsorDAO(0, "Sponsor1", "", "","","", mutableListOf())
        val sponsor2 = SponsorDAO(0, "Sponsor2", "", "","","", mutableListOf())

        val sponsorEmpty = mutableListOf<SponsorDAO>()
    }

    @Test
    fun `1_getAllEmpty`(){
        Mockito.`when`(spMock.findAll()).thenReturn(sponsorEmpty)

        assertEquals(sp.getSponsors(Optional.empty()), sponsorEmpty)
    }

    @Test(expected = NotFoundException::class)
    fun `2_getOneNotFound`(){
        Mockito.`when`(spMock.findById(1)).thenThrow(NotFoundException())
        sp.getSponsor(1)
    }

    /*@Test
    fun `4_addOne`(){
        sp.createSponsor(sponsor1)
        var sp1 = sp.getSponsor(1)
        assertEquals(sp1.name, sponsor1.name)
    }*/

    /*@Test(expected = AlreadyExistsException::class)
    fun `5_addOneAlreadyExists`(){
        Mockito.`when`(spMock.existsByName(sponsor1.name)).thenReturn(true)
        sp.createSponsor(sponsor1)
    }*/

    /*@Test
    fun `6_getOneById`(){
        Mockito.`when`(spMock.findById(1)).thenReturn(Optional.of(sponsor1))

        val sponsor = sp.getSponsor(1)

        assertEquals(sponsor, sponsor1)
    }*/

    /*@Test(expected = NotFoundException::class)
    fun `7_getOneByNameException`(){
        sp.getSponsor(1)
    }*/

    /*@Test
    fun `8_updateInst`(){
        sp.createSponsor(sponsor1)
        sponsor1.name="NEWSponsor"
        sp.updateSponsor(sponsor1)
        assertEquals(sp.getSponsor(1).name, "NEWSponsor")
    }*/

   /* @Test
    fun `9_deleteOneAfterAdd`(){
        sp.createSponsor(sponsor1)
        var sp1 = sp.deleteSponsor(1)
        assertEquals(sp.getSponsors(), emptyList<SponsorDAO>())
    }

    @Test(expected = NotFoundException::class)
    fun `10_deleteOneNotFound`(){
        sp.deleteSponsor(1)
    }*/




}

package com.iadi.grants.services.dao


import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate
import javax.persistence.*

@Entity
data class StudentDAO(
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_generator")
        @SequenceGenerator(name = "app_generator", sequenceName = "userSeq")
        override var id: Long,
        var address: String,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        var birthDate: LocalDate,
        @ManyToOne
        var institution: InstitutionDAO,
        override var name:String,
        override var email:String,
        var course: String,
        @OneToOne(cascade = [CascadeType.ALL])
        var cv: CvDAO,
        @OneToMany(mappedBy = "student", cascade = [CascadeType.ALL])
        var applications: MutableList<ApplicationDAO>,
        override var username: String,
        override var password: String,
        @ManyToMany
        override var roles: MutableList<RoleDAO>
) : UserDAO(id, name, email, username, password,  roles)
{
   constructor() : this(0, "",LocalDate.now(), InstitutionDAO(),"", "","",CvDAO(), mutableListOf<ApplicationDAO>(),"","",mutableListOf<RoleDAO>
   (RoleDAO("STUDENT"))){


}

    constructor(role:String) : this(0, "",LocalDate.now(), InstitutionDAO(),"", "","",CvDAO(), mutableListOf<ApplicationDAO>(),"","",mutableListOf<RoleDAO>
    (RoleDAO("STUDENT"),RoleDAO(role)))

}
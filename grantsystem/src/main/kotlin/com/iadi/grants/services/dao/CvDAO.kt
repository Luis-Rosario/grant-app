package com.iadi.grants.services.dao

import javax.persistence.*

@Entity
data class CvDAO(
        @OneToMany(cascade = [CascadeType.ALL])
        var fields: MutableList<FieldDAO>
)
 {
     constructor() : this(mutableListOf<FieldDAO>()) {

     }
     constructor(id:Long):this(mutableListOf<FieldDAO>()){
         this.id=id
     }


     @Id @GeneratedValue(generator = "app_generator")
     @SequenceGenerator(name="app_generator", sequenceName = "cvSeq")
     var id: Long = 0
}
package com.iadi.grants.api.data

import javax.persistence.Tuple


data class PanelDTO(val panelID:Long,
                    var panelChair: Long,
                    val reviewer:List<IdAndName>, val grant:Long) {

}
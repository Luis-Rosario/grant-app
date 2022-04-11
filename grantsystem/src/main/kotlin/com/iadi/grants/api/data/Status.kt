package com.iadi.grants.api.data

enum class Status(val stat:String) {
    DRAFT("draft"),
    SUBMITTED("submitted"),
    GRANTED("granted"),
    NOT_GRANTED("not_granted")
}
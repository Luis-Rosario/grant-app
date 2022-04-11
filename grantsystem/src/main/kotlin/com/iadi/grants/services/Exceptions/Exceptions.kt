package com.iadi.grants.services.Exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.RuntimeException

@ResponseStatus(HttpStatus.NOT_FOUND,reason = "Not Found")
class NotFoundException(message:String = "Not Found") : RuntimeException(message)

@ResponseStatus(HttpStatus.CONFLICT,reason = "Already Exists")
class AlreadyExistsException(message: String="Already Exists"):RuntimeException(message)


@ResponseStatus(HttpStatus.NOT_FOUND)
class UsernameNotFoundException(message: String="Username not found"):RuntimeException(message)

@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED,reason = "Not Allowed")
class NotAllowedException(message: String="Not Allowed"):RuntimeException(message)


@ResponseStatus(HttpStatus.BAD_REQUEST,reason = "Panel has chairman that isn't in the reviewer list")
class InvalidPanelException(message: String="Panel has chairman that isn't in the reviewer list"):RuntimeException(message)


@ResponseStatus(HttpStatus.UNAUTHORIZED,reason = "Not logged in")
class UnauthorizedException(message: String="Not logged in"):RuntimeException(message)

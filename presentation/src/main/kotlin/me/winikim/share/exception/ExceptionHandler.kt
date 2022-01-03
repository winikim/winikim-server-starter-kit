package me.winikim.share.exception

import me.winikim.share.doamin.exception.BadRequestException
import me.winikim.share.doamin.exception.ConflictException
import me.winikim.share.doamin.exception.NotFoundException
import me.winikim.share.doamin.exception.UnauthorizedException
import org.apache.logging.log4j.LogManager
import org.hibernate.exception.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import java.lang.RuntimeException

@RestControllerAdvice
class ExceptionHandler {
    private val log = LogManager.getLogger()

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun methodArgumentNotValidExceptionHandler(e: MethodArgumentTypeMismatchException): WinikimExceptionResponse {
        log.error("[MethodArgumentTypeMismatchException] message = ${e.message}")
        return WinikimExceptionResponse(
            code = "bad_request",
            message = e.message ?: "잘못 된 요청 입니다.",
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun methodArgumentNotValidExceptionHandler(e: MethodArgumentNotValidException): WinikimExceptionResponse {
        log.error("[methodArgumentNotValidExceptionHandler] message = ${e.message}")
        return WinikimExceptionResponse(
            code = "bad_request",
            message = e.bindingResult.fieldError?.defaultMessage ?: "알 수 없는 에러",
        )
    }

    @ExceptionHandler(NotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun notFoundExceptionHandler(e: NotFoundException): WinikimExceptionResponse {
        log.error("[존재하지 않음] message = ${e.message}")
        return WinikimExceptionResponse(
            code = e.code,
            message = e.message,
        )
    }


    @org.springframework.web.bind.annotation.ExceptionHandler(BadRequestException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun badRequestExceptionHandler(e: BadRequestException): WinikimExceptionResponse {
        log.error("[잘못 된 요청] message = ${e.message}")
        return WinikimExceptionResponse(
            code = e.code,
            message = e.message,
        )
    }


    @org.springframework.web.bind.annotation.ExceptionHandler(ConflictException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun conflictExceptionHandler(e: ConflictException): WinikimExceptionResponse {
        log.error("[중복 예와] message = ${e.message}")
        return WinikimExceptionResponse(
            code = e.code,
            message = e.message,
        )
    }


    @org.springframework.web.bind.annotation.ExceptionHandler(UnauthorizedException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun unAuthorizedExceptionHandler(e: ConflictException): WinikimExceptionResponse {
        log.error("[인증 예외] message = ${e.message}")
        return WinikimExceptionResponse(
            code = e.code,
            message = e.message,
        )
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun httpMessageNotReadableExceptionHandler(e: HttpMessageNotReadableException): WinikimExceptionResponse {
        log.error("[httpMessageNotReadableExceptionHandler] message = ${e.message}")
        return WinikimExceptionResponse(
            code = "invalid_json",
            message = "JSON 형식이 잘못되었습니다.",
        )
    }

    @ExceptionHandler(ConstraintViolationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun constraintViolationExceptionHandler(e: ConstraintViolationException): WinikimExceptionResponse {
        log.error("[파리미터 유효성 검사 실패] message = ${e.message}")
        val exception = BadRequestException(message = e.message ?: "메시지 없음")
        return WinikimExceptionResponse(
            code = exception.code,
            message = exception.message,
        )
    }

    @ExceptionHandler(RuntimeException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun internalServerError(e: RuntimeException): WinikimExceptionResponse {
        e.printStackTrace()
        log.error("[내부 서버 에러] message = ${e.message}")
        return WinikimExceptionResponse(
            code = "internal_server_error",
            message = "처리 중 문제가 발생하였습니다.",
        )
    }
}
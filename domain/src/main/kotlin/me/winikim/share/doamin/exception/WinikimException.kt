package me.winikim.share.doamin.exception

import java.lang.RuntimeException

open class WinikimException(
    val code: String,
    override val message: String? = null
) : RuntimeException(
    message ?: code
)

open class NotFoundException(
    code: String,
    override val message: String
) : WinikimException(
    code = code,
    message = message ?: code
)

open class ConflictException(
    code: String,
    override val message: String
) : WinikimException(
    code = code,
    message = message ?: code
)

open class BadRequestException(
    code: String = "bad_request",
    override val message: String
) : WinikimException(
    code = code,
    message = message ?: code
)

open class UnauthorizedException(
    code: String = "unauthorized",
    override val message: String = "인증에 실패 하였습니다."
) : WinikimException(
    code = code,
    message = message ?: code
)

class NotFoundUserException: NotFoundException("not_found_user", "사용자를 찾을 수 없습니다.")
class NotFoundAdminUserException : NotFoundException("not_found_admin_user", "어드민 사용자를 찾을 수 없습니다.")
class InvalidIssuerException : BadRequestException("invalid_issuer", "발행자가 유효하지 않습니다.")
class TokenExpiredException: UnauthorizedException("token_expired", "토큰이 만료 되었습니다.")
class CheckUsernameOrPasswordException : BadRequestException("invalid_admin_user", "아이디 또는 패스워드를 확인하여 주세요.")
class UsernameAlreadyExistsException : BadRequestException("username_already_exists", "아이디가 이미 존재합니다.")
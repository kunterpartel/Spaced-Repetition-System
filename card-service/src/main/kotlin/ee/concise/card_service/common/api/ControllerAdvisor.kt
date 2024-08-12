package ee.concise.card_service.common.api

import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.ConstraintViolationException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@ControllerAdvice
class ControllerAdvisor {

    val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseBody
    fun handleException(e: MethodArgumentNotValidException, response: HttpServletResponse): ValidationErrorResponse {
        logger.info(e.message, e)

        response.sendError(HttpStatus.BAD_REQUEST.value())

        return ValidationErrorResponse(
            ExceptionConverter.translateBindingResult(e.bindingResult)
        )
    }

    @ResponseBody
    @ExceptionHandler(HttpMediaTypeNotSupportedException::class)
    fun handleException(e: HttpMediaTypeNotSupportedException, response: HttpServletResponse): GenericErrorResponse {

        logger.info(e.detailMessageCode, e)

        return GenericErrorResponse(
            "MESSAGE_NOT_READABLE"
        )
    }

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException::class)
    fun handleException(
        response: HttpServletResponse,
        e: ConstraintViolationException
    ): ValidationErrorResponse {
        logger.info(e.message, e)

        response.sendError(HttpStatus.BAD_REQUEST.value())

        return ValidationErrorResponse(
            ExceptionConverter.convert(e)
        )
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleException(
        e: MethodArgumentTypeMismatchException,
        response: HttpServletResponse
    ): ValidationErrorResponse {
        logger.info(e.message, e)

        response.sendError(HttpStatus.BAD_REQUEST.value())

        return ValidationErrorResponse(
            "INVALID_ARGUMENT",
            listOf(
                FieldErrorResponse(
                    e.parameter?.parameterName ?: "Unknown",
                    "InvalidType",
                    "Request parameter is invalid"
                )
            )
        )
    }

}

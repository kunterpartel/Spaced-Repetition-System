package ee.concise.card_service.domain.card.api.http

import ee.concise.card_service.common.api.GenericErrorResponse
import ee.concise.card_service.common.api.ValidationErrorResponse
import ee.concise.card_service.domain.card.api.http.CardMapper.toProcessSessionParameter
import ee.concise.card_service.domain.card.api.http.CardMapper.toResponseList
import ee.concise.card_service.domain.card.features.CreateCardFeature
import ee.concise.card_service.domain.card.features.GetAllCardsByDeckIdFeature
import ee.concise.card_service.domain.card.features.ProcessSessionResultsFeature
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Validated
@RestController
@RequestMapping(value = ["/v1/card"])
@Tag(name = "Card API")
class CardController(
    private val createCardFeature: CreateCardFeature,
    private val getAllCardsByDeckIdFeature: GetAllCardsByDeckIdFeature,
    private val processSessionResultsFeature: ProcessSessionResultsFeature
) {
    val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @PostMapping(consumes = ["application/json"], produces = ["application/json"])
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new card")
    @ApiResponses(
        ApiResponse(
            responseCode = "201",
            description = "Card created successfully",
            content = [Content(schema = Schema(implementation = CardResponse::class))]
        ),
        ApiResponse(
            responseCode = "400", description = "Constraint violation",
            content = [Content(schema = Schema(implementation = ValidationErrorResponse::class))]
        ),
        ApiResponse(
            responseCode = "500",
            description = "Unknown Error",
            content = [Content(schema = Schema(implementation = GenericErrorResponse::class))]
        )
    )
    fun createCard(@RequestBody @Valid @io.swagger.v3.oas.annotations.parameters.RequestBody request: CreateCardRequest): CardResponse {
        logger.info("Got request to create new card {}", request);

        return CardMapper.toResponse(
            createCardFeature.create(
                CreateCardFeature.Parameters(
                    request.deckId,
                    request.answer,
                    request.question
                )
            )
        )
    }

    @GetMapping("/due/{deckId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all due date cards with Deck ID")
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Cards fetched successfully",
        ),
        ApiResponse(
            responseCode = "400", description = "Constraint violation",
            content = [Content(schema = Schema(implementation = ValidationErrorResponse::class))]
        ),
        ApiResponse(
            responseCode = "500",
            description = "Unknown Error",
            content = [Content(schema = Schema(implementation = GenericErrorResponse::class))]
        )
    )
    fun getDueCards(@PathVariable @Min(1) deckId: Long): List<CardResponse> {
        logger.info("Got a request to fetch all due Cards")

        return toResponseList(getAllCardsByDeckIdFeature.get(deckId))
    }

    @PostMapping("/session-results")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Session results")
    @ApiResponses(
        ApiResponse(
            responseCode = "204",
            description = "Cards fetched successfully",
        ),
        ApiResponse(
            responseCode = "400", description = "Constraint violation",
            content = [Content(schema = Schema(implementation = ValidationErrorResponse::class))]
        ),
        ApiResponse(
            responseCode = "500",
            description = "Unknown Error",
            content = [Content(schema = Schema(implementation = GenericErrorResponse::class))]
        )
    )
    fun updateSessionResults(@RequestBody requst: SessionResultsRequest) {
        logger.info("Got a request to process all Session Results with request ${requst}")


        processSessionResultsFeature.process(toProcessSessionParameter(requst.results, requst.strategyType))
    }
}

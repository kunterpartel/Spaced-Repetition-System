package ee.concise.card_service.domain.card.api.http

import ee.concise.card_service.domain.card.common.Card
import ee.concise.card_service.domain.card.features.SaveCardFeature
import jakarta.transaction.Transactional
import org.hamcrest.Matchers
import org.hamcrest.core.Is
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDateTime

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Tag("integration")
class GetAllDueCardsWithDeckIdControllerIntegrationTests(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val saveCardFeature: SaveCardFeature
) {

    @BeforeEach
    fun setup() {
        val card = Card(0, 1L, "Question?", "answer", LocalDateTime.now(), 5)
        val secondCard = Card(0, 1L, "What is Question?", "answer", LocalDateTime.now().plusMinutes(5), 5)

        saveCardFeature.save(card)
        saveCardFeature.save(secondCard)
    }

    @Test
    @DisplayName(value = "When DeckId exists then should get all due Cards")
    @Transactional
    @Throws(
        Exception::class
    )
    fun whenDeckIdExistsThenShouldGetAllDueCards() {
        mockMvc.perform(
            createDefaultRequest(1L)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpectAll(
                MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.jsonPath("$", Matchers.hasSize<Any>(1)),
                MockMvcResultMatchers.jsonPath("$[0]", Matchers.aMapWithSize<Any, Any>(5)),
                MockMvcResultMatchers.jsonPath<Any>("$[0].id", Matchers.notNullValue()),
                MockMvcResultMatchers.jsonPath<Any>("$[0].deckId", Matchers.notNullValue()),
                MockMvcResultMatchers.jsonPath<Any>("$[0].dueDate", Matchers.notNullValue()),
                MockMvcResultMatchers.jsonPath<String>("$[0].question", Is.`is`<String>("Question?")),
                MockMvcResultMatchers.jsonPath<String>("$[0].answer", Is.`is`<String>("answer"))
            )
    }

    @Test
    @DisplayName(value = "When DeckId does not exists then should return empty array")
    @Transactional
    @Throws(
        Exception::class
    )
    fun whenDeckIdDoesNotExistThenReturnEmptyArray() {
        mockMvc.perform(
            createDefaultRequest(999L)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpectAll(
                MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.jsonPath("$", Matchers.hasSize<Any>(0)),
            )
    }

    private fun createDefaultRequest(id: Long): MockHttpServletRequestBuilder {
        return get("/v1/card/due/$id")
            .contentType(MediaType.APPLICATION_JSON)
    }
}

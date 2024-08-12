package ee.concise.card_service.domain.card.api.http

import jakarta.transaction.Transactional
import org.hamcrest.Matchers
import org.hamcrest.core.Is
import org.json.JSONObject
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.lang.NonNull
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Tag("integration")
class CreateCardControllerIntegrationTests(@Autowired private val mockMvc: MockMvc) {

    @Test
    @DisplayName(value = "When valid payload then should save Card to database and return a response")
    @Transactional
    @Throws(
        Exception::class
    )
    fun onValidPayloadShouldSaveToDatabaseAndReturnAResponse() {
        val payload: JSONObject = getPayload()

        mockMvc.perform(
            createDefaultRequest(payload)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpectAll(
                MockMvcResultMatchers.status().isCreated(),
                MockMvcResultMatchers.jsonPath("$", Matchers.aMapWithSize<Any, Any>(5)),
                MockMvcResultMatchers.jsonPath<Any>("$.id", Matchers.notNullValue()),
                MockMvcResultMatchers.jsonPath<Any>("$.deckId", Matchers.notNullValue()),
                MockMvcResultMatchers.jsonPath<Any>("$.dueDate", Matchers.notNullValue()),
                MockMvcResultMatchers.jsonPath<String>("$.question", Is.`is`<String>("What is a question?")),
                MockMvcResultMatchers.jsonPath<String>("$.answer", Is.`is`<String>("this is an answer"))
            )
    }

    @Test
    @DisplayName(value = "When invalid payload then should return validation errors")
    @Transactional
    @Throws(
        Exception::class
    )
    fun onInvalidPayloadShouldReturnValidationErrors() {

        mockMvc.perform(
            createDefaultRequest(JSONObject())
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpectAll(
                MockMvcResultMatchers.status().isBadRequest(),
                MockMvcResultMatchers.jsonPath("$", Matchers.aMapWithSize<Any, Any>(2)),
                MockMvcResultMatchers.jsonPath<String>("$.message", Is.`is`<String>("INVALID_ARGUMENT")),
                MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize<Any>(3)),
                MockMvcResultMatchers.jsonPath("$.errors[0]", Matchers.aMapWithSize<Any, Any>(3)),
                MockMvcResultMatchers.jsonPath("$.errors[0].field", Is.`is`("answer")),
                MockMvcResultMatchers.jsonPath("$.errors[0].reason", Is.`is`("NotEmpty")),
                MockMvcResultMatchers.jsonPath("$.errors[0].message", Is.`is`("must not be empty")),
                MockMvcResultMatchers.jsonPath("$.errors[1].field", Is.`is`("deckId")),
                MockMvcResultMatchers.jsonPath("$.errors[1].reason", Is.`is`("Min")),
                MockMvcResultMatchers.jsonPath("$.errors[1].message", Is.`is`("must be greater than or equal to 1")),
                MockMvcResultMatchers.jsonPath("$.errors[2].field", Is.`is`("question")),
                MockMvcResultMatchers.jsonPath("$.errors[2].reason", Is.`is`("NotEmpty")),
                MockMvcResultMatchers.jsonPath("$.errors[2].message", Is.`is`("must not be empty")),
            )
    }

    @NonNull
    private fun getPayload(): JSONObject {
        val payload = JSONObject()
        payload.put("deckId", 1)
        payload.put("answer", "this is an answer")
        payload.put("question", "What is a question?")

        return payload
    }

    private fun createDefaultRequest(payload: JSONObject): MockHttpServletRequestBuilder {
        return post("/v1/card")
            .contentType(MediaType.APPLICATION_JSON)
            .content(payload.toString())
            .accept(MediaType.APPLICATION_JSON)
    }
}

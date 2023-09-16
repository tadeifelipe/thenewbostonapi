package com.tadeifelipe.thenewboston.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.tadeifelipe.thenewboston.model.Bank
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.*

@SpringBootTest
@AutoConfigureMockMvc
internal class BankControllerTest @Autowired constructor (
        val mockMvc: MockMvc,
        val objectMapper: ObjectMapper
) {

    var baseUrl = "/api/banks"

    @Test
    fun `should return all banks`() {
        mockMvc.get(baseUrl)
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$[0].accountNumber") { value("1234") }
                }
    }

    @Test
    fun `should return with the given acoountNumber`() {
        val accountNumber = 1234

        mockMvc.get("$baseUrl/$accountNumber")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.trust") { value("3.14") }
                    jsonPath("$.transactionalFee") { value("17") }
                }
    }

    @Test
    fun `should return not found if the account number does not exist`() {
        val accountNumber = 3536

        mockMvc.get("$baseUrl/$accountNumber")
                .andDo { print() }
                .andExpect {
                    status { isNotFound() }
                }
    }

    @Test
    fun `should add the new bank`() {
        val newBank = Bank("456", 3.1, 25)

        val performPost = mockMvc.post(baseUrl) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(newBank)
        }

        performPost
                .andDo { print() }
                .andExpect {
                    status { isCreated() }
                    content { contentType( MediaType.APPLICATION_JSON )}
                    jsonPath("$.accountNumber") { value(456) }
                    jsonPath("$.trust") { value(3.1) }
                    jsonPath("$.transactionalFee") { value(25) }
                }
    }

    @Test
    fun `should return bad request if bank with given account number already exists`() {
        val newBank = Bank("1234", 3.1, 25)

        val performPost = mockMvc.post(baseUrl) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(newBank)
        }

        performPost
                .andDo { print() }
                .andExpect {
                    status { isBadRequest() }
                }
    }

    @Test
    @DirtiesContext
    fun `should update an existing bank`() {
        val bank = Bank("1234", 1.0, 25)

        val performPatch = mockMvc.patch(baseUrl) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(bank)
        }

        performPatch
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        json(objectMapper.writeValueAsString(bank))
                    }
                }

        mockMvc.get("$baseUrl/${bank.accountNumber}")
                .andExpect { content { json(objectMapper.writeValueAsString(bank)) } }
    }

    @Test
    fun `should return bad request if bank with given account number do not exists`() {
        val newBank = Bank("do not exists", 3.1, 25)

        val performPost = mockMvc.patch(baseUrl) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(newBank)
        }

        performPost
                .andDo { print() }
                .andExpect {
                    status { isNotFound() }
                }
    }

    @Test
    @DirtiesContext
    fun `should delete a bank given account number`() {
        val accountNumber = 1234

        mockMvc.delete("$baseUrl/$accountNumber")
                .andDo { print() }
                .andExpect {
                    status { isNoContent() }
                }

        mockMvc.get("$baseUrl/$accountNumber")
                .andDo { print() }
                .andExpect {
                    status { isNotFound() }
                }
    }

    @Test
    fun `should not found bank given account number that not exists`() {
        val accountNumber = 96854

        mockMvc.delete("$baseUrl/$accountNumber")
                .andDo { print() }
                .andExpect {
                    status { isNotFound() }
                }
    }
}
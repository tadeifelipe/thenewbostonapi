package com.tadeifelipe.thenewboston.service

import com.tadeifelipe.thenewboston.datasource.BankDataSource
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class BankServiceTest {

    private val datasource: BankDataSource = mockk(relaxed = true)
    private val bankservice = BankService(datasource)

    @Test
    fun `should call its data source to retrieve banks`() {
        bankservice.getBanks()

        verify(exactly = 1) { datasource.retrieveBanks() }
    }

}
package com.tadeifelipe.thenewboston.datasource

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

internal class MockBankDataSourceTest {

    private val mockDataSource = MockBankDataSource()

    @Test
    fun `should provide a collection of banks`() {
        val banks = mockDataSource.retrieveBanks()

        assertThat(banks.size).isGreaterThanOrEqualTo(3)
    }

    @Test
    fun `provide some mock data`() {
        val banks = mockDataSource.retrieveBanks()

        assertThat(banks).allMatch { bank -> bank.accountNumber.isNotBlank() }
        assertThat(banks).anyMatch { bank -> bank.trust != 0.0 }
        assertThat(banks).anyMatch { bank -> bank.transactionalFee != 0 }
    }
}
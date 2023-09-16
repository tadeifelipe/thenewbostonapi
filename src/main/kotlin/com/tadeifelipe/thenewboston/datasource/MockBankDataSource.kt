package com.tadeifelipe.thenewboston.datasource

import com.tadeifelipe.thenewboston.model.Bank
import org.springframework.stereotype.Repository
import java.lang.IllegalArgumentException

@Repository
class MockBankDataSource : BankDataSource {

    val banks = mutableListOf(
            Bank("1234", 3.14, 17),
            Bank("1010", 7.0, 0),
            Bank("4568", 0.0, 100)
    )

    override fun retrieveBanks(): Collection<Bank> = banks

    override fun retrieveBank(accountNumber: String): Bank =
            banks.firstOrNull() { it.accountNumber == accountNumber }
                    ?: throw NoSuchElementException("Could not find a bank account with $accountNumber")

    override fun createBank(bank: Bank): Bank {
        if (banks.any { it.accountNumber == bank.accountNumber }) {
            throw IllegalArgumentException("Bank with account number ${bank.accountNumber} already exists")
        }

        banks.add(bank)

        return bank
    }

    override fun updateBank(bank: Bank): Bank {
        val currentBank = banks.firstOrNull() { it.accountNumber == bank.accountNumber }
                ?: throw NoSuchElementException("Could not find a bank account with ${bank.accountNumber}")

        banks.remove(currentBank)
        banks.add(bank)

        return bank
    }

    override fun deleteBank(accountNumber: String) {
        val currentBank = banks.firstOrNull() { it.accountNumber == accountNumber }
                ?: throw NoSuchElementException("Could not find a bank account with $accountNumber")

        banks.remove(currentBank)
    }
}
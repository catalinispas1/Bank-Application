package com.luxoft.bankapp.tests;



import com.luxoft.bankapp.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class BankReportStreamsTest {

    private Bank bank;
    private Client client1, client2, client3;

    @BeforeEach
    void setUp() {
        bank = new Bank();

        // Create clients
        client1 = new Client("Alice", Gender.FEMALE, "New York");
        client2 = new Client("Bob", Gender.MALE, "Los Angeles");
        client3 = new Client("Charlie", Gender.MALE, "New York");

        // Add accounts to clients
        client1.addAccount(new SavingAccount(1, 500.0));
        client1.addAccount(new CheckingAccount(2, 200.0, 100.0));

        client2.addAccount(new SavingAccount(3, 300.0));
        client2.addAccount(new CheckingAccount(4, 100.0, 50.0));

        client3.addAccount(new SavingAccount(5, 800.0));

        // Add clients to bank
        try {
            bank.addClient(client1);
            bank.addClient(client2);
            bank.addClient(client3);
        } catch (Exception e) {
            fail("Unexpected exception during setup: " + e.getMessage());
        }
    }

    @Test
    void testGetNumberOfClients() {
        assertEquals(3, BankReportStreams.getNumberOfClients(bank));
    }

    @Test
    void testGetNumberOfAccounts() {
        assertEquals(5, BankReportStreams.getNumberOfAccounts(bank));
    }

    @Test
    void testGetClientsSorted() {
        SortedSet<Client> sortedClients = BankReportStreams.getClientsSorted(bank);
        assertEquals(3, sortedClients.size());

        Iterator<Client> iterator = sortedClients.iterator();
        assertEquals("Alice", iterator.next().getName());
        assertEquals("Bob", iterator.next().getName());
        assertEquals("Charlie", iterator.next().getName());
    }

    @Test
    void testGetTotalSumInAccounts() {
        double totalSum = BankReportStreams.getTotalSumInAccounts(bank);
        assertEquals(1900.0, totalSum, 0.01);
    }

    @Test
    void testGetAccountsSortedBySum() {
        SortedSet<Account> sortedAccounts = BankReportStreams.getAccountsSortedBySum(bank);

        Iterator<Account> iterator = sortedAccounts.iterator();
        assertEquals(100.0, iterator.next().getBalance());
        assertEquals(200.0, iterator.next().getBalance());
        assertEquals(300.0, iterator.next().getBalance());
        assertEquals(500.0, iterator.next().getBalance());
        assertEquals(800.0, iterator.next().getBalance());
    }

    @Test
    void testGetBankCreditSum() {
        double creditSum = BankReportStreams.getBankCreditSum(bank);
        assertEquals(150.0, creditSum, 0.01);
    }

    @Test
    void testGetCustomerAccounts() {
        Map<Client, Collection<Account>> customerAccounts = BankReportStreams.getCustomerAccounts(bank);

        assertEquals(3, customerAccounts.size());
        assertTrue(customerAccounts.containsKey(client1));
        assertTrue(customerAccounts.containsKey(client2));
        assertTrue(customerAccounts.containsKey(client3));

        assertEquals(2, customerAccounts.get(client1).size());
        assertEquals(2, customerAccounts.get(client2).size());
        assertEquals(1, customerAccounts.get(client3).size());
    }

    @Test
    void testGetClientsByCity() {
        Map<String, List<Client>> clientsByCity = BankReportStreams.getClientsByCity(bank);

        assertEquals(2, clientsByCity.size());
        assertEquals(2, clientsByCity.get("New York").size());
        assertEquals(1, clientsByCity.get("Los Angeles").size());

        assertTrue(clientsByCity.get("New York").contains(client1));
        assertTrue(clientsByCity.get("New York").contains(client3));
        assertTrue(clientsByCity.get("Los Angeles").contains(client2));
    }
}

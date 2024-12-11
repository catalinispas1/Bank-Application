package com.luxoft.bankapp.domain;

import java.util.*;
import java.util.stream.Collectors;

public class BankReportStreams {

    // Returns the number of clients in the bank
    public static int getNumberOfClients(Bank bank) {
        return (int) bank.getClients().stream().count();
    }

    // Returns the total number of accounts for all bank clients
    public static int getNumberOfAccounts(Bank bank) {
        return bank.getClients().stream()
                .mapToInt(client -> client.getAccounts().size())
                .sum();
    }

    // Displays the set of clients in alphabetical order
    public static SortedSet<Client> getClientsSorted(Bank bank) {
        return bank.getClients().stream()
                .sorted(Comparator.comparing(Client::getName))
                .collect(Collectors.toCollection(TreeSet::new));
    }

    // Returns the total sum (balance) from the accounts of all bank clients
    public static double getTotalSumInAccounts(Bank bank) {
        return bank.getClients().stream()
                .flatMap(client -> client.getAccounts().stream())
                .mapToDouble(Account::getBalance)
                .sum();
    }

    // Returns the set of all accounts ordered by current account balance
    public static SortedSet<Account> getAccountsSortedBySum(Bank bank) {
        return bank.getClients().stream()
                .flatMap(client -> client.getAccounts().stream())
                .sorted(Comparator.comparingDouble(Account::getBalance))
                .collect(Collectors.toCollection(TreeSet::new));
    }

    // Returns the total amount of credits granted to the bank clients (sum of negative balances)
    public static double getBankCreditSum(Bank bank) {
        return bank.getClients().stream()
                .flatMap(client -> client.getAccounts().stream())
                .filter(account -> account instanceof CheckingAccount && account.getBalance() < 0)
                .mapToDouble(account -> -account.getBalance())
                .sum();
    }

    // Returns a map of clients and their accounts
    public static Map<Client, Collection<Account>> getCustomerAccounts(Bank bank) {
        return bank.getClients().stream()
                .collect(Collectors.toMap(client -> client, Client::getAccounts));
    }

    // Returns a map of cities and their associated clients, ordered by city name
    public static Map<String, List<Client>> getClientsByCity(Bank bank) {
        return bank.getClients().stream()
                .filter(client -> client.getCity() != null) // Ensure city is not null
                .collect(Collectors.groupingBy(Client::getCity, TreeMap::new, Collectors.toList()));
    }
}

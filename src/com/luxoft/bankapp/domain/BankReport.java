package com.luxoft.bankapp.domain;

import java.util.*;
import java.util.stream.Collectors;

public class BankReport {

    public int getNumberOfClients(Bank bank) {
        return bank.getClients().size();
    }

    public int getNumberOfAccounts(Bank bank) {
        return bank.getClients().stream()
                .mapToInt(client -> client.getAccounts().size())
                .sum();
    }

    public SortedSet<Client> getClientsSorted(Bank bank) {
        return new TreeSet<>(bank.getClients());
    }

    public double getTotalSumInAccounts(Bank bank) {
        return bank.getClients().stream()
                .flatMap(client -> client.getAccounts().stream())
                .mapToDouble(Account::getBalance)
                .sum();
    }

    public SortedSet<Account> getAccountsSortedBySum(Bank bank) {
        return bank.getClients().stream()
                .flatMap(client -> client.getAccounts().stream())
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparingDouble(Account::getBalance))));
    }

    public double getBankCreditSum(Bank bank) {
        return bank.getClients().stream()
                .flatMap(client -> client.getAccounts().stream())
                .filter(account -> account instanceof CheckingAccount)
                .mapToDouble(Account::getBalance)
                .filter(balance -> balance < 0)
                .sum();
    }

    public Map<Client, Collection<Account>> getCustomerAccounts(Bank bank) {
        return bank.getClients().stream()
                .collect(Collectors.toMap(client -> client, Client::getAccounts));
    }

    public Map<String, List<Client>> getClientsByCity(Bank bank) {
        return bank.getClients().stream()
                .collect(Collectors.groupingBy(Client::getCity, TreeMap::new, Collectors.toList()));
    }
}


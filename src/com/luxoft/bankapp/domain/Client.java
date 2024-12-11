package com.luxoft.bankapp.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Client {

	private String name;
	private String city; // New field for the city
	private Gender gender;
	private final Set<Account> accounts = new HashSet<>();

	public Client(String name, Gender gender, String city) {
		this.name = name;
		this.gender = gender;
		this.city = city;
	}

	public void addAccount(final Account account) {
		accounts.add(account);
	}

	public String getName() {
		return name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Gender getGender() {
		return gender;
	}

	public Set<Account> getAccounts() {
		return Collections.unmodifiableSet(accounts);
	}

	public String getClientGreeting() {
		if (gender != null) {
			return gender.getGreeting() + " " + name;
		} else {
			return name;
		}
	}

	@Override
	public String toString() {
		return getClientGreeting();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof Client)) return false;
		Client other = (Client) obj;
		return name.equals(other.name);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}
}

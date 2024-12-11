package com.luxoft.bankapp.domain;

import java.text.DateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.luxoft.bankapp.exceptions.ClientExistsException;
import com.luxoft.bankapp.utils.ClientRegistrationListener;

public class Bank {

	private final Set<Client> clients = new HashSet<>();
	private final Set<ClientRegistrationListener> listeners = new HashSet<>();
	private final EmailService emailService = new EmailService(); // EmailService for async email processing

	private int printedClients = 0;
	private int emailedClients = 0;
	private int debuggedClients = 0;

	public Bank() {
		listeners.add(new PrintClientListener());
		listeners.add(new EmailNotificationListener(emailService)); // Updated to use async EmailService
		listeners.add(new DebugListener());
	}

	public int getPrintedClients() {
		return printedClients;
	}

	public int getEmailedClients() {
		return emailedClients;
	}

	public int getDebuggedClients() {
		return debuggedClients;
	}

	public void addClient(final Client client) throws ClientExistsException {
		if (clients.contains(client)) {
			throw new ClientExistsException("Client already exists in the bank");
		}
		clients.add(client);
		notify(client);
	}

	private void notify(Client client) {
		for (ClientRegistrationListener listener : listeners) {
			listener.onClientAdded(client);
		}
	}

	public Set<Client> getClients() {
		return Collections.unmodifiableSet(clients);
	}

	public void close() {
		emailService.close(); // Gracefully shut down the email service
	}

	class PrintClientListener implements ClientRegistrationListener {
		@Override
		public void onClientAdded(Client client) {
			System.out.println("Client added: " + client.getName());
			printedClients++;
		}
	}

	class EmailNotificationListener implements ClientRegistrationListener {
		private final EmailService emailService;

		public EmailNotificationListener(EmailService emailService) {
			this.emailService = emailService;
		}

		@Override
		public void onClientAdded(Client client) {
			// Add email to the queue for asynchronous processing
			emailService.sendNotificationEmail(
					new Email(
							client,
							"bank@example.com",
							client.getName() + "@example.com",
							"Welcome to Our Bank!",
							"Dear " + client.getClientGreeting() + ",\n\nThank you for joining our bank."
					)
			);
			emailedClients++;
		}
	}

	class DebugListener implements ClientRegistrationListener {
		@Override
		public void onClientAdded(Client client) {
			System.out.println("Client " + client.getName() + " added on: " +
					DateFormat.getDateInstance(DateFormat.FULL).format(new Date()));
			debuggedClients++;
		}
	}
}

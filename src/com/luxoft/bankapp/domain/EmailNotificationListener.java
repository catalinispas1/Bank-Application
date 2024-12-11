package com.luxoft.bankapp.domain;

import com.luxoft.bankapp.utils.ClientRegistrationListener;

public class EmailNotificationListener implements ClientRegistrationListener {
    private final EmailService emailService;

    public EmailNotificationListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void onClientAdded(Client client) {
        Email email = new Email(
                client,
                "bank@example.com",
                client.getName() + "@example.com",
                "Welcome to Our Bank!",
                "Dear " + client.getClientGreeting() + ",\n\nThank you for joining our bank."
        );
        emailService.sendNotificationEmail(email);
    }
}

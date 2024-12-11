package com.luxoft.bankapp.domain;

public class EmailService {
    private final Queue queue = new Queue();
    private final Thread workerThread;

    public EmailService() {
        workerThread = new Thread(() -> {
            try {
                while (true) {
                    Email email = queue.remove();
                    if (email == null) break; // Exit thread if queue is closed and empty

                    System.out.println("Sending email: " + email);
                    Thread.sleep(2000); // Simulate delay for sending email
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupted state
            }
        });

        workerThread.start();
    }

    public void sendNotificationEmail(Email email) {
        queue.add(email);
    }

    public void close() {
        queue.close();
        try {
            workerThread.join(); // Wait for the worker thread to finish
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted state
        }
    }
}

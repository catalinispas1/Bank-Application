package com.luxoft.bankapp.domain;

import java.util.LinkedList;
import java.util.List;

public class Queue {
    private final List<Email> emailQueue = new LinkedList<>();
    private boolean acceptingEmails = true;

    public synchronized void add(Email email) {
        if (!acceptingEmails) {
            throw new IllegalStateException("Queue is closed for new emails.");
        }
        emailQueue.add(email);
        notifyAll(); // Notify any waiting threads
    }

    public synchronized Email remove() throws InterruptedException {
        while (emailQueue.isEmpty() && acceptingEmails) {
            wait(); // Wait until there's an email in the queue or the queue is closed
        }

        if (!emailQueue.isEmpty()) {
            return emailQueue.remove(0);
        }
        return null; // Queue is closed and empty
    }

    public synchronized void close() {
        acceptingEmails = false;
        notifyAll(); // Notify all threads waiting for new emails
    }
}


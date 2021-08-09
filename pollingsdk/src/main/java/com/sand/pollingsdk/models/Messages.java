package com.sand.pollingsdk.models;

import java.util.List;

public class Messages {
    private List<Message> messages;

    public Messages(List<Message> messages) {
        this.messages = messages;
    }

    public List<Message> getMessages() {
        return messages;
    }
}

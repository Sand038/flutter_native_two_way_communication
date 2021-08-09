package com.sand.pollingsdk.models;

import java.util.List;

public class MessagesWithCount {
    final int count;
    final List<Message> messages;

    public MessagesWithCount(int count, List<Message> messages) {
        this.count = count;
        this.messages = messages;
    }

    public int getCount() {
        return count;
    }

    public List<Message> getMessages() {
        return messages;
    }
}

package com.redhat.podmortem.provider.openai.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Request implements Serializable {
    private static final long serialVersionUID = 1L;

    private String model;
    private List<Map<String, String>> messages;
    private boolean stream;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<Map<String, String>> getMessages() {
        return messages;
    }

    public void setMessages(List<Map<String, String>> messages) {
        this.messages = messages;
    }

    public boolean isStream() {
        return stream;
    }

    public void setStream(boolean stream) {
        this.stream = stream;
    }
}

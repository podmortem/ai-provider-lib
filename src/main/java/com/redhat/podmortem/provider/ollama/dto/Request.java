package com.redhat.podmortem.provider.ollama.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import java.io.Serializable;

@RegisterForReflection
public class Request implements Serializable {
    private static final long serialVersionUID = 1L;

    private String model;
    private String prompt;
    private boolean stream;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public boolean isStream() {
        return stream;
    }

    public void setStream(boolean stream) {
        this.stream = stream;
    }
}

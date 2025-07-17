package com.redhat.podmortem.provider.ollama.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;
import java.io.Serializable;
import java.time.Instant;

@RegisterForReflection
public class Response implements Serializable {
    private static final long serialVersionUID = 1L;

    private String model;

    @JsonProperty("created_at")
    private Instant createdAt;

    private String response;

    private boolean done;

    @JsonProperty("eval_count")
    private int evalCount;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public int getEvalCount() {
        return evalCount;
    }

    public void setEvalCount(int evalCount) {
        this.evalCount = evalCount;
    }
}

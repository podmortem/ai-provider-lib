package com.redhat.podmortem.provider.ollama.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;
import java.io.Serializable;
import java.time.Instant;

/**
 * DTO representing a response from the Ollama text generation API.
 *
 * @see com.redhat.podmortem.provider.ollama.OllamaClient
 * @see Request
 */
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

    /**
     * Gets the model that was used to generate this response.
     *
     * @return the model identifier
     */
    public String getModel() {
        return model;
    }

    /**
     * Sets the model that was used to generate this response.
     *
     * @param model the model identifier
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Gets the timestamp when this response was created.
     *
     * @return the creation timestamp as an Instant
     */
    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the timestamp when this response was created.
     *
     * @param createdAt the creation timestamp
     */
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Gets the generated response text from the model.
     *
     * @return the generated text content
     */
    public String getResponse() {
        return response;
    }

    /**
     * Sets the generated response text from the model.
     *
     * @param response the generated text content
     */
    public void setResponse(String response) {
        this.response = response;
    }

    /**
     * Gets whether the text generation is complete.
     *
     * @return true if generation is complete, false if more data is expected
     */
    public boolean isDone() {
        return done;
    }

    /**
     * Sets whether the text generation is complete.
     *
     * @param done true if generation is complete, false if more data is expected
     */
    public void setDone(boolean done) {
        this.done = done;
    }

    /**
     * Gets the number of tokens evaluated during generation.
     *
     * @return the evaluation token count
     */
    public int getEvalCount() {
        return evalCount;
    }

    /**
     * Sets the number of tokens evaluated during generation.
     *
     * @param evalCount the evaluation token count
     */
    public void setEvalCount(int evalCount) {
        this.evalCount = evalCount;
    }
}

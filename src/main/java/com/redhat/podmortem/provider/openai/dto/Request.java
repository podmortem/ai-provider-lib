package com.redhat.podmortem.provider.openai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;
import java.io.Serializable;

/**
 * DTO representing a request to the OpenAI text completion API.
 *
 * @see com.redhat.podmortem.provider.openai.OpenAIClient
 * @see Response
 */
@RegisterForReflection
public class Request implements Serializable {
    private static final long serialVersionUID = 1L;

    private String model;
    private String prompt;
    private boolean stream;

    @JsonProperty("max_tokens")
    private Integer maxTokens;

    private Double temperature;

    public Request() {}

    /**
     * Gets the model identifier to use for text completion.
     *
     * @return the model name (e.g., "text-davinci-003", "gpt-3.5-turbo-instruct")
     */
    public String getModel() {
        return model;
    }

    /**
     * Sets the model identifier to use for text completion.
     *
     * @param model the model name to use
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Gets the prompt text to send to the model for completion.
     *
     * @return the prompt text
     */
    public String getPrompt() {
        return prompt;
    }

    /**
     * Sets the prompt text to send to the model for completion.
     *
     * @param prompt the prompt text
     */
    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    /**
     * Gets whether to enable streaming response mode.
     *
     * @return true if streaming is enabled, false for a single response
     */
    public boolean isStream() {
        return stream;
    }

    /**
     * Sets whether to enable streaming response mode.
     *
     * @param stream true to enable streaming, false for single response
     */
    public void setStream(boolean stream) {
        this.stream = stream;
    }

    /**
     * Gets the maximum number of tokens to generate in the completion.
     *
     * @return the maximum token count, or null if not specified
     */
    public Integer getMaxTokens() {
        return maxTokens;
    }

    /**
     * Sets the maximum number of tokens to generate in the completion.
     *
     * @param maxTokens the maximum token count to generate
     */
    public void setMaxTokens(Integer maxTokens) {
        this.maxTokens = maxTokens;
    }

    /**
     * Gets the sampling temperature for text generation.
     *
     * @return the temperature value between 0.0 and 2.0, or null if not specified
     */
    public Double getTemperature() {
        return temperature;
    }

    /**
     * Sets the sampling temperature for text generation.
     *
     * <p>Controls randomness: 0.0 = deterministic, 1.0 = balanced, 2.0 = creative.
     *
     * @param temperature the temperature value between 0.0 and 2.0
     */
    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }
}

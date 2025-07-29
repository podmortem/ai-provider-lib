package com.redhat.podmortem.provider.ollama.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import java.io.Serializable;

/**
 * DTO representing a request to the Ollama text generation API.
 *
 * @see com.redhat.podmortem.provider.ollama.OllamaClient
 * @see Response
 */
@RegisterForReflection
public class Request implements Serializable {
    private static final long serialVersionUID = 1L;

    private String model;
    private String prompt;
    private boolean stream;

    /**
     * Gets the model identifier to use for text generation.
     *
     * @return the model name (e.g., "llama2", "codellama", "mistral")
     */
    public String getModel() {
        return model;
    }

    /**
     * Sets the model identifier to use for text generation.
     *
     * @param model the model name to use
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Gets the prompt text to send to the model for generation.
     *
     * @return the prompt text
     */
    public String getPrompt() {
        return prompt;
    }

    /**
     * Sets the prompt text to send to the model for generation.
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
}

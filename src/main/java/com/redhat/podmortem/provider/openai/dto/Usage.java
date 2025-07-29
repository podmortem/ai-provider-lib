package com.redhat.podmortem.provider.openai.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;
import java.io.Serializable;

/**
 * DTO representing token usage statistics from the OpenAI API.
 *
 * <p>Contains token consumption details for completion requests including prompt, completion, and
 * total counts. Used for monitoring API usage and calculating costs.
 *
 * @see Response
 * @see com.redhat.podmortem.provider.openai.OpenAIClient
 */
@RegisterForReflection
@JsonIgnoreProperties(ignoreUnknown = true)
public class Usage implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("prompt_tokens")
    private int promptTokens;

    @JsonProperty("completion_tokens")
    private int completionTokens;

    @JsonProperty("total_tokens")
    private int totalTokens;

    /**
     * Gets the number of tokens in the input prompt.
     *
     * @return the token count for the prompt text
     */
    public int getPromptTokens() {
        return promptTokens;
    }

    /**
     * Sets the number of tokens in the input prompt.
     *
     * @param promptTokens the token count for the prompt text
     */
    public void setPromptTokens(int promptTokens) {
        this.promptTokens = promptTokens;
    }

    /**
     * Gets the number of tokens generated in the completion response.
     *
     * @return the token count for the generated completion
     */
    public int getCompletionTokens() {
        return completionTokens;
    }

    /**
     * Sets the number of tokens generated in the completion response.
     *
     * @param completionTokens the token count for the generated completion
     */
    public void setCompletionTokens(int completionTokens) {
        this.completionTokens = completionTokens;
    }

    /**
     * Gets the total number of tokens used for this request.
     *
     * @return the total token count (prompt + completion)
     */
    public int getTotalTokens() {
        return totalTokens;
    }

    /**
     * Sets the total number of tokens used for this request.
     *
     * @param totalTokens the total token count
     */
    public void setTotalTokens(int totalTokens) {
        this.totalTokens = totalTokens;
    }
}

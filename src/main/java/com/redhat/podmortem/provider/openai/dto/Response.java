package com.redhat.podmortem.provider.openai.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;

/**
 * DTO representing a response from the OpenAI text completion API.
 *
 * @see com.redhat.podmortem.provider.openai.OpenAIClient
 * @see Request
 * @see Usage
 */
@RegisterForReflection
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Represents a single completion choice within an OpenAI response.
     *
     * <p>Contains generated text, choice index, and completion status.
     */
    @RegisterForReflection
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Choice implements Serializable {
        private int index;
        private String text;

        @JsonProperty("finish_reason")
        private String finishReason;

        /**
         * Gets the index of this choice within the response.
         *
         * @return the zero-based index of this choice
         */
        public int getIndex() {
            return index;
        }

        /**
         * Sets the index of this choice within the response.
         *
         * @param index the zero-based index
         */
        public void setIndex(int index) {
            this.index = index;
        }

        /**
         * Gets the generated completion text for this choice.
         *
         * @return the generated text content
         */
        public String getText() {
            return text;
        }

        /**
         * Sets the generated completion text for this choice.
         *
         * @param text the generated text content
         */
        public void setText(String text) {
            this.text = text;
        }

        /**
         * Gets the reason why text generation finished for this choice.
         *
         * @return the finish reason (e.g., "stop", "length", "content_filter")
         */
        public String getFinishReason() {
            return finishReason;
        }

        /**
         * Sets the reason why text generation finished for this choice.
         *
         * @param finishReason the finish reason
         */
        public void setFinishReason(String finishReason) {
            this.finishReason = finishReason;
        }
    }

    private String id;
    private String object;
    private long created;
    private String model;

    @JsonProperty("choices")
    private List<Choice> choices;

    private Usage usage;

    /**
     * Gets the unique identifier for this completion response.
     *
     * @return the response ID
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier for this completion response.
     *
     * @param id the response ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the object type of this response.
     *
     * @return the object type (typically "text_completion")
     */
    public String getObject() {
        return object;
    }

    /**
     * Sets the object type of this response.
     *
     * @param object the object type
     */
    public void setObject(String object) {
        this.object = object;
    }

    /**
     * Gets the Unix timestamp when this completion was created.
     *
     * @return the creation timestamp in seconds since epoch
     */
    public long getCreated() {
        return created;
    }

    /**
     * Sets the Unix timestamp when this completion was created.
     *
     * @param created the creation timestamp in seconds since epoch
     */
    public void setCreated(long created) {
        this.created = created;
    }

    /**
     * Gets the creation time as a Java Instant object.
     *
     * @return the creation time as an Instant
     */
    public Instant getCreatedAsInstant() {
        return Instant.ofEpochSecond(created);
    }

    /**
     * Gets the model that was used to generate this completion.
     *
     * @return the model identifier
     */
    public String getModel() {
        return model;
    }

    /**
     * Sets the model that was used to generate this completion.
     *
     * @param model the model identifier
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Gets the list of completion choices returned by the API.
     *
     * @return the list of completion choices, may be empty
     */
    public List<Choice> getChoices() {
        return choices;
    }

    /**
     * Sets the list of completion choices returned by the API.
     *
     * @param choices the list of completion choices
     */
    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    /**
     * Gets the token usage statistics for this completion.
     *
     * @return the usage statistics, or null if not provided
     */
    public Usage getUsage() {
        return usage;
    }

    /**
     * Sets the token usage statistics for this completion.
     *
     * @param usage the usage statistics
     */
    public void setUsage(Usage usage) {
        this.usage = usage;
    }
}

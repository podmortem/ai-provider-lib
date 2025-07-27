package com.redhat.podmortem.provider.openai.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;
import java.io.Serializable;

@RegisterForReflection
@JsonIgnoreProperties(ignoreUnknown = true)
public class Choice implements Serializable {
    private static final long serialVersionUID = 1L;

    @RegisterForReflection
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Message implements Serializable {
        private String role;
        private String content;

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    private int index;
    private Message message;

    @JsonProperty("finish_reason")
    private String finishReason;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public String getFinishReason() {
        return finishReason;
    }

    public void setFinishReason(String finishReason) {
        this.finishReason = finishReason;
    }
}

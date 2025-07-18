package com.redhat.podmortem.provider.openai;

import com.redhat.podmortem.common.model.analysis.AnalysisResult;
import com.redhat.podmortem.common.model.provider.AIProvider;
import com.redhat.podmortem.common.model.provider.AIProviderConfig;
import com.redhat.podmortem.common.model.provider.AIResponse;
import com.redhat.podmortem.common.model.provider.ValidationResult;
import com.redhat.podmortem.provider.openai.dto.Request;
import com.redhat.podmortem.provider.openai.dto.Response;
import com.redhat.podmortem.provider.service.PromptTemplateService;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
@Named("openai")
@RegisterForReflection
public class OpenAIProvider implements AIProvider {

    @Inject @RestClient OpenAIClient restClient;

    @Inject PromptTemplateService promptService;

    @Override
    public Uni<AIResponse> generateExplanation(AnalysisResult result, AIProviderConfig config) {
        String userPrompt = promptService.buildUserPrompt(result);
        String systemPrompt = promptService.getSystemPrompt();

        var requestBody = new Request();
        requestBody.setModel(config.getModelId());
        requestBody.setStream(false);
        requestBody.setMessages(
                List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", userPrompt)));

        Instant requestStart = Instant.now();

        return restClient
                .getCompletion(
                        URI.create(config.getApiUrl()),
                        "Bearer " + config.getAuthToken(),
                        requestBody)
                .map(osResponse -> toAIResponse(osResponse, config, requestStart));
    }

    @Override
    public Uni<ValidationResult> validateConfiguration(AIProviderConfig config) {
        ValidationResult result =
                new ValidationResult(
                        true,
                        getProviderId(),
                        config.getModelId(),
                        "Connection successful (mocked).");
        return Uni.createFrom().item(result);
    }

    @Override
    public String getProviderId() {
        return "openai";
    }

    private AIResponse toAIResponse(
            Response response, AIProviderConfig config, Instant requestStart) {
        Instant responseEnd = Instant.now();
        AIResponse aiResponse = new AIResponse();
        aiResponse.setProviderId(getProviderId());
        aiResponse.setModelId(config.getModelId());
        aiResponse.setGeneratedAt(response.getCreated());
        if (response.getChoices() != null && !response.getChoices().isEmpty()) {
            aiResponse.setExplanation(response.getChoices().get(0).getMessage().get("content"));
        } else {
            aiResponse.setExplanation("No explanation could be generated.");
        }
        if (response.getUsage() != null) {
            aiResponse.setTokenCount(response.getUsage().getTotalTokens());
        }
        aiResponse.setProcessingTime(Duration.between(requestStart, responseEnd));
        return aiResponse;
    }
}

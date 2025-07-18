package com.redhat.podmortem.provider.ollama;

import com.redhat.podmortem.common.model.analysis.AnalysisResult;
import com.redhat.podmortem.common.model.provider.AIProvider;
import com.redhat.podmortem.common.model.provider.AIProviderConfig;
import com.redhat.podmortem.common.model.provider.AIResponse;
import com.redhat.podmortem.common.model.provider.ValidationResult;
import com.redhat.podmortem.provider.ollama.dto.Request;
import com.redhat.podmortem.provider.ollama.dto.Response;
import com.redhat.podmortem.provider.service.PromptTemplateService;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
@Named("ollama")
@RegisterForReflection
public class OllamaProvider implements AIProvider {

    @Inject @RestClient OllamaClient restClient;

    @Inject PromptTemplateService promptService;

    @Override
    public Uni<AIResponse> generateExplanation(AnalysisResult result, AIProviderConfig config) {
        String userPrompt = promptService.buildUserPrompt(result);
        String systemPrompt = promptService.getSystemPrompt();

        var requestBody = new Request();
        requestBody.setModel(config.getModelId());
        requestBody.setPrompt(systemPrompt + "\n" + userPrompt);
        requestBody.setStream(false);

        Instant requestStart = Instant.now();

        return restClient
                .getCompletion(URI.create(config.getApiUrl()), requestBody)
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
        return "ollama";
    }

    private AIResponse toAIResponse(
            Response response, AIProviderConfig config, Instant requestStart) {
        Instant responseEnd = Instant.now();
        AIResponse aiResponse = new AIResponse();
        aiResponse.setProviderId(getProviderId());
        aiResponse.setModelId(config.getModelId());
        aiResponse.setGeneratedAt(response.getCreatedAt());
        aiResponse.setExplanation(response.getResponse());
        aiResponse.setTokenCount(response.getEvalCount());
        aiResponse.setProcessingTime(Duration.between(requestStart, responseEnd));
        return aiResponse;
    }
}

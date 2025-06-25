package com.redhat.podmortem.provider.openshift;

import com.redhat.podmortem.common.model.analysis.AnalysisResult;
import com.redhat.podmortem.common.model.provider.AIProvider;
import com.redhat.podmortem.common.model.provider.AIProviderConfig;
import com.redhat.podmortem.common.model.provider.AIResponse;
import com.redhat.podmortem.common.model.provider.ProviderCapabilities;
import com.redhat.podmortem.common.model.provider.ValidationResult;
import com.redhat.podmortem.provider.openshift.dto.Request;
import com.redhat.podmortem.provider.openshift.dto.Response;
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
@Named("openshift-ai")
public class OpenShiftAIProvider implements AIProvider {

    @Inject @RestClient OpenShiftAIClient restClient;

    @Override
    public Uni<AIResponse> generateExplanation(AnalysisResult result, AIProviderConfig config) {
        String userPrompt = "Analyze the following pod failure: " + result.toString();
        String systemPrompt =
                "You are a Kubernetes expert and will be given analysis of a failed pod. Provide a concise explanation.";

        var requestBody = new Request();
        requestBody.setModel(config.getModelId());
        requestBody.setStream(false);
        requestBody.setMaxTokens(500);
        requestBody.setTemperature(0.3);
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
    public ProviderCapabilities getCapabilities() {
        return new ProviderCapabilities(true, 4096, List.of("mistral-7b-instruct"), "Varies", 0.0);
    }

    @Override
    public String getProviderId() {
        return "openshift-ai";
    }

    private AIResponse toAIResponse(
            Response osResponse, AIProviderConfig config, Instant requestStart) {
        Instant responseEnd = Instant.now();
        AIResponse aiResponse = new AIResponse();
        aiResponse.setProviderId(getProviderId());
        aiResponse.setModelId(config.getModelId());
        aiResponse.setGeneratedAt(osResponse.getCreated());
        if (osResponse.getChoices() != null && !osResponse.getChoices().isEmpty()) {
            aiResponse.setExplanation(osResponse.getChoices().get(0).getMessage().get("content"));
        } else {
            aiResponse.setExplanation("No explanation could be generated.");
        }
        if (osResponse.getUsage() != null) {
            aiResponse.setTokenCount(osResponse.getUsage().getTotalTokens());
        }
        aiResponse.setProcessingTime(Duration.between(requestStart, responseEnd));
        return aiResponse;
    }
}

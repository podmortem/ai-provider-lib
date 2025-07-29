package com.redhat.podmortem.provider.service;

import com.redhat.podmortem.common.model.analysis.AnalysisResult;
import io.quarkus.qute.Engine;
import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service for managing prompt templates used by AI providers.
 *
 * @see com.redhat.podmortem.provider.openai.OpenAIProvider
 * @see com.redhat.podmortem.provider.ollama.OllamaProvider
 */
@ApplicationScoped
public class PromptTemplateService {

    private static final Logger log = LoggerFactory.getLogger(PromptTemplateService.class);

    @Inject
    @Location("system.txt")
    Template builtInSystemPromptTemplate;

    @Inject
    @Location("user.txt")
    Template builtInUserPromptTemplate;

    @Inject Engine engine;

    @ConfigProperty(
            name = "podmortem.prompts.external.path",
            defaultValue = "/etc/podmortem/prompts")
    public String externalPromptsPath;

    @ConfigProperty(name = "podmortem.prompts.external.enabled", defaultValue = "false")
    public boolean externalPromptsEnabled;

    /**
     * Gets the system prompt text for AI requests.
     *
     * <p>External prompts take precedence over built-in templates when enabled.
     *
     * @return the system prompt text, either from external file or built-in template
     */
    public String getSystemPrompt() {
        if (externalPromptsEnabled) {
            String externalPrompt = loadExternalPrompt("system.txt");
            if (externalPrompt != null) {
                log.debug("Using external system prompt from ConfigMap");
                return externalPrompt;
            }
        }

        log.debug("Using built-in system prompt");
        return builtInSystemPromptTemplate.render();
    }

    /**
     * Builds the user prompt text for AI requests using the provided analysis result.
     *
     * <p>Template receives the AnalysisResult object as a "result" variable.
     *
     * @param result the analysis result containing pod failure information
     * @return the formatted user prompt text with embedded analysis data
     * @throws RuntimeException if template processing fails with external templates
     */
    public String buildUserPrompt(AnalysisResult result) {
        if (externalPromptsEnabled) {
            String externalPrompt = loadExternalPrompt("user.txt");
            if (externalPrompt != null) {
                try {
                    log.debug("Using external user prompt from ConfigMap");
                    Template template = engine.parse(externalPrompt);
                    return template.data("result", result).render();
                } catch (Exception e) {
                    log.warn(
                            "Failed to parse external user prompt template, falling back to built-in: {}",
                            e.getMessage());
                }
            }
        }

        log.debug("Using built-in user prompt");
        return builtInUserPromptTemplate.data("result", result).render();
    }

    /**
     * Loads an external prompt template from the filesystem.
     *
     * @param filename the prompt filename to load (e.g., "system.txt", "user.txt")
     * @return the prompt content as a string, or null if not found or error occurs
     */
    private String loadExternalPrompt(String filename) {
        try {
            Path promptPath = Paths.get(externalPromptsPath, filename);
            if (Files.exists(promptPath)) {
                log.info("Loading external prompt: {}", promptPath);
                return Files.readString(promptPath);
            } else {
                log.debug("External prompt file not found: {}", promptPath);
            }
        } catch (IOException e) {
            log.warn("Failed to load external prompt {}: {}", filename, e.getMessage());
        }
        return null;
    }
}

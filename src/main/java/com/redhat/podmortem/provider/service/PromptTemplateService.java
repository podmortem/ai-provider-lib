package com.redhat.podmortem.provider.service;

import com.redhat.podmortem.common.model.analysis.AnalysisResult;
import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PromptTemplateService {

    @Inject
    @Location("system.txt")
    Template systemPromptTemplate;

    @Inject
    @Location("user.txt")
    Template userPromptTemplate;

    public String getSystemPrompt() {
        return systemPromptTemplate.render();
    }

    public String buildUserPrompt(AnalysisResult result) {
        return userPromptTemplate.data("result", result).render();
    }
}

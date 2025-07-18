package com.redhat.podmortem.provider.ollama;

import com.redhat.podmortem.provider.ollama.dto.Request;
import com.redhat.podmortem.provider.ollama.dto.Response;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(baseUri = "http://localhost:11434") // overridden at runtime
public interface OllamaClient {

    @POST
    @Path("/api/generate")
    Uni<Response> getCompletion(Request requestBody);
}

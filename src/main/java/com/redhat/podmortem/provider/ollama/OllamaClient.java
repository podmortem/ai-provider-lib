package com.redhat.podmortem.provider.ollama;

import com.redhat.podmortem.provider.ollama.dto.Request;
import com.redhat.podmortem.provider.ollama.dto.Response;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.POST;
import java.net.URI;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(baseUri = "http://localhost:11434") // overridden at runtime
public interface OllamaClient {

    @POST
    Uni<Response> getCompletion(URI baseUri, Request requestBody);
}

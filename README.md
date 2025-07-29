# ai-provider-lib

A Quarkus-based library providing AI integrations for generating explanations of Kubernetes pod failure analysis results.

## Overview

This library abstracts different AI providers behind a common interface, allowing the Podmortem system to generate human-readable explanations for pod failures using various AI services.

## Supported Providers

- **OpenAI** - OpenAI's completion API framework, useable with many providers (vLLM, Gemini, ect.)
- **Ollama** - Local AI models

## Configuration

External prompts can be configured via:
- `podmortem.prompts.external.enabled` - Enable external prompt loading
- `podmortem.prompts.external.path` - Path to external prompt files (default: `/etc/podmortem/prompts`)

## Building

```bash
./mvnw package
```

For native compilation:
```bash
./mvnw package -Dnative
```

package com.opencerts.certification.request;

import java.util.List;
import java.util.UUID;

public record AnswerFormDTO(
        UUID questionId,
        String testIdentifier,
        List<String> selectedOptions
) {
}

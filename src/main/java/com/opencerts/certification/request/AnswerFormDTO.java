package com.opencerts.certification.request;

import java.util.List;

public record AnswerFormDTO(
        String questionId,
        String testIdentifier,
        List<String> selectedOptions
) {
}

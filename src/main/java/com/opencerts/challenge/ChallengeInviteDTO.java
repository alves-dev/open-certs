package com.opencerts.challenge;

import com.opencerts.shared.UserDTO;

import java.util.List;

public record ChallengeInviteDTO(
        String id,
        String name,
        String creatorName,
        String certificationName,
        int totalQuestions,
        List<UserDTO> participants
) {
    public ChallengeInviteDTO(Challenge challenge, List<UserDTO> participants) {
        this(
                challenge.id(),
                challenge.name(),
                challenge.createBy().name(),
                challenge.certification().displayName(),
                challenge.questionIds().size(),
                participants
        );
    }
}

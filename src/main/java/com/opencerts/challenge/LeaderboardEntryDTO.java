package com.opencerts.challenge;

public record LeaderboardEntryDTO(
        String userId,
        String userName,
        int score,
        boolean finished,
        int questionsAnswered,
        boolean isCurrentUser
) {
}

package com.opencerts.util;

public class Page {

    private Page() {
        throw new IllegalStateException("Utility class");
    }

    public static final String HOME = "index";

    public static final String QUESTION = "question";
    public static final String QUESTION_FORM = "question-form";

    public static final String TESTS = "tests";
    public static final String TESTS_DETAILS = "test-details";

    public static final String CHALLENGE = "challenge/list";
    public static final String CHALLENGE_INVITE = "challenge/invite";
    public static final String CHALLENGE_LEADERBOARD = "challenge/leaderboard";
    public static final String CHALLENGE_QUIZ = "challenge/quiz";

    public static final String STATS = "stats";

    public static final String DOCUMENTATION = "docs";
    public static final String ERROR = "error";
    public static final String LOGIN = "login";

    public static final String PRIVACY = "privacy";
    public static final String TERMS = "terms";

}

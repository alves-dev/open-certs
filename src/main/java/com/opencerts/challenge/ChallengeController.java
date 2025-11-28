package com.opencerts.challenge;

import com.opencerts.certification.CertificationService;
import com.opencerts.certification.Question;
import com.opencerts.shared.UserDTO;
import com.opencerts.user.User;
import com.opencerts.user.UserService;
import com.opencerts.util.Page;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/challenges")
public class ChallengeController {

    private final ChallengeService challengeService;
    private final CertificationService certificationService;
    private final UserService userService;

    public ChallengeController(ChallengeService challengeService, CertificationService certificationService,
                               UserService userService) {
        this.challengeService = challengeService;
        this.certificationService = certificationService;
        this.userService = userService;
    }

    @GetMapping
    public String listChallenges(Model model) {
        List<ChallengeDTO> myChallenges = challengeService.findForCurrentUser();

        model.addAttribute("challenges", myChallenges);
        model.addAttribute("certifications", certificationService.listAllToDTO());

        return Page.CHALLENGE;
    }

    @PostMapping
    public String createChallenge(@RequestParam String name,
                                  @RequestParam String certificationId,
                                  @RequestParam int numberOfQuestions) {
        Challenge challenge = challengeService.create(name, certificationId, numberOfQuestions);
        return "redirect:/challenges/" + challenge.id();
    }

    @GetMapping("/invite/{challengeId}")
    public String invite(Model model, HttpServletRequest request, @PathVariable String challengeId) {
        ChallengeInviteDTO challengeInvite = challengeService.getChallengeInviteDTO(challengeId);
        model.addAttribute("challenge", challengeInvite);

        String currentUrl = request.getRequestURL().toString();
        model.addAttribute("shareUrl", currentUrl);

        User user = userService.getCurrent();
        if (user == null) return Page.CHALLENGE_INVITE;

        var contains = challengeInvite.participants()
                .stream().map(UserDTO::id)
                .toList().contains(user.id());
        if (contains)
            return "redirect:/challenges/" + challengeId;

        return Page.CHALLENGE_INVITE;
    }

    @PostMapping("/{challengeId}/join")
    public String acceptInvite(@PathVariable String challengeId) {
        challengeService.acceptInvite(challengeId);
        return "redirect:/challenges/" + challengeId;
    }

    @GetMapping("/{challengeId}")
    public String leaderboars(Model model, @PathVariable String challengeId) {
        var userCurrent = userService.getCurrent();

        Challenge challenge = challengeService.findById(challengeId);

        boolean isParticipant = challenge.progressByUser().keySet().stream()
                .anyMatch(userId -> userId.equals(userCurrent.id()));
        if (!isParticipant) {
            model.addAttribute("errorMessage", "Challenge not found or you are not a participant.");
            model.addAttribute("status", "400");
            return Page.ERROR;
        }

        ChallengeDTO challengeDTO = new ChallengeDTO(
                challenge.id(),
                challenge.name(),
                challenge.certification().displayName(),
                challenge.questionIds().size(),
                challenge.progressByUser().get(userCurrent.id()).questionsAnswered().size(),
                challenge.progressByUser().size(),
                challenge.status()
        );

        List<LeaderboardEntryDTO> leaderboard = new ArrayList<>();
        challenge.progressByUser().forEach(
                (userId, progress) -> {
                    UserDTO user = userService.getDTOById(userId);

                    LeaderboardEntryDTO entry = new LeaderboardEntryDTO(
                            userId,
                            user.name(),
                            progress.score(challenge.questionIds().size()),
                            progress.isFinished(),
                            progress.questionsAnswered().size(),
                            userId.equals(userCurrent.id())
                    );
                    leaderboard.add(entry);
                }
        );

        // Ordena o leaderboard por score (maior para menor) e depois por questionsAnswered (maior para menor)
        leaderboard.sort((a, b) -> {
            if (b.score() != a.score()) {
                return Integer.compare(b.score(), a.score());
            } else {
                return Integer.compare(b.questionsAnswered(), a.questionsAnswered());
            }
        });

        String challengeStatus = "ACCEPTED";
        if (challenge.progressByUser().get(userCurrent.id()).isFinished())
            challengeStatus = "FINISHED";
        else if (!challenge.progressByUser().get(userCurrent.id()).questionsAnswered().isEmpty())
            challengeStatus = "IN_PROGRESS";

        model.addAttribute("challenge", challengeDTO);
        model.addAttribute("leaderboard", leaderboard);
        model.addAttribute("challengeStatus", challengeStatus);

        return Page.CHALLENGE_LEADERBOARD;
    }

    @GetMapping("/{challengeId}/questions")
    public String questions(Model model, @PathVariable String challengeId) {
        Challenge challenge = challengeService.findById(challengeId);
        Challenge.ChallengeProgress progress = challenge.progressByUser().get(userService.getCurrent().id());

        model.addAttribute("challenge", new ChallengeQuestionStatusDTO(
                challenge.id(),
                challenge.name(),
                challenge.questionIds().size(),
                progress.questionsAnswered().size() + 1,
                progress.questionsCorrect().size(),
                progress.questionsIncorrect().size(),
                progress.questionsAnswered().size(),
                100 * progress.questionsAnswered().size() / challenge.questionIds().size()
        ));

        Question question = challengeService.getNextQuestion(challengeId);
        if (question == null)
            return "redirect:/challenges/" + challengeId;

        model.addAttribute("question", question);
        model.addAttribute("answers", question.answersText());

        return Page.CHALLENGE_QUIZ;
    }

    @PostMapping("/{challengeId}/answer")
    public String submitChallengeAnswer(@PathVariable String challengeId,
                                        @RequestParam(required = false) List<String> selectedOptions,
                                        @RequestParam(required = false) String questionId,
                                        @RequestParam(required = false) String action
    ) {
        if ("skip".equals(action))
            return "redirect:/challenges/" + challengeId + "/questions";

        challengeService.submitAnswer(challengeId, questionId, selectedOptions);
        return "redirect:/challenges/" + challengeId + "/questions";
    }
}
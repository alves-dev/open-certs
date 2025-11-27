package com.opencerts.challenge;

import com.opencerts.certification.CertificationService;
import com.opencerts.shared.UserDTO;
import com.opencerts.user.UserService;
import com.opencerts.util.Page;
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
        challengeService.create(name, certificationId, numberOfQuestions);
        return "redirect:/" + Page.CHALLENGE;
    }

    @GetMapping("/invite/{challengeId}")
    public String invite(Model model, @PathVariable String challengeId) {
        ChallengeInviteDTO challengeInvite = challengeService.getChallengeInviteDTO(challengeId);
        model.addAttribute("challenge", challengeInvite);

        var contains = challengeInvite.participants()
                .stream().map(UserDTO::id)
                .toList().contains(userService.getCurrent().id());
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

        Challenge challenge = challengeService.findById(challengeId);
        ChallengeDTO dto = new ChallengeDTO(
                challenge.id(),
                challenge.name(),
                challenge.certification().displayName(),
                challenge.questionIds().size(),
                0,
                challenge.progressByUser().size()
        );

        List<LeaderboardEntryDTO> leaderboard = new ArrayList<>();

        var userCurrent = userService.getCurrent();

        challenge.progressByUser().forEach(
                (userId, progress) -> {
                    UserDTO user = userService.getDTOById(userId);

                    LeaderboardEntryDTO entry = new LeaderboardEntryDTO(
                            userId,
                            user.name(),
                            progress.score(challenge.questionIds().size()),
                            progress.finished(),
                            progress.questionsAnswered().size(),
                            userId.equals(userCurrent.id())
                    );
                    leaderboard.add(entry);
                }
        );

        boolean contains = leaderboard.stream().map(LeaderboardEntryDTO::userId).toList().contains(userCurrent.id());
        if (!contains) {
            model.addAttribute("errorMessage", "Challenge not found or you are not a participant.");
            model.addAttribute("status", "400");
            return Page.ERROR;
        }

        model.addAttribute("challenge", dto);
        model.addAttribute("leaderboard", leaderboard);

        return Page.CHALLENGE_LEADERBOARD;
    }
}
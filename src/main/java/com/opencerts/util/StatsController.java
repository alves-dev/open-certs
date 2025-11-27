package com.opencerts.util;

import com.opencerts.certification.QuestionService;
import com.opencerts.certification.response.CertificationQuestionCount;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
public class StatsController {

    private final QuestionService questionService;

    public StatsController(QuestionService questionService) {
        this.questionService = questionService;
    }

    record CertificationStatDTO(
            String provider,
            String certificationName,
            long questionCount,
            int percentageOfTotal
    ) {
    }

    @GetMapping("/stats")
    public String stats(Model model) {
        List<CertificationQuestionCount.DTO> certificationQuestionCountList = questionService.countByCertification();

        long totalQuestions = 0;
        long maxQuestionsInSingleCert = 0;

        for (CertificationQuestionCount.DTO c : certificationQuestionCountList) {
            long count = c.total();
            totalQuestions += count;
            if (count > maxQuestionsInSingleCert)
                maxQuestionsInSingleCert = count;
        }

        // 2. Monta a lista com a porcentagem calculada
        List<CertificationStatDTO> statsList = new ArrayList<>();
        for (CertificationQuestionCount.DTO c : certificationQuestionCountList) {
            long count = c.total();

            // Regra de três simples para a largura da barra
            // Se a cert com mais questões tem 50, e esta tem 25, a barra será 50%
            int percent = (maxQuestionsInSingleCert > 0)
                    ? (int) ((count * 100) / maxQuestionsInSingleCert)
                    : 0;

            statsList.add(new CertificationStatDTO(c.certificationProvider(), c.certificationDisplayName(), count, percent));
        }

        statsList.sort(Comparator.comparingLong(CertificationStatDTO::questionCount).reversed());

        model.addAttribute("totalCertifications", certificationQuestionCountList.size());
        model.addAttribute("totalQuestions", totalQuestions);
        model.addAttribute("certificationStats", statsList);

        return Page.STATS;
    }
}

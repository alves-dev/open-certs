package com.opencerts;

import com.opencerts.certification.Certification;
import com.opencerts.certification.CertificationService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DataInit {

    private final CertificationService certificationService;

    public DataInit(CertificationService certificationService) {
        this.certificationService = certificationService;
    }

    @PostConstruct
    void insert() {
        certificationService.saveUpdate(new Certification("AWS", "Cloud Practitioner", "FOUNDATIONAL"));
        certificationService.saveUpdate(new Certification("AWS", "AI Practitioner", "FOUNDATIONAL"));

        certificationService.saveUpdate(new Certification("AWS", "Solutions Architect", "ASSOCIATE"));
        certificationService.saveUpdate(new Certification("AWS", "Machine Learning Engineer", "ASSOCIATE"));
        certificationService.saveUpdate(new Certification("AWS", "CloudOps Engineer", "ASSOCIATE"));
        certificationService.saveUpdate(new Certification("AWS", "Developer", "ASSOCIATE"));
        certificationService.saveUpdate(new Certification("AWS", "Data Engineer", "ASSOCIATE"));

        certificationService.saveUpdate(new Certification("AWS", "Generative AI Developer", "PROFESSONAL"));
        certificationService.saveUpdate(new Certification("AWS", "Solutions Architect", "PROFESSONAL"));
        certificationService.saveUpdate(new Certification("AWS", "DevOps Engineer", "PROFESSONAL"));

        certificationService.saveUpdate(new Certification("AWS", "Machine Learning", "SPECIALTY"));
        certificationService.saveUpdate(new Certification("AWS", "Advanced Networking", "SPECIALTY"));
        certificationService.saveUpdate(new Certification("AWS", "Security", "SPECIALTY"));
    }
}

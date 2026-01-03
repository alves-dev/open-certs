package com.opencerts.util;

import com.opencerts.certification.Certification;
import com.opencerts.certification.CertificationService;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class DataTestInit {

    private final CertificationService certificationService;

    public DataTestInit(CertificationService certificationService) {
        this.certificationService = certificationService;
    }

    @PostConstruct
    void insert() {
        certificationService.saveUpdate(new Certification("AWS", "Cloud Practitioner", "FOUNDATIONAL"));

        certificationService.saveUpdate(new Certification("AWS", "Solutions Architect", "ASSOCIATE"));
        certificationService.saveUpdate(new Certification("AWS", "CloudOps Engineer", "ASSOCIATE"));
        certificationService.saveUpdate(new Certification("AWS", "Developer", "ASSOCIATE"));

        certificationService.saveUpdate(new Certification("AWS", "Advanced Networking", "SPECIALTY"));
        certificationService.saveUpdate(new Certification("AWS", "Security", "SPECIALTY"));

        certificationService.saveUpdate(new Certification("ALVES-DEV", "Test", "HARD"));
    }
}

package com.opencerts.certification;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CertificationService {

    private final CertificationRepository repository;

    public CertificationService(CertificationRepository repository) {
        this.repository = repository;
    }

    public List<Certification> listAll() {
        return repository.findAll();
    }

    public void saveUpdate(Certification certification) {
        repository.findById(certification.id())
                .orElseGet(() -> repository.save(certification));
    }

    public Certification getById(String certificationId) {
        return repository.findById(certificationId).get();
    }
}

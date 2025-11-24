package com.opencerts.certification;

import com.opencerts.certification.response.CertificationDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CertificationService {

    private final CertificationRepository repository;
    private List<Certification> localCache = null;

    public CertificationService(CertificationRepository repository) {
        this.repository = repository;
        loadCache();
    }

    public List<CertificationDTO> listAll() {
        return localCache.stream().map(CertificationDTO::new).toList();
    }

    public void saveUpdate(Certification certification) {
        repository.findById(certification.id())
                .orElseGet(() -> repository.save(certification));
        loadCache();
    }

    public Optional<Certification> getById(String certificationId) {
        return localCache.stream()
                .filter(certification -> Objects.equals(certification.id(), certificationId))
                .findFirst();
    }

    private void loadCache() {
        if (localCache == null) {
            localCache = repository.findAll();
        }
    }
}

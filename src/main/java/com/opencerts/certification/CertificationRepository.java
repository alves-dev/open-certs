package com.opencerts.certification;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CertificationRepository extends MongoRepository<Certification, String> {
}

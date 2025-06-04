package com.example.healthme.domain.admin.repository;

import com.example.healthme.domain.admin.entity.TransactionHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TranscationRepository extends JpaRepository<TransactionHistoryEntity,Integer> {

    Page<TransactionHistoryEntity> findByTranscationPeopleContaining(String searchText, Pageable pageable);

}

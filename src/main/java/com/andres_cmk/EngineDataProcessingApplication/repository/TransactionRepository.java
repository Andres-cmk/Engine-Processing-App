package com.andres_cmk.EngineDataProcessingApplication.repository;

import com.andres_cmk.EngineDataProcessingApplication.domain.entity.Transaction;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface TransactionRepository extends JpaRepository<Transaction,Long> {
}

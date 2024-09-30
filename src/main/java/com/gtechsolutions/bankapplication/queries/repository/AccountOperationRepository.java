package com.gtechsolutions.bankapplication.queries.repository;

import com.gtechsolutions.bankapplication.queries.entity.AccountOperation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountOperationRepository extends JpaRepository<AccountOperation, String> {
}

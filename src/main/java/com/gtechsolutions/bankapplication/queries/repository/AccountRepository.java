package com.gtechsolutions.bankapplication.queries.repository;

import com.gtechsolutions.bankapplication.queries.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,String> {
}

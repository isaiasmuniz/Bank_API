package com.muniz.isaias.bank_Api_restFull.repository;

import com.muniz.isaias.bank_Api_restFull.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}

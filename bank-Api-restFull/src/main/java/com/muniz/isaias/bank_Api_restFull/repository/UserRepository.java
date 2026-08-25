package com.muniz.isaias.bank_Api_restFull.repository;

import com.muniz.isaias.bank_Api_restFull.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}

package com.empyrean.auth.repository;

import com.empyrean.auth.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by chirdeep on 18/11/2016.
 */

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUsername(String userName);
}

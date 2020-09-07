package com.soaint.repository;

import com.soaint.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<Currency,Long>, JpaSpecificationExecutor<Currency> {
    Optional<Currency> findByMoney(String money);
    boolean existsByMoney(String money);
}

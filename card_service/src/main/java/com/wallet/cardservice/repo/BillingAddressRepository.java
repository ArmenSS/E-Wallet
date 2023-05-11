package com.wallet.cardservice.repo;

import com.wallet.cardservice.entity.BillingAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillingAddressRepository extends JpaRepository<BillingAddress, Long> {

    BillingAddress findByUserId(Long id);

}

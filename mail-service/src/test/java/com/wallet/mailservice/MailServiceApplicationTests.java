package com.wallet.mailservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = {"com.wallet.mailservice.repository"})
@SpringBootTest
class MailServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}

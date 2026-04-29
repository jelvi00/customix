package com.customix.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;
import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JPAConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {

        return () -> {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (Objects.nonNull(auth) && auth.isAuthenticated()) {
                var username = auth.getName();
                if (username.contains("anonymousUser")) username = "_system_";

                return Optional.of(username);
            }

            return Optional.empty();
        };

    }

}

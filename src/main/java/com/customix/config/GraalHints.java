package com.customix.config;

import org.springframework.aot.hint.*;

import java.util.List;
import java.util.UUID;

public class GraalHints implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {

        hints.reflection().registerType(
                TypeReference.of("org.hibernate.internal.log.DeprecationLogger_$logger"),
                builder -> builder.withConstructor(
                        List.of(TypeReference.of("org.jboss.logging.Logger")),
                        ExecutableMode.INVOKE
                )
        );

        hints.reflection().registerType(
                TypeReference.of("org.hibernate.internal.log.DeprecationLogger"),
                builder -> builder.withMembers(
                        MemberCategory.INVOKE_PUBLIC_METHODS,
                        MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS
                )
        );

        hints.reflection().registerType(UUID[].class,
                builder -> builder.withMembers(MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS));

        hints.reflection().registerType(UUID.class,
                builder -> builder.withMembers(MemberCategory.INVOKE_PUBLIC_METHODS));
    }

}

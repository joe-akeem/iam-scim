package com.airlock.iam.scim.config;

import com.airlock.iam.common.api.domain.model.store.user.UserStore;
import com.airlock.iam.core.misc.plugin.config.activation.ConfigActivator;
import com.airlock.iam.plugin.framework.application.service.license.LicenseManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.nio.file.Path;
import java.util.Optional;

import static com.airlock.iam.core.misc.plugin.config.activation.ConfigActivator.ContextHandling.DEFAULT_CONTEXT_ONLY;
import static com.airlock.iam.plugin.framework.misc.AppId.appId;

@Configuration
public class UserStoreConfig {

    @Bean
    public ConfigActivator configActivator(Optional<Path> activationDir, LicenseManager licenseManager) {
        return new ConfigActivator(
                appId("iam-scim"),
                DEFAULT_CONTEXT_ONLY,
                activationDir,
                licenseManager
        );
    }

    @Bean
    @Scope("prototype")
    public UserStore userStore(ConfigActivator configActivator) {
        return null;
    }
}

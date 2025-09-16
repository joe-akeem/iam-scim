package com.airlock.iam.scim.config;

import com.airlock.iam.common.api.domain.model.store.user.UserStore;
import com.airlock.iam.core.misc.plugin.config.activation.ConfigActivator;
import com.airlock.iam.license.domain.service.counter.InstanceCounter;
import com.airlock.iam.license.domain.service.loader.LicenseLoader;
import com.airlock.iam.plugin.framework.application.service.license.DefaultPluginConfigLicenseManager;
import com.airlock.iam.plugin.framework.application.service.license.LicenseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;

import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

import static com.airlock.iam.core.misc.plugin.config.activation.ConfigActivator.ContextHandling.DEFAULT_CONTEXT_ONLY;
import static com.airlock.iam.plugin.framework.misc.AppId.appId;

@Configuration
public class UserStoreConfig {

    @SuppressWarnings("LoggerInitializedWithForeignClass")
    public static final Logger LICENSE_MANAGER_LOGGER = LoggerFactory.getLogger(DefaultPluginConfigLicenseManager.class);

    @Bean(initMethod = "autoActivateOnConfigFileChanges")
    public ConfigActivator configActivator(Optional<Path> activationDir, LicenseManager licenseManager, ScimAppMedusaConfiguration configuration) {
        ConfigActivator configActivator = new ConfigActivator(
                appId("loginapp"),
                DEFAULT_CONTEXT_ONLY,
                activationDir,
                licenseManager
        );
        configActivator.loadPluginTreeAsync(configuration);
        return configActivator;
    }

    @Bean
    public LicenseManager licenseManager(LicenseLoader licenseLoader, InstanceCounter instanceCounter) {
        return new DefaultPluginConfigLicenseManager(licenseLoader, instanceCounter, LICENSE_MANAGER_LOGGER::info);
    }

    @Bean
    private static LicenseLoader licenseLoader (@Value("${iam.license}") Path licenseFile) {
        return LicenseLoader.forFile(licenseFile);
    }

    @Bean InstanceCounter instanceCounter() {
        return () -> 1;
    }

    @Bean
    public ScimAppMedusaConfiguration configuration() {
        return new ScimAppMedusaConfiguration();
    }

    @Bean
    @RequestScope()
    public UserStore userStore(ScimAppMedusaConfiguration scimAppMedusaConfiguration) throws InterruptedException {
       return Objects.requireNonNull(scimAppMedusaConfiguration.userStore(), "No UserStore found. Config activated?");
    }
}

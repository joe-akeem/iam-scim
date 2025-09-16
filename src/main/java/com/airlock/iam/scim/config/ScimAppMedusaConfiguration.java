package com.airlock.iam.scim.config;

import com.airlock.iam.common.api.domain.model.store.user.UserStore;
import com.airlock.iam.core.misc.plugin.config.activation.ConfigurationRootContainer;
import com.airlock.iam.login.app.misc.configuration.Loginapp;
import com.airlock.iam.plugin.api.config.ConfigurationContext;

import java.util.Map;

import static com.airlock.iam.plugin.api.config.ConfigurationContext.defaultContext;

public class ScimAppMedusaConfiguration implements ConfigurationRootContainer {

    private boolean licensed = false;
    private UserStore userStore;

    @Override
    public void setLicensed(boolean licensed) {
        this.licensed = licensed;
    }

    @Override
    public boolean isLicensed() {
        return licensed;
    }

    @Override
    public void activate(Map<ConfigurationContext, Object> configRoots) {
        Loginapp loginapp = (Loginapp) configRoots.get(defaultContext());
        this.userStore = loginapp.getUserStore().orElseThrow(() -> new IllegalStateException("No UserStore configured"));
    }

    @Override
    public void deactivate() {
    }

    public UserStore userStore() {
        return userStore;
    }
}

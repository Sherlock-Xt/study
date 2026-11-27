package com.masterslave.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DataSourceConfig {
    @Value("${slave.datasource.driver-class-name}")
    private String slaveDriverClassname;
    @Value("${slave.datasource.url}")
    private String slaveUrl;
    @Value("${slave.datasource.username}")
    private String slaveUsername;
    @Value("${slave.datasource.password}")
    private String slavePassword;
    @Value("${master.datasource.driver-class-name}")
    private String masterDriverClassname;
    @Value("${master.datasource.url}")
    private String masterUrl;
    @Value("${master.datasource.username}")
    private String masterUsername;
    @Value("${master.datasource.password}")
    private String masterPassword;

    public String getSlaveDriverClassname() {
        return slaveDriverClassname;
    }

    public void setSlaveDriverClassname(String slaveDriverClassname) {
        this.slaveDriverClassname = slaveDriverClassname;
    }

    public String getSlaveUrl() {
        return slaveUrl;
    }

    public void setSlaveUrl(String slaveUrl) {
        this.slaveUrl = slaveUrl;
    }

    public String getSlaveUsername() {
        return slaveUsername;
    }

    public void setSlaveUsername(String slaveUsername) {
        this.slaveUsername = slaveUsername;
    }

    public String getSlavePassword() {
        return slavePassword;
    }

    public void setSlavePassword(String slavePassword) {
        this.slavePassword = slavePassword;
    }

    public String getMasterDriverClassname() {
        return masterDriverClassname;
    }

    public void setMasterDriverClassname(String masterDriverClassname) {
        this.masterDriverClassname = masterDriverClassname;
    }

    public String getMasterUrl() {
        return masterUrl;
    }

    public void setMasterUrl(String masterUrl) {
        this.masterUrl = masterUrl;
    }

    public String getMasterUsername() {
        return masterUsername;
    }

    public void setMasterUsername(String masterUsername) {
        this.masterUsername = masterUsername;
    }

    public String getMasterPassword() {
        return masterPassword;
    }

    public void setMasterPassword(String masterPassword) {
        this.masterPassword = masterPassword;
    }
}

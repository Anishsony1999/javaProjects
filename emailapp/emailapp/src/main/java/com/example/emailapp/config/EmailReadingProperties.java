package com.example.emailapp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "email.reading")
public class EmailReadingProperties {
    private String host;
    private int port;
    private String username;
    private String password;
    private String protocol;
    private String folder;
    private long checkInterval;

    // Constructors
    public EmailReadingProperties() {}

    // Getters and Setters
    public String getHost() { return host; }
    public void setHost(String host) { this.host = host; }

    public int getPort() { return port; }
    public void setPort(int port) { this.port = port; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getProtocol() { return protocol; }
    public void setProtocol(String protocol) { this.protocol = protocol; }

    public String getFolder() { return folder; }
    public void setFolder(String folder) { this.folder = folder; }

    public long getCheckInterval() { return checkInterval; }
    public void setCheckInterval(long checkInterval) { this.checkInterval = checkInterval; }
}
package model;

import java.util.Map;

public class User {
    String username;
    String password;
    long sessionId;
    Account accountType;

    public User(String username, String password, Account accountType) {
        this.username = username;
        this.password = password;
        this.sessionId = this.hashCode();
        this.accountType = accountType;
    }

    public User(Map<String, Object> propertiesMap) {
        username = propertiesMap.get("username").toString();
        password = propertiesMap.get("password").toString();
        sessionId = (long) propertiesMap.get("sessionId");
        accountType = Account.valueOf(propertiesMap.get("accountType").toString());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public Account getAccountType() {
        return accountType;
    }

    public void setAccountType(Account accountType) {
        this.accountType = accountType;
    }
}

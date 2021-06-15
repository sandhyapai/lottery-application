package com.bynder.lotteryapplication.model;

import org.springframework.data.domain.Persistable;

import javax.persistence.*;

@Entity
@Table(name = "lotteryUser")
public class LotteryUser implements Persistable<String> {

    @Id
    private String userName;

    private String password;

    private String name;

    @Transient
    private boolean update;

    public LotteryUser(String userName, String password, String name) {
        this.userName = userName;
        this.password = password;
        this.name = name;
    }

    public LotteryUser() {
    }

    @Override
    public String getId() {
        return userName;
    }

    @Override
    public boolean isNew() {
        return !this.update;
    }

    @PrePersist
    @PostLoad
    void markUpdated() {
        this.update = true;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getPassword() {
        return this.password;
    }

    public String getName() {
        return this.name;
    }

    public boolean isUpdate() {
        return this.update;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof LotteryUser)) return false;
        final LotteryUser other = (LotteryUser) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$userName = this.getUserName();
        final Object other$userName = other.getUserName();
        if (this$userName == null ? other$userName != null : !this$userName.equals(other$userName)) return false;
        final Object this$password = this.getPassword();
        final Object other$password = other.getPassword();
        if (this$password == null ? other$password != null : !this$password.equals(other$password)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        if (this.isUpdate() != other.isUpdate()) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof LotteryUser;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $userName = this.getUserName();
        result = result * PRIME + ($userName == null ? 43 : $userName.hashCode());
        final Object $password = this.getPassword();
        result = result * PRIME + ($password == null ? 43 : $password.hashCode());
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        result = result * PRIME + (this.isUpdate() ? 79 : 97);
        return result;
    }

    public String toString() {
        return "LotteryUser(userName=" + this.getUserName() + ", password=" + this.getPassword() + ", name=" + this.getName() + ", update=" + this.isUpdate() + ")";
    }
}

package de.kaufland.ksilence.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Contact {

    @Id
    @GeneratedValue
    private long id;
    @NotNull
    private String name;
    @NotNull
    private String registrationToken;
    @NotNull
    private OperatingSystem os;
    @NotNull
    private boolean isAvailable;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegistrationToken() {
        return registrationToken;
    }

    public void setRegistrationToken(String registrationToken) {
        this.registrationToken = registrationToken;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public OperatingSystem getOs() {
        return os;
    }

    public void setOs(OperatingSystem os) {
        this.os = os;
    }

    @Override
    public String toString(){
        return "[Contact(" + id + "): " +
                " name=" + this.name +
                " registrationToken=" + this.registrationToken +
                " os=" + this.os +
                " isAvailable=" + this.isAvailable +
                " ]";
    }
}

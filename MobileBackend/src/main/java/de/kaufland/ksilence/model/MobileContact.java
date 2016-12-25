package de.kaufland.ksilence.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class MobileContact {

    @Id
    @GeneratedValue
    private long id;
    @NotNull
    private String name;
    @NotNull
    private String registration_token;
    @NotNull
    private OperatingSystem os;
    @NotNull
    private boolean is_available;
    
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
        return registration_token;
    }

    public void setRegistrationToken(String registrationToken) {
        this.registration_token = registrationToken;
    }

    public boolean isAvailable() {
        return is_available;
    }

    public void setAvailable(boolean available) {
        is_available = available;
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
                " registrationToken=" + this.registration_token +
                " os=" + this.os +
                " isAvailable=" + this.is_available +
                " ]";
    }
}

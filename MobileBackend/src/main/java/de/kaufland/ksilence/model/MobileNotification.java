package de.kaufland.ksilence.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class MobileNotification {

    @Id
    @GeneratedValue
    private long id;
    @NotNull
    private long toContactId;
    @NotNull
    private long fromContactId;
    @NotNull
    private String fromContactName;
    @NotNull
    private String body;

    private Status state;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getToContactId() {
        return toContactId;
    }

    public void setToContactId(long toContactId) {
        this.toContactId = toContactId;
    }

    public long getFromContactId() {
        return fromContactId;
    }

    public void setFromContactId(long fromContactId) {
        this.fromContactId = fromContactId;
    }

    public void setState(Status state) {
        this.state = state;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Status getState() {
        return state;
    }

    public String getFromContactName() {
        return fromContactName;
    }

    public void setFromContactName(String fromContactName) {
        this.fromContactName = fromContactName;
    }
}

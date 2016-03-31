package com.infinityworks.webapp.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "record_vote_log")
public class RecordVoteLog extends BaseEntity {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "users_id")
    private User user;

    @JoinColumn(name = "wards_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Ward ward;

    private String ern;

    @Temporal(TemporalType.TIMESTAMP)
    private Date added;

    public RecordVoteLog() {
        this.added = new Date();
    }

    public Ward getWard() {
        return ward;
    }

    public void setWard(Ward ward) {
        this.ward = ward;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getErn() {
        return ern;
    }

    public void setErn(String ern) {
        this.ern = ern;
    }

    public Date getAdded() {
        return added;
    }

    public void setAdded(Date date) {
        this.added = date;
    }
}

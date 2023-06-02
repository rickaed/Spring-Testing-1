package com.spring_test.spring_test_one.entity;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;

@Entity
public class Fire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Min(value = 0, message = "severity must be positive")
    private int severity;
    private Instant date;

    @ManyToOne
    @JoinColumn(name="fireman_id")
    private Fireman fireman;

    public Fire() {
    }

    public Fire(int severity,
            Instant date) {
        this.date = date;
        this.severity = severity;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getSeverity() {
        return this.severity;
    }

    public void setSeverity(int severity) {
        this.severity = severity;
    }

    public Instant getDate() {
        return this.date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Fireman getFireman() {
        return this.fireman;
    }

    public void setFireman(Fireman fireman) {
        this.fireman = fireman;
    }

    @Override
    public String toString() {
        return "Fire{" +
                "id=" + id +
                ", severity=" + severity +
                ", date=" + date +
                ", fireman=" + fireman +
                '}';
    }
    
}

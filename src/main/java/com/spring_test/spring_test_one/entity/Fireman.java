package com.spring_test.spring_test_one.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Fireman {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "fireman")
    private List<Fire> fires;

    public Fireman() {
    }

    public Fireman(String name) {
        this.name = name;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Fire> getFires() {
        return this.fires;
    }

    public void setFires(List<Fire> fires) {
        this.fires = fires;
    }

    @Override
    public String toString() {
        return "Fireman{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", fires=" + fires +
                '}';
    }

}

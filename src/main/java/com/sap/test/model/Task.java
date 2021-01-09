package com.sap.test.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Getter
@Setter
@Entity
public class Task {

    @Id
    private String id;

    @NotBlank
    @Size(min = 4, max = 250)
    @Column(unique = true, length = 250)
    private String name;

    @NotBlank
    @Size(min = 10, max = 30)
    @Column(length = 30)
    private String recurrency;

    @NotBlank
    @Size(min = 10, max = 1000)
    @Lob
    private String code;

    public Task() { }

    public Task(String name, String recurrency, String code) {
        this.name = name;
        this.recurrency = recurrency;
        this.code = code;
    }

    public Task(String id, String name, String recurrency, String code) {
        this.id = id;
        this.name = name;
        this.recurrency = recurrency;
        this.code = code;
    }
}

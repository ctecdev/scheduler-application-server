package com.sap.test.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Data
@Entity
public class Task {

    @Id
    private String id;
    @Column(unique = true, length = 250)
    private String name;
    @Column(length = 30)
    private String recurrency;
    @Lob
    private String code;

}

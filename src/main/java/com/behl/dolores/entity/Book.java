package com.behl.dolores.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "books")
@Data
public class Book implements Serializable {

    private static final long serialVersionUID = -8195832520871706503L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @Column(name = "name", nullable = true)
    private String name;

    @Column(name = "publication", nullable = true)
    private String publication;
    @Column(name = "author", nullable = true)
    private String author;
    @Column(name = "onloan", nullable=true)
    private boolean onloan;
    @Column(name = "sales", nullable = true)
    private Double sales;

}

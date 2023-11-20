package com.ll.sbb;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Hello
{
    @GeneratedValue @Id
    private int id;

    @Column
    private String name;
}

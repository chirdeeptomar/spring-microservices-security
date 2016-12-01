package com.empyrean.auth.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Chirdeep on 19/11/2016.
 */

@Data
@Entity
public class Privilege {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    public Privilege() {
    }

    public Privilege(String name) {
        this.name = name;
    }
}

package com.fluidmechanics.DPD.Models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
// @RequiredArgsConstructor
public class Appuser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String username;
    String password;
    String email;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToMany
    @JsonManagedReference
    List<Modeldata> modeldatas;
}

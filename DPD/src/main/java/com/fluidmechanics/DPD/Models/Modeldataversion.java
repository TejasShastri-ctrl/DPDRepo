package com.fluidmechanics.DPD.Models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Data
public class Modeldataversion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int versionNumber;
    private String filePath;
    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.LAZY) // This is fine; just don't expose "modeldata" in JSON
    @JoinColumn(name = "modeldata_id")
    @JsonBackReference
    private Modeldata modeldata;

    public String getVersionLabel() {
        return "v" + versionNumber;
    }
}

package com.fluidmechanics.DPD.Models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Data
public class Modeldataversion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int versionNumber;

    private String filePath;

    private String description;

    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.LAZY) // This is fine; just don't expose "modeldata" in JSON
    @JoinColumn(name = "modeldata_id")
    @JsonBackReference
    private Modeldata modeldata;

    public String getVersionLabel() {
        return "v" + versionNumber;
    }

}
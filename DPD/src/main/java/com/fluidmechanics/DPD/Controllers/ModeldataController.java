package com.fluidmechanics.DPD.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fluidmechanics.DPD.Models.Modeldata;
import com.fluidmechanics.DPD.Models.Modeldataversion;
import com.fluidmechanics.DPD.Services.ModeldataService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/modeldata")
@RequiredArgsConstructor
public class ModeldataController {

    private final ModeldataService modeldataService;

    @PostMapping
    public ResponseEntity<Modeldata> createNewModel(
            @RequestParam String name,
            @RequestParam MultipartFile file) throws IOException {
        return ResponseEntity.ok(modeldataService.createNewModel(name, file));
    }

    @PostMapping("/{id}/upload-version")
    public ResponseEntity<Modeldata> uploadNewVersion(
            @PathVariable Long id,
            @RequestParam MultipartFile file) throws IOException {
        return ResponseEntity.ok(modeldataService.saveNewVersion(id, file));
    }

    @GetMapping
    public ResponseEntity<List<Modeldata>> getAllModeldata() {
        return ResponseEntity.ok(modeldataService.getAllModeldata());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Modeldata> getModeldata(@PathVariable Long id) {
        return modeldataService.getModeldata(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/versions")
    public ResponseEntity<List<Modeldataversion>> getVersions(@PathVariable Long id) {
        return ResponseEntity.ok(modeldataService.getVersions(id));
    }

    // Used to be string earlier. will implement this in UI instead if I get the
    // time
    @PostMapping("/{id}/rollback")
    public ResponseEntity<Modeldata> rollbackToVersion(
            @PathVariable Long id,
            @RequestParam int versionNumber) {
        Modeldata rolledBack = modeldataService.rollbackToVersion(id, versionNumber);
        return ResponseEntity.ok(rolledBack);
    }
}

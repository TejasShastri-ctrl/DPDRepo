package com.fluidmechanics.DPD.Services;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fluidmechanics.DPD.Repos.ModeldataVersionRepository;
import com.fluidmechanics.DPD.Models.Modeldata;
import com.fluidmechanics.DPD.Models.Modeldataversion;
import com.fluidmechanics.DPD.Repos.ModeldataRepository;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ModeldataService {

    private final ModeldataVersionRepository versionRepository;
    private final ModeldataRepository modeldataRepository;

    private final String uploadDirPath = "C:/Users/Tejas Shastri/MyAppUploads/uploads";

    public Modeldata createNewModel(String name, MultipartFile file) throws IOException {
        Modeldata modeldata = new Modeldata();
        modeldata.setName(name);
        modeldata.setCreatedAt(LocalDateTime.now());
        modeldata.setUpdatedAt(LocalDateTime.now());
        modeldata.setFileType(file.getContentType());
    
        Modeldata savedModeldata = modeldataRepository.save(modeldata);
        return saveNewVersion(savedModeldata.getId(), file);
    }

    public Modeldata saveNewVersion(Long modeldataId, MultipartFile file) throws IOException {
        Optional<Modeldata> modeldataOpt = modeldataRepository.findById(modeldataId);
        if (modeldataOpt.isEmpty()) {
            throw new RuntimeException("Modeldata not found");
        }
    
        Modeldata modeldata = modeldataOpt.get();

        File uploadDir = new File(uploadDirPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
    
        int versionCount = versionRepository.countByModeldata(modeldata);
        int newVersionNumber = versionCount + 1;
    
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String filePath = uploadDirPath + File.separator + fileName;
    
        File dest = new File(filePath);
        file.transferTo(dest);
    
        // Create version entity
        Modeldataversion versionEntry = new Modeldataversion();
        versionEntry.setModeldata(modeldata);
        versionEntry.setVersionNumber(newVersionNumber);
        versionEntry.setFilePath(filePath);
        versionEntry.setTimestamp(LocalDateTime.now());
    
        versionRepository.save(versionEntry);
    
        if (modeldata.getVersions() == null) {
            modeldata.setVersions(new ArrayList<>());
        }
        modeldata.getVersions().add(versionEntry);
    
        modeldata.setCurrentVersion(versionEntry);
        modeldata.setUpdatedAt(LocalDateTime.now());
    
        return modeldataRepository.save(modeldata);
    }
    


    public List<Modeldata> getAllModeldata() {
        return modeldataRepository.findAll();
    }

    public Optional<Modeldata> getModeldata(Long id) {
        return modeldataRepository.findById(id);
    }

    public List<Modeldataversion> getVersions(Long modeldataId) {
        return versionRepository.findByModeldataIdOrderByVersionNumberDesc(modeldataId);
    }

    public Modeldata rollbackToVersion(Long modeldataId, int versionNumber) {
        Modeldata modeldata = modeldataRepository.findById(modeldataId)
                .orElseThrow(() -> new RuntimeException("Modeldata not found"));

        Modeldataversion version = versionRepository.findByModeldataIdAndVersionNumber(modeldataId, versionNumber)
                .orElseThrow(() -> new RuntimeException("Version not found"));

        modeldata.setCurrentVersion(version);
        modeldata.setUpdatedAt(LocalDateTime.now());

        return modeldataRepository.save(modeldata);
    }
}

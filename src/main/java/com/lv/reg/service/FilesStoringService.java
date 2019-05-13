package com.lv.reg.service;

import com.lv.reg.entities.Contract;
import com.lv.reg.formBean.ContractForm;

import com.lv.reg.service.storage.StorageProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

@Service

public class FilesStoringService {
    private StorageProperties storageProperties;
    private String basePath;

    public FilesStoringService(StorageProperties storageProperties) {
        this.basePath = storageProperties.getLocation();
    }

    public void saveFiles(ContractForm contractForm, Contract contract) {
        List<MultipartFile> customerDocuments = contractForm.getCustomerDocument();

        for(MultipartFile file: customerDocuments)
        try {
            if (!file.isEmpty()) {
                String originalFilename = file.getOriginalFilename();
                Path filePath = defineParentDirectory(contract).resolve(originalFilename);
                Files.write(filePath, file.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stream<Path> loadAll(Contract contract) {
        Path path = defineParentDirectory(contract);
        try {
            return Files.walk(path, 1)
                    .filter(p -> !p.equals(path))
                    .map(path::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read stored files", e);
        }
    }

    public Resource loadAsResource(Contract contract, String filename) {
        try {
            Path file = defineParentDirectory(contract).resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException(
                        "Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not read file: " + filename, e);
        }
    }

    private String generateName() {

        return null;
    }

    private Path defineParentDirectory(Contract contract) {
        int years = LocalDate.now().getYear() % 1000;
        Path customerRootDir = Paths.get(basePath, contract.getCustomer().getOrgName().replaceAll("\\s+", "_")+"_y" + years);
        if (Files.notExists(customerRootDir)) {
            try {
                Files.createDirectories(customerRootDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Path contractRootDir = customerRootDir.resolve(contract.getContractIdentifier());
        if (Files.notExists(contractRootDir)) {
            try {
                Files.createDirectories(contractRootDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return contractRootDir;
    }
}

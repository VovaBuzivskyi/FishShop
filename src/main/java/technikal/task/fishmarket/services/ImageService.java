package technikal.task.fishmarket.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import technikal.task.fishmarket.dtos.FishDto;
import technikal.task.fishmarket.models.Fish;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class ImageService {

    static final Logger log = LoggerFactory.getLogger(ImageService.class);

    public List<String> saveImages(FishDto fishDto, Date catchDate) {
        List<MultipartFile> images = List.of(
                fishDto.getFirstImageFile(),
                fishDto.getSecondImageFile(),
                fishDto.getThirdImageFile()
        );
        List<String> fileNames = new ArrayList<>();

        String uploadDir = "public/images/";
        Path uploadPath = Paths.get(uploadDir);

        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            for (MultipartFile multipartFile : images) {
                if (!multipartFile.isEmpty()) {
                    String storageFileName = catchDate.getTime() + "_" + multipartFile.getOriginalFilename();
                    try (InputStream inputStream = multipartFile.getInputStream()) {
                        Files.copy(inputStream, Paths.get(uploadDir + storageFileName), StandardCopyOption.REPLACE_EXISTING);
                        fileNames.add(storageFileName);
                        log.info("File {} saved", storageFileName);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error saving images", e);
        }

        return fileNames;
    }

    public void deleteImages(Fish fish) {
        List<String> fileNames = Stream.of(
                fish.getFirstImageFileName(),
                fish.getSecondImageFileName(),
                fish.getThirdImageFileName()
        ).filter(Objects::nonNull).toList();

        fileNames.forEach(name -> {
            Path imagePath = Paths.get("public/images/" + name);
            try {
                Files.delete(imagePath);
                log.info("Deleted image {}", name);
            } catch (IOException ex) {
                log.error("Error deleting file {}: {}", name, ex.getMessage());
            }
        });
    }
}

package technikal.task.fishmarket.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import technikal.task.fishmarket.dtos.FishDto;
import technikal.task.fishmarket.exceptions.ProcessImageException;
import technikal.task.fishmarket.models.Fish;

import java.io.IOException;
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
    static final String UPLOAD_DIR = "public/images/";

    public List<String> saveImages(FishDto fishDto, Date catchDate) {
        List<MultipartFile> images = List.of(
                fishDto.getFirstImageFile(),
                fishDto.getSecondImageFile(),
                fishDto.getThirdImageFile()
        );
        List<String> fileNames = new ArrayList<>();
        Path uploadPath = Paths.get(UPLOAD_DIR);

        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            for (MultipartFile multipartFile : images) {
                if (!multipartFile.isEmpty()) {
                    String storageFileName = catchDate.getTime() + "_" + multipartFile.getOriginalFilename();
                    Files.copy(multipartFile.getInputStream(), Paths.get(UPLOAD_DIR + storageFileName), StandardCopyOption.REPLACE_EXISTING);
                    fileNames.add(storageFileName);
                    log.info("File {} saved", storageFileName);
                }
            }

        } catch (IOException e) {
            log.error("Error saving images", e);
            throw new ProcessImageException("Під час збереження зображень сталася помилка");
        }
        return fileNames;
    }

    public void deleteImages(Fish fish) {
        Stream.of(fish.getFirstImageFileName(), fish.getSecondImageFileName(), fish.getThirdImageFileName())
                .filter(Objects::nonNull)
                .forEach(name -> {
                    Path imagePath = Paths.get(UPLOAD_DIR + name);
                    try {
                        Files.delete(imagePath);
                        log.info("Deleted image: {}", name);
                    } catch (IOException e) {
                        log.error("Error delete images", e);
                        throw new ProcessImageException("Помилка видалення зображення: " + name);
                    }
                });
    }
}

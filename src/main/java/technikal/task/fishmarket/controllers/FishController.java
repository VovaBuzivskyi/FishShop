package technikal.task.fishmarket.controllers;


import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import technikal.task.fishmarket.models.Fish;
import technikal.task.fishmarket.dtos.FishDto;
import technikal.task.fishmarket.repositories.FishRepository;

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

@Controller
@RequestMapping("/fish")
public class FishController {

    private static final Logger log = LoggerFactory.getLogger(FishController.class);
    private final FishRepository repo;

    public FishController(FishRepository repo) {
        this.repo = repo;
    }

    @GetMapping({"", "/"})
    public String showFishList(Model model) {
        List<Fish> fishlist = repo.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("fishlist", fishlist);

        log.info("{} Fishes was get from fish repository", fishlist.size());
        return "index";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        FishDto fishDto = new FishDto();
        model.addAttribute("fishDto", fishDto);
        return "createFish";
    }

    @GetMapping("/delete")
    public String deleteFish(@RequestParam int id) {

        Fish fish = repo.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Fish not found"));

        List<String> fileNames = Stream.of(
                        fish.getFirstImageFileName(),
                        fish.getSecondImageFileName(),
                        fish.getThirdImageFileName()
                ).filter(Objects::nonNull)
                .toList();

        fileNames.forEach(name -> {
            Path imagePath = Paths.get("public/images/" + name);
            try {
                Files.delete(imagePath);
            } catch (Exception ex) {
                System.out.println("Exception: " + ex.getMessage());
            }
        });

        repo.delete(fish);
        log.info("Fish with id {} was deleted", fish.getId());

        return "redirect:/fish";
    }

    @PostMapping("/create")
    public String addFish(@Valid @ModelAttribute FishDto fishDto, BindingResult result) {

        if (fishDto.getFirstImageFile().isEmpty()) {
            result.addError(new FieldError("fishDto", "firstImageFile", "Потрібне фото рибки"));
        }

        if (result.hasErrors()) {
            return "createFish";
        }

        Fish fish = new Fish();
        Date catchDate = new Date();

        MultipartFile image = fishDto.getFirstImageFile();
        MultipartFile image2 = fishDto.getSecondImageFile();
        MultipartFile image3 = fishDto.getThirdImageFile();

        List<MultipartFile> images = new ArrayList<>(List.of(image, image2, image3));
        List<String> fileNames = new ArrayList<>();

        try {
            String uploadDir = "public/images/";
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            images.stream()
                    .filter(multipartFile -> !multipartFile.isEmpty())
                    .forEach(multipartFile -> {
                        String storageFileName = catchDate.getTime() + "_" + multipartFile.getOriginalFilename();
                        try (InputStream inputStream = multipartFile.getInputStream()) {
                            Files.copy(inputStream, Paths.get(uploadDir + storageFileName), StandardCopyOption.REPLACE_EXISTING);
                            fileNames.add(storageFileName);
                            log.info("File with name {} was added to local storage", storageFileName);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });

        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }

        switch (fileNames.size()) {
            case 1 -> fish.setFirstImageFileName(fileNames.get(0));
            case 2 -> {
                fish.setFirstImageFileName(fileNames.get(0));
                fish.setSecondImageFileName(fileNames.get(1));
            }
            case 3 -> {
                fish.setFirstImageFileName(fileNames.get(0));
                fish.setSecondImageFileName(fileNames.get(1));
                fish.setThirdImageFileName(fileNames.get(2));
            }
        }

        fish.setCatchDate(catchDate);
        fish.setName(fishDto.getName());
        fish.setPrice(fishDto.getPrice());
        repo.save(fish);
        log.info("Fish with id {} was created", fish.getId());

        return "redirect:/fish";
    }
}

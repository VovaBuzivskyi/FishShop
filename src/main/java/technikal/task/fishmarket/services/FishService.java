package technikal.task.fishmarket.services;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import technikal.task.fishmarket.dtos.FishDto;
import technikal.task.fishmarket.models.Fish;
import technikal.task.fishmarket.repositories.FishRepository;

import java.util.Date;
import java.util.List;

@Service
public class FishService {

    private static final Logger log = LoggerFactory.getLogger(FishService.class);

    private final FishRepository repo;
    private final ImageService imageService;

    public FishService(FishRepository repo, ImageService imageService) {
        this.repo = repo;
        this.imageService = imageService;
    }

    public List<Fish> getAllFishes() {
        List<Fish> fishlist = repo.findAll(Sort.by(Sort.Direction.DESC, "id"));
        log.info("{} Fishes retrieved from repository", fishlist.size());
        return fishlist;
    }

    public void saveFish(FishDto fishDto) {
        if (fishDto.getFirstImageFile().isEmpty()) {
            throw new IllegalArgumentException("Потрібне фото рибки");
        }

        Fish fish = new Fish();
        Date catchDate = new Date();

        List<String> fileNames = imageService.saveImages(fishDto, catchDate);

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
    }

    public void deleteFish(int id) {
        Fish fish = repo.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Рибку з ID:%d не знайдено".formatted(id)));

        imageService.deleteImages(fish);
        repo.delete(fish);
        log.info("Fish with id {} was deleted", fish.getId());
    }
}

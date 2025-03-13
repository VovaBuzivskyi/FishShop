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

    private final FishRepository fishRepository;
    private final ImageService imageService;

    public FishService(FishRepository fishRepository, ImageService imageService) {
        this.fishRepository = fishRepository;
        this.imageService = imageService;
    }

    public List<Fish> getAllFishes() {
        List<Fish> fishlist = fishRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        log.info("{} Fishes retrieved from repository", fishlist.size());
        return fishlist;
    }

    public void saveFish(FishDto fishDto) {
        if (fishDto.getImageFiles().isEmpty()) {
            throw new IllegalArgumentException("Потрібне фото рибки");
        }

        Date catchDate = new Date();
        List<String> fileNames = imageService.saveImages(fishDto, catchDate);

        Fish fish = new Fish();
        fish.setImageFileNames(fileNames);
        fish.setCatchDate(catchDate);
        fish.setName(fishDto.getName());
        fish.setPrice(fishDto.getPrice());

        fishRepository.save(fish);
        log.info("Fish with id {} was created", fish.getId());
    }

    public void deleteFish(int id) {
        Fish fish = fishRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Рибку з ID:%d не знайдено".formatted(id)));

        imageService.deleteImages(fish);
        fishRepository.delete(fish);
        log.info("Fish with id {} was deleted", fish.getId());
    }
}

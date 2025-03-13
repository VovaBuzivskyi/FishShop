package technikal.task.fishmarket.services;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;
import technikal.task.fishmarket.dtos.FishDto;
import technikal.task.fishmarket.models.Fish;
import technikal.task.fishmarket.repositories.FishRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FishServiceTest {

    @Mock
    private FishRepository fishRepository;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private FishService fishService;

    @Test
    void getAllFishesTest() {
        Fish firstFish = new Fish();
        Fish secondFish = new Fish();
        List<Fish> fishList = new ArrayList<>(List.of(firstFish, secondFish));

        when(fishRepository.findAll(Sort.by(Sort.Direction.DESC, "id")))
                .thenReturn(fishList);

        List<Fish> result = fishService.getAllFishes();

        assertEquals(fishList, result);
    }

    @Test
    void saveFishTest() {
        FishDto fishDto = new FishDto();
        fishDto.setName("Salmon");
        fishDto.setPrice(100.0);
        fishDto.setImageFiles(new ArrayList<>(List.of(mock(MultipartFile.class))));

        List<String> fileNames = List.of("image1.jpg", "image2.jpg");
        when(imageService.saveImages(any(FishDto.class), any(Date.class))).thenReturn(fileNames);

        fishService.saveFish(fishDto);

        verify(fishRepository).save(any(Fish.class));
    }

    @Test
    void saveFishWithoutImageThrowsExceptionTest() {
        FishDto fishDto = new FishDto();
        fishDto.setName("Salmon");
        fishDto.setPrice(100.0);
        fishDto.setImageFiles(new ArrayList<>());

        assertThrows(IllegalArgumentException.class, () -> fishService.saveFish(fishDto));
    }

    @Test
    void deleteFishTest() {
        int id = 1;
        Fish fish = new Fish();
        fish.setId(id);

        when(fishRepository.findById(id)).thenReturn(Optional.of(fish));

        fishService.deleteFish(id);

        verify(imageService).deleteImages(fish);
        verify(fishRepository).delete(fish);
    }

    @Test
    void deleteFishThrowsExceptionTest() {
        int id = 1;

        when(fishRepository.findById(id)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> fishService.deleteFish(id));
    }
}
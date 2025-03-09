package technikal.task.fishmarket.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import technikal.task.fishmarket.dtos.FishDto;
import technikal.task.fishmarket.exceptions.ProccessImageException;
import technikal.task.fishmarket.models.Fish;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static technikal.task.fishmarket.services.ImageService.UPLOAD_DIR;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

    @InjectMocks
    private ImageService imageService;

    @Mock
    private MultipartFile firstImage;

    @Mock
    private MultipartFile secondImage;

    @Mock
    private MultipartFile thirdImage;

    private FishDto fishDto;
    private final Date catchDate = new Date();
    private String uploadDir;

    @BeforeEach
    void setUp() {
        fishDto = new FishDto();
        fishDto.setFirstImageFile(firstImage);
        fishDto.setSecondImageFile(secondImage);
        fishDto.setThirdImageFile(thirdImage);

        uploadDir = "public/images/";
    }

    @Test
    void saveImagesTest() throws IOException {
        when(firstImage.isEmpty()).thenReturn(false);
        when(secondImage.isEmpty()).thenReturn(true);
        when(thirdImage.isEmpty()).thenReturn(false);

        when(firstImage.getOriginalFilename()).thenReturn("fish1.jpg");
        when(thirdImage.getOriginalFilename()).thenReturn("fish3.png");

        InputStream inputStream1 = mock(InputStream.class);
        InputStream inputStream3 = mock(InputStream.class);
        when(firstImage.getInputStream()).thenReturn(inputStream1);
        when(thirdImage.getInputStream()).thenReturn(inputStream3);

        Files.createDirectories(Paths.get(uploadDir));

        List<String> savedFileNames = imageService.saveImages(fishDto, catchDate);

        assertEquals(2, savedFileNames.size());
        assertTrue(savedFileNames.get(0).endsWith(".jpg"));
        assertTrue(savedFileNames.get(1).endsWith(".png"));

        verify(firstImage, times(1)).getInputStream();
        verify(thirdImage, times(1)).getInputStream();
        verify(secondImage, never()).getInputStream();

        Files.deleteIfExists(Paths.get(uploadDir + catchDate.getTime() + "_" + "fish1.jpg"));
        Files.deleteIfExists(Paths.get(uploadDir + catchDate.getTime() + "_" + "fish3.png"));
    }

    @Test
    void saveImagesThrowsExceptionTest() throws IOException {
        when(firstImage.isEmpty()).thenReturn(false);
        when(firstImage.getOriginalFilename()).thenReturn("fish1.jpg");
        when(firstImage.getInputStream()).thenThrow(IOException.class);

        assertThrows(ProccessImageException.class, () -> imageService.saveImages(fishDto, catchDate));

        verify(firstImage, times(1)).getInputStream();
    }

    @Test
    void deleteImagesTest() throws IOException {
        Path testFile = Paths.get(UPLOAD_DIR, "testImage.jpg");
        Files.createDirectories(testFile.getParent());
        Files.createFile(testFile);

        Fish fish = new Fish();
        fish.setFirstImageFileName("testImage.jpg");
        fish.setSecondImageFileName(null);
        fish.setThirdImageFileName(null);

        imageService.deleteImages(fish);

        assertFalse(Files.exists(Paths.get(UPLOAD_DIR, "testImage.jpg")));
    }

    @Test
    void deleteImagesThrowsExceptionTest() {
        Fish fish = new Fish();
        fish.setFirstImageFileName("fish1.jpg");

        ProccessImageException exception = assertThrows(ProccessImageException.class,
                () -> imageService.deleteImages(fish));

        assertTrue(exception.getMessage().contains("Помилка видалення зображення"));
    }
}
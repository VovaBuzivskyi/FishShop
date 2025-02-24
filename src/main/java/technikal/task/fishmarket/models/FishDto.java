package technikal.task.fishmarket.models;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

public class FishDto {
	

	@NotEmpty(message = "потрібна назва рибки")
	private String name;
	@Min(0)
	private double price;

	private MultipartFile firstImageFile;
	private MultipartFile secondImageFile;
	private MultipartFile thirdImageFile;

	public MultipartFile getThirdImageFile() {
		return thirdImageFile;
	}

	public void setThirdImageFile(MultipartFile thirdImageFile) {
		this.thirdImageFile = thirdImageFile;
	}

	public MultipartFile getSecondImageFile() {
		return secondImageFile;
	}

	public void setSecondImageFile(MultipartFile secondImageFile) {
		this.secondImageFile = secondImageFile;
	}

	public MultipartFile getFirstImageFile() {
		return firstImageFile;
	}

	public void setFirstImageFile(MultipartFile firstImageFile) {
		this.firstImageFile = firstImageFile;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
}

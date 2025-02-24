package technikal.task.fishmarket.models;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "fish")
public class Fish {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String name;
	private double price;
	private Date catchDate;

	private String firstImageFileName;
	private String secondImageFileName;
	private String thirdImageFileName;

	public String getFirstImageFileName() {
		return firstImageFileName;
	}

	public void setFirstImageFileName(String imageFileName) {
		this.firstImageFileName = imageFileName;
	}

	public String getSecondImageFileName() {
		return secondImageFileName;
	}

	public void setSecondImageFileName(String secondImageFileName) {
		this.secondImageFileName = secondImageFileName;
	}

	public String getThirdImageFileName() {
		return thirdImageFileName;
	}

	public void setThirdImageFileName(String thirdImageFileName) {
		this.thirdImageFileName = thirdImageFileName;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public Date getCatchDate() {
		return catchDate;
	}
	public void setCatchDate(Date catchDate) {
		this.catchDate = catchDate;
	}

}

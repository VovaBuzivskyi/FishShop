package technikal.task.fishmarket.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Date;

@Entity
@Table(name = "fish")
public class Fish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "catch_date", nullable = false)
    private Date catchDate;

    @Column(name = "image_file_name", nullable = false)
    private String firstImageFileName;

    @Column(name = "second_image_file_name")
    private String secondImageFileName;

    @Column(name = "third_image_file_name")
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

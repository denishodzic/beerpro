package ch.beerpro.domain.models;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Beer implements Entity, Serializable {

    public static final String COLLECTION = "beers";
    public static final String FIELD_NAME = "name";

    @Exclude
    private String id;
    private String manufacturer;
    private String name;
    private String category;
    private String photo;
    private float avgRating;
    private int numRatings;

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getCategory(){
        return category;
    }

    public String getManufacturer(){
        return manufacturer;
    }

    public String getName() {
        return this.name;
    }

    public String getPhoto() {
        return this.photo;
    }

    public float getAvgRating() {
        return this.avgRating;
    }

    public int getNumRatings() {
        return this.numRatings;
    }
}

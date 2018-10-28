package ch.beerpro.domain.models;

import com.google.firebase.firestore.Exclude;

import java.util.Date;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rating implements Entity {
    public static final String COLLECTION = "ratings";
    public static final String FIELD_BEER_ID = "beerId";
    public static final String FIELD_USER_ID = "userId";
    public static final String FIELD_LIKES = "likes";
    public static final String FIELD_CREATION_DATE = "creationDate";

    @Exclude
    private String id;
    private String beerId;
    private String beerName;
    private String userId;
    private String userName;
    private String userPhoto;
    private String photo;
    private float rating;
    private String comment;

    /**
     * We use a Map instead of an Array to be able to query it.
     *
     * @see <a href="https://firebase.google.com/docs/firestore/solutions/arrays#solution_a_map_of_values"/>
     */
    private Map<String, Boolean> likes;
    private Date creationDate;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getBeerId(){
        return beerId;
    }

    public String getBeerName(){ return beerName;}

    public Date getCreationDate(){
        return creationDate;
    }

    public Map getLikes(){
        return likes;
    }

    public String getUserPhoto(){ return userPhoto;}

    public String getPhoto(){ return photo;}

    public String getUserId() {
        return this.userId;
    }

    public String getUserName() {
        return this.userName;
    }

    public float getRating() {
        return this.rating;
    }

    public String getComment(){
        return this.comment;
    }
}

package ch.beerpro.domain.models;

import java.util.Date;

import lombok.Data;

@Data
public class MyBeerFromFridge implements MyBeer {
    private MyFridge fridge;
    private Beer beer;
    private int amount;
    private Rating rating;

    public MyBeerFromFridge(MyFridge fridge, Beer beer) {
        this.fridge = fridge;
        this.beer = beer;
    }

    @Override
    public String getBeerId() {
        return fridge.getBeerId();
    }

    @Override
    public Beer getBeer() {
        return beer;
    }
/*
    @Override
    public String getBeerId() {
        return rating.getBeerId();
    }
*/
    @Override
    public Date getDate() {
        return rating.getCreationDate();
    }
}

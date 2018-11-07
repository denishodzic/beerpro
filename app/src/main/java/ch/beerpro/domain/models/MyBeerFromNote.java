package ch.beerpro.domain.models;

import java.util.Date;

import lombok.Data;

@Data
public class MyBeerFromNote implements MyBeer {
    private MyNote note;
    private Beer beer;

    public MyBeerFromNote(MyNote note, Beer beer) {
        this.note = note;
        this.beer = beer;
    }

    @Override
    public String getBeerId() {
        return note.getBeerId();
    }

    @Override
    public Beer getBeer() {
        return null;
    }

    @Override
    public Date getDate() { return note.getAddedAt(); }
}

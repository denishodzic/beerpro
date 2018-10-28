package ch.beerpro.presentation.profile.myfridge;

import android.util.Pair;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ch.beerpro.data.repositories.BeersRepository;
import ch.beerpro.data.repositories.CurrentUser;
import ch.beerpro.data.repositories.MyBeersRepository;
import ch.beerpro.data.repositories.MyFridgesRepository;
import ch.beerpro.data.repositories.WishlistRepository;
import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.MyBeerFromFridge;

public class MyFridgeViewModel extends ViewModel implements CurrentUser {

    private final MutableLiveData<String> currentUserId = new MutableLiveData<>();
    private final WishlistRepository wishlistRepository;
    private final BeersRepository beersRepository;
    private final MyFridgesRepository myFridgesRepository;
    public MyFridgeViewModel(MyBeersRepository myBeersRepository, MyFridgesRepository myFridgesRepository) {
        this.myFridgesRepository = myFridgesRepository;
        wishlistRepository = new WishlistRepository();
        beersRepository = new BeersRepository();
        currentUserId.setValue(getCurrentUser().getUid());
    }

    public LiveData<List<Pair<MyBeerFromFridge, Beer>>> getMyFridgeListWithBeer() {
        LiveData<List<Beer>> allBeers = beersRepository.getAllBeers();
        return myFridgesRepository.getMyFridgeWithBeers(currentUserId, allBeers);
    }
}

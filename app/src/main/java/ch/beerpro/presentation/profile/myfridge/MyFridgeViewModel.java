package ch.beerpro.presentation.profile.myfridge;

import android.util.Pair;

import com.google.android.gms.tasks.Task;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ch.beerpro.data.repositories.BeersRepository;
import ch.beerpro.data.repositories.CurrentUser;
import ch.beerpro.data.repositories.FridgeRepository;
import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.MyFridge;

public class MyFridgeViewModel extends ViewModel implements CurrentUser {
    private static final String TAG = "MyFridgeViewModel";

    private final MutableLiveData<String> currentUserId = new MutableLiveData<>();
    private final FridgeRepository fridgeRepository;
    private final BeersRepository beersRepository;

    public MyFridgeViewModel() {
        fridgeRepository = new FridgeRepository();
        beersRepository = new BeersRepository();

        currentUserId.setValue(getCurrentUser().getUid());
    }

    public LiveData<List<Pair<MyFridge, Beer>>> getMyFridgeWithBeers() {
        return fridgeRepository.getMyFridgeWithBeers(currentUserId, beersRepository.getAllBeers());
    }

    public Task<Void> addBeerToFridge(String itemId) {
        return fridgeRepository.addBeerToFridge(getCurrentUser().getUid(), itemId);
    }

    public Task<Void> removeBeerFromFridge(String itemId) {
        return fridgeRepository.removeBeerFromFridge(getCurrentUser().getUid(), itemId);
    }

    public void setAmount(String itemId, String amount) {
        fridgeRepository.setAmount(getCurrentUser().getUid(), itemId, amount);
    }
}

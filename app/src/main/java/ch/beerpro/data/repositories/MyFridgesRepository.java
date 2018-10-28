package ch.beerpro.data.repositories;

import android.util.Pair;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.lifecycle.LiveData;
import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.Entity;
import ch.beerpro.domain.models.MyBeerFromFridge;
import ch.beerpro.domain.utils.FirestoreQueryLiveDataArray;

import static androidx.lifecycle.Transformations.map;
import static androidx.lifecycle.Transformations.switchMap;
import static ch.beerpro.domain.utils.LiveDataExtensions.combineLatest;

public class MyFridgesRepository {

    private static LiveData<List<MyBeerFromFridge>> getMyBeerFromFridgesByUser(String userId) {
        return new FirestoreQueryLiveDataArray<>(FirebaseFirestore.getInstance().collection(MyBeerFromFridge.COLLECTION).whereEqualTo(MyBeerFromFridge.FIELD_USER_ID, userId),
                MyBeerFromFridge.class);
    }

    public LiveData<List<Pair<MyBeerFromFridge, Beer>>> getMyFridgeWithBeers(LiveData<String> currentUserId,
                                                                             LiveData<List<Beer>> allBeers) {
        return map(combineLatest(getMyBeerFromFridges(currentUserId), map(allBeers, Entity::entitiesById)), input -> {
            List<MyBeerFromFridge> MyBeerFromFridges = input.first;
            HashMap<String, Beer> beersById = input.second;

            ArrayList<Pair<MyBeerFromFridge, Beer>> result = new ArrayList<>();
            for (MyBeerFromFridge MyBeerFromFridge : MyBeerFromFridges) {
                Beer beer = beersById.get(MyBeerFromFridge.getId());
                result.add(Pair.create(MyBeerFromFridge, beer));
            }
            return result;
        });
    }

    public LiveData<List<MyBeerFromFridge>> getMyBeerFromFridges(LiveData<String> currentUserId) {
        return switchMap(currentUserId, MyFridgesRepository::getMyBeerFromFridgesByUser);
    }

    public Task<Void> addMyBeerFromFridge(String userId, String itemId) {
        return addMyBeerFromFridge(userId, itemId, 1);
    }

    public Task<Void> addMyBeerFromFridge(String userId, String itemId, Integer amount) {
        return updateAmount(userId, itemId, amount);
    }

    public Task<Void> removeMyBeerFromFridge(String userId, String itemId) {
        return removeMyBeerFromFridge(userId, itemId, 1);
    }

    public Task<Void> removeMyBeerFromFridge(String userId, String itemId, Integer amount) {
        return updateAmount(userId, itemId, -amount);
    }

    private Task<Void> updateAmount(String userId, String itemId, Integer amount) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String MyBeerFromFridgeId = MyBeerFromFridge.generateId(userId, itemId);
        DocumentReference MyBeerFromFridgeQuery = db.collection(MyBeerFromFridge.COLLECTION).document(MyBeerFromFridgeId);

        return MyBeerFromFridgeQuery.get().continueWithTask(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                int currentAmount = (Integer) task.getResult().get(MyBeerFromFridge.FIELD_AMOUNT);
                return MyBeerFromFridgeQuery.update(MyBeerFromFridge.FIELD_AMOUNT, currentAmount + amount);
            } else if (task.isSuccessful()) {
                return MyBeerFromFridgeQuery.set(new MyBeerFromFridge(userId, itemId, amount));
            } else {
                throw task.getException();
            }
        });
    }
}

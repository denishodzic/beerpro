package ch.beerpro.data.repositories;

import android.util.Pair;
import androidx.lifecycle.LiveData;
import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.Entity;
import ch.beerpro.domain.models.MyFridge;
import ch.beerpro.domain.utils.FirestoreQueryLiveData;
import ch.beerpro.domain.utils.FirestoreQueryLiveDataArray;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static androidx.lifecycle.Transformations.map;
import static androidx.lifecycle.Transformations.switchMap;
import static ch.beerpro.domain.utils.LiveDataExtensions.combineLatest;

public class FridgeRepository {
    private static LiveData<List<MyFridge>> getMyFridgeFromUser(String userId) {
        return new FirestoreQueryLiveDataArray<>(FirebaseFirestore.getInstance().collection(MyFridge.COLLECTION)
                .orderBy(MyFridge.FIELD_ADDED_AT, Query.Direction.DESCENDING).whereEqualTo(MyFridge.FIELD_USER_ID, userId),
                MyFridge.class);
    }

    private static LiveData<MyFridge> getUserFridgeFor(Pair<String, Beer> input) {
        String userId = input.first;
        Beer beer = input.second;
        DocumentReference document = FirebaseFirestore.getInstance().collection(MyFridge.COLLECTION)
                .document(MyFridge.generateId(userId, beer.getId()));
        return new FirestoreQueryLiveData<>(document, MyFridge.class);
    }

    public Task<Void> addBeerToFridge(String userId, String itemId) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String fridgeId = MyFridge.generateId(userId, itemId);

        DocumentReference fridgeEntryQuery = db.collection(MyFridge.COLLECTION).document(fridgeId);

        return fridgeEntryQuery.get().continueWithTask(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                return fridgeEntryQuery.update(MyFridge.FIELD_AMOUNT, task.getResult().getLong(MyFridge.FIELD_AMOUNT) + 1);
            } else if (task.isSuccessful()) {
                return fridgeEntryQuery.set(new MyFridge(null, userId, itemId, new Date(), 1));
            } else {
                throw task.getException();
            }
        });
    }

    public LiveData<List<Pair<MyFridge, Beer>>> getMyFridgeWithBeers(LiveData<String> currentUserId,
                                                                     LiveData<List<Beer>> allBeers) {
        return map(combineLatest(getMyFridge(currentUserId), map(allBeers, Entity::entitiesById)), input -> {
            List<MyFridge> fridges = input.first;
            HashMap<String, Beer> beersById = input.second;

            ArrayList<Pair<MyFridge, Beer>> result = new ArrayList<>();
            for (MyFridge fridge : fridges) {
                Beer beer = beersById.get(fridge.getBeerId());
                result.add(Pair.create(fridge, beer));
            }
            return result;
        });
    }

    public LiveData<List<MyFridge>> getMyFridge(LiveData<String> currentUserId) {
        return switchMap(currentUserId, FridgeRepository::getMyFridgeFromUser);
    }

    public LiveData<MyFridge> getMyFridgeForBeer(LiveData<String> currentUserId, LiveData<Beer> beer) {
        return switchMap(combineLatest(currentUserId, beer), FridgeRepository::getUserFridgeFor);
    }

    public void setAmount(String userId, String beerId, String amount) {
        DocumentReference document = FirebaseFirestore.getInstance().collection(MyFridge.COLLECTION)
                .document(MyFridge.generateId(userId, beerId));
        document.update(MyFridge.FIELD_AMOUNT, Integer.parseInt(amount));
    }

    public Task<Void> removeBeerFromFridge(String userId, String itemId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String fridgeId = MyFridge.generateId(userId, itemId);

        DocumentReference fridgeEntryQuery = db.collection(MyFridge.COLLECTION).document(fridgeId);

        return fridgeEntryQuery.get().continueWithTask(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                return fridgeEntryQuery.delete();
            } else {
                throw task.getException();
            }
        });
    }
}

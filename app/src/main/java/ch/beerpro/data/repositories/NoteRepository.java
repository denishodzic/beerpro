package ch.beerpro.data.repositories;

import android.util.Pair;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.MyNote;
import ch.beerpro.domain.utils.FirestoreQueryLiveDataArray;

import static androidx.lifecycle.Transformations.switchMap;
import static ch.beerpro.domain.utils.LiveDataExtensions.combineLatest;

public class NoteRepository {
    public static LiveData<List<MyNote>> getNoteByUserAndBeer(Pair<String, Beer> input) {
        String userId = input.first;
        Beer beer = input.second;

        return new FirestoreQueryLiveDataArray<>(FirebaseFirestore.getInstance().collection(MyNote.COLLECTION)
                .orderBy(MyNote.FIELD_ADDED_AT, Query.Direction.DESCENDING)
                .whereEqualTo(MyNote.FIELD_USER_ID, userId)
                .whereEqualTo(MyNote.FIELD_BEER_ID, beer.getId()), MyNote.class);
    }

    public LiveData<List<MyNote>> getNotesForBeer(LiveData<String> userId, LiveData<Beer> beerId) {
        return switchMap(combineLatest(userId, beerId), NoteRepository::getNoteByUserAndBeer);
    }


    public Task<Void> removeNote(String userId, String itemId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String fridgeId = MyNote.generateId(userId, itemId);

        DocumentReference noteEntryQuery = db.collection(MyNote.COLLECTION).document(fridgeId);

        return noteEntryQuery.get().continueWithTask(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                return noteEntryQuery.delete();
            } else {
                throw task.getException();
            }
        });
    }

    public LiveData<List<MyNote>> getMyNotes(MutableLiveData<String> currentUserId) {
        return switchMap(currentUserId, NoteRepository::getNoteByUser);
    }

    private static LiveData<List<MyNote>> getNoteByUser(String userId) {
        return new FirestoreQueryLiveDataArray<>(FirebaseFirestore.getInstance().collection(MyNote.COLLECTION)
                .orderBy(MyNote.FIELD_ADDED_AT, Query.Direction.DESCENDING).whereEqualTo(MyNote.FIELD_USER_ID, userId),
                MyNote.class);
    }
}
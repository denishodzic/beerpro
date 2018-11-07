package ch.beerpro.domain.utils;

import android.os.Handler;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import javax.annotation.Nullable;

import androidx.lifecycle.LiveData;
import ch.beerpro.domain.models.Entity;
import ch.beerpro.presentation.utils.EntityClassSnapshotParser;

public class FirestoreQueryLiveData<T extends Entity> extends LiveData<T> implements EventListener<DocumentSnapshot> {

    private static final String TAG = "FQueryLiveData";

    private final Handler handler = new Handler();
    private final DocumentReference documentReference;
    private boolean listenerRemovePending = false;
    private ListenerRegistration listenerRegistration;
    private final Runnable removeListener = new Runnable() {
        @Override
        public void run() {
            listenerRegistration.remove();
            listenerRemovePending = false;
        }
    };

    private EntityClassSnapshotParser<T> parser;

    public FirestoreQueryLiveData(DocumentReference documentReference, Class<T> modelClass) {
        this.documentReference = documentReference;
        parser = new EntityClassSnapshotParser<T>(modelClass);
    }

    @Override
    protected void onActive() {
        if (listenerRemovePending) {
            handler.removeCallbacks(removeListener);
        } else if (listenerRegistration == null) {
            listenerRegistration = documentReference.addSnapshotListener(this);
        }
        listenerRemovePending = false;
    }

    @Override
    protected void onInactive() {
        handler.postDelayed(removeListener, 2000);
        listenerRemovePending = true;
    }

    @Override
    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
        if (documentSnapshot != null && documentSnapshot.exists()) {
            setValue(parser.parseSnapshot(documentSnapshot));
        }
    }
}
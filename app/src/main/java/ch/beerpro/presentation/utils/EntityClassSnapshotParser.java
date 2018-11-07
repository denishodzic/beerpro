package ch.beerpro.presentation.utils;

import androidx.annotation.NonNull;
import ch.beerpro.domain.models.Entity;
import com.firebase.ui.firestore.ClassSnapshotParser;
import com.google.firebase.firestore.DocumentSnapshot;

public class EntityClassSnapshotParser<T extends Entity> extends ClassSnapshotParser<T> {
    public EntityClassSnapshotParser(Class<T> modelClass) {
        super(modelClass);
    }

    @NonNull
    @Override
    public T parseSnapshot(@NonNull DocumentSnapshot snapshot) {
        T t = super.parseSnapshot(snapshot);
        t.setId(snapshot.getId());
        return t;
    }
}

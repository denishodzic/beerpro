package ch.beerpro.presentation.details.createrating;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

import androidx.lifecycle.ViewModel;
import ch.beerpro.data.repositories.CurrentUser;
import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.MyNote;

public class CreateNoteViewModel extends ViewModel implements CurrentUser {
    private static final String TAG = "CreateNoteViewModel";

    private Beer item;

    public void setItem(Beer item) {
        this.item = item;
    }
    public Beer getItem() {
        return item;
    }

    public Task<Void> saveNote(Beer item, String note) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String itemId = item.getId();
        String userId = getCurrentUser().getUid();
        String noteId = MyNote.generateId(userId, itemId);

        DocumentReference noteEntryQuery = db.collection(MyNote.COLLECTION).document(noteId);

        return noteEntryQuery.get().continueWithTask(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                return noteEntryQuery.update(MyNote.FIELD_NOTE, note);
            } else if (task.isSuccessful()) {
                return noteEntryQuery.set(new MyNote(userId, itemId, new Date(), note));
            } else {
                throw task.getException();
            }
        });
    }
}

package ch.beerpro.presentation.details;

import ch.beerpro.domain.models.MyNote;

public interface OnNoteListener {
    void onEditClickedListener(MyNote note);

    void onRemoveClickedListener(MyNote note);
}

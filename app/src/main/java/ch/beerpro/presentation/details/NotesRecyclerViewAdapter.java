package ch.beerpro.presentation.details;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.beerpro.R;
import ch.beerpro.domain.models.MyNote;
import ch.beerpro.presentation.utils.EntityDiffItemCallback;


public class NotesRecyclerViewAdapter extends ListAdapter<MyNote, NotesRecyclerViewAdapter.ViewHolder> {

    private static final EntityDiffItemCallback<MyNote> DIFF_CALLBACK = new EntityDiffItemCallback<>();

    private final OnNoteListener listener;

    public NotesRecyclerViewAdapter(OnNoteListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.activity_details_notes_listentry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.bind(getItem(position), listener);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.addedAt)
        TextView addedAt;

        @BindView(R.id.note)
        TextView note;

        @BindView(R.id.removeNote)
        Button remove;

        @BindView(R.id.editNote)
        Button edit;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);
        }

        void bind(MyNote item, OnNoteListener listener) {
            String formattedDate =
                    DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.SHORT).format(item.getAddedAt());
            addedAt.setText(formattedDate);

            remove.setOnClickListener(v -> listener.onRemoveClickedListener(item));
            edit.setOnClickListener(v -> listener.onEditClickedListener(item));
            note.setText(item.getNote());
        }
    }
}

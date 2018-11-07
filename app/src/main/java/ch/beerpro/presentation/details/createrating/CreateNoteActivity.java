package ch.beerpro.presentation.details.createrating;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.beerpro.R;
import ch.beerpro.domain.models.Beer;
import ch.beerpro.presentation.details.DetailsActivity;

public class CreateNoteActivity extends AppCompatActivity {
    public static final String ITEM = "item";
    public static final String NOTE = "note";
    private static final String TAG = "CreateNoteActivity";
    private Beer beer;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.note)
    EditText text;

    private CreateNoteViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.title_activity_note));

        beer = (Beer) getIntent().getExtras().getSerializable(ITEM);
        String note = getIntent().getExtras().getString(NOTE);

        model = ViewModelProviders.of(this).get(CreateNoteViewModel.class);

        model.setItem(beer);
        text.setText(note);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_rating_activity, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                saveNote();
                return true;
            case android.R.id.home:
                if (getParentActivityIntent() == null) {
                    onBackPressed();
                } else {
                    Intent intent = new Intent(this, DetailsActivity.class);
                    intent.putExtra(DetailsActivity.ITEM_ID, beer.getId());
                    NavUtils.navigateUpTo(this, intent);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveNote() {
        String note = text.getText().toString();
        model.saveNote(model.getItem(), note)
                .addOnSuccessListener(task -> onBackPressed())
                .addOnFailureListener(error -> Log.e(TAG, "Could not save note", error));
    }
}

package ch.beerpro.presentation.profile.myfridge;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.beerpro.GlideApp;
import ch.beerpro.R;
import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.MyBeerFromFridge;
import ch.beerpro.presentation.utils.EntityPairDiffItemCallback;

public class MyFridgeRecyclerViewAdapter extends ListAdapter<Pair<MyBeerFromFridge, Beer>, MyFridgeRecyclerViewAdapter.ViewHolder> {
    private static final DiffUtil.ItemCallback<Pair<MyBeerFromFridge, Beer>> DIFF_CALLBACK = new EntityPairDiffItemCallback<>();
    private OnFridgeListInteractionListener listener;

    public MyFridgeRecyclerViewAdapter(OnFridgeListInteractionListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.activity_my_fridge_listentry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pair<MyBeerFromFridge, Beer> item = getItem(position);
        holder.bind(item.first, item.second, listener);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.manufacturer)
        TextView manufacturer;

        @BindView(R.id.category)
        TextView category;

        @BindView(R.id.photo)
        ImageView photo;

        @BindView(R.id.ratingBar)
        RatingBar ratingBar;

        @BindView(R.id.numRatings)
        TextView numRatings;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        void bind(MyBeerFromFridge myBeerFromFridge, Beer beer, OnFridgeListInteractionListener listener) {
            name.setText(beer.getName());
            manufacturer.setText(beer.getManufacturer());
            category.setText(beer.getCategory());
            ratingBar.setNumStars(5);
            ratingBar.setRating(beer.getAvgRating());
            numRatings.setText(itemView.getResources().getString(R.string.fmt_num_ratings, beer.getNumRatings()));
            itemView.setOnClickListener(v -> listener.onMoreClickedListener(photo, beer));
            GlideApp.with(itemView).load(beer.getPhoto()).apply(new RequestOptions().override(240, 240).centerInside())
                    .into(photo);
        }

    }
}

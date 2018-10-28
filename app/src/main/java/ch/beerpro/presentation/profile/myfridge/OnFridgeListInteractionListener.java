package ch.beerpro.presentation.profile.myfridge;

import android.widget.ImageView;

import ch.beerpro.domain.models.Beer;


interface OnFridgeListInteractionListener {
    void onMoreClickedListener(ImageView photo, Beer item);

    void onWishClickedListener(Beer item);
}
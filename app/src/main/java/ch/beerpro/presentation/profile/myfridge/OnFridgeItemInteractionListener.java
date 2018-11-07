package ch.beerpro.presentation.profile.myfridge;

import android.widget.ImageView;
import ch.beerpro.domain.models.Beer;

public interface OnFridgeItemInteractionListener {

    void onMoreClickedListener(ImageView photo, Beer beer);

    void onFridgeClickedListener(Beer beer);

    void onSaveFridgeClickedListener(Beer beer, String amount);

    void onRemoveFridgeClickedListener(Beer beer);
}

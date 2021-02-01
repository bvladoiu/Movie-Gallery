package com.vaha.challenge.ui.movielist;


import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;


/**
 * Class that allows autocomplete entries containing less than 3 letters, by default, not available in Android
 * TODO: move custom widgets like these in a custom_widgets package to be reused in more screens
 */
public class InstantAutoComplete extends androidx.appcompat.widget.AppCompatAutoCompleteTextView {

    public InstantAutoComplete(Context context) {
        super(context);
    }

    public InstantAutoComplete(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
    }

    public InstantAutoComplete(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
    }

    @Override
    public boolean enoughToFilter() {
        return true;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused && getFilter()!=null) {
            performFiltering(getText(), 0);
        }
    }

}

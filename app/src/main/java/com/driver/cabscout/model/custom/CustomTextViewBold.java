package com.driver.cabscout.model.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by pankaj on 20/1/17.
 */

public class CustomTextViewBold extends TextView {

    /*
     * Caches typefaces based on their file path and name, so that they don't have to be created every time when they are referenced.
     */
    private static Typeface mTypeface;

    public CustomTextViewBold(final Context context) {
        this(context, null);
    }

    public CustomTextViewBold(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTextViewBold(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);

        if (mTypeface == null) {
            mTypeface = Typeface.createFromAsset(context.getAssets(), "Mark Simonson - Proxima Nova Semibold_0.ttf");
        }
        setTypeface(mTypeface);
    }

}
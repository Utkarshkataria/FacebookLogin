package com.algofocus.facebooklogin.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class Montserrat_Bold extends android.support.v7.widget.AppCompatTextView {
    public Montserrat_Bold(Context context) {
        super(context);
        initTypeFace(context);
    }

    public Montserrat_Bold(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTypeFace(context);
    }

    public Montserrat_Bold(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTypeFace(context);
    }

    public void initTypeFace(Context context)
    {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"Montserrat_Bold.ttf");
        this.setTypeface(typeface);
    }
}

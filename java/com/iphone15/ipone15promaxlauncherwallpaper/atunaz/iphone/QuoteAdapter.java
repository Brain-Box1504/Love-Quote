package com.iphone15.ipone15promaxlauncherwallpaper.atunaz.iphone;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuoteAdapter extends PagerAdapter {

    Context context;
    List<String> quotes;
    int bgResId;
    String fontName;
    Map<Integer, QuoteStyle> styleMap = new HashMap<>();
    SparseArray<View> views = new SparseArray<>();
    // Default background and fonts (can change if needed)
    int defaultBackground;
    String defaultFont;
    public QuoteAdapter(Context context, List<String> quotes, int bgResId, String fontName) {
        this.context = context;
        this.quotes = quotes;
        this.defaultBackground = defaultBackground;
        this.defaultFont = defaultFont;

        // Initialize all quotes with default style
        for (int i = 0; i < quotes.size(); i++) {
            styleMap.put(i, new QuoteStyle(defaultBackground, defaultFont));
        }
    }

    public void setBackground(int resId) {
        this.bgResId = resId;
    }

    public void setFont(String fontName) {
        this.fontName = fontName;
    }

    // Update style for a specific quote
    public void setStyleForPosition(int position, int background, String font) {
        styleMap.put(position, new QuoteStyle(background, font));
    }

    @Override
    public int getCount() {
        return quotes.size();
    }

    public View getViewAt(int position) {
        return views.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        FrameLayout frame = new FrameLayout(context);

        // 👇 Make sure style is not null
        QuoteStyle style = styleMap.get(position);
        if (style == null) {
            style = new QuoteStyle(defaultBackground, defaultFont);
            styleMap.put(position, style);
        }

        // ✅ Set background
        frame.setBackgroundResource(style.backgroundResId);  // this must be a valid drawable!

        // ✅ Create and style the quote text
        TextView text = new TextView(context);
        text.setText(quotes.get(position));
        text.setTextColor(Color.RED);
        text.setTextSize(28f);
        text.setGravity(Gravity.CENTER);
        text.setShadowLayer(10, 0, 0, Color.rgb(220,40,60));
        text.setPadding(40, 40, 40, 40);

        // ✅ Set fonts
        try {
            Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/" + style.fontName);
            text.setTypeface(tf);
        } catch (Exception e) {
            text.setTypeface(Typeface.DEFAULT_BOLD);
        }

        frame.addView(text, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));

        container.addView(frame);
        return frame;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        views.remove(position);
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }
}

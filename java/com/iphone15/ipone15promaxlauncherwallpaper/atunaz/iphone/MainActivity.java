package com.iphone15.ipone15promaxlauncherwallpaper.atunaz.iphone;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView quoteText;
    private ImageView prevBtn, nextBtn;
    private LinearLayout bgSelector;
    private int currentQuoteIndex = 0;
    private int currentFontIndex = 0;
    private FrameLayout quoteFrame;

    private int[] backgroundImages = {
            R.drawable.bg1, R.drawable.bg2, R.drawable.bg3,
            R.drawable.bg4, R.drawable.bg5, R.drawable.bg6,
            R.drawable.bg7, R.drawable.bg8, R.drawable.bg9, R.drawable.bg10
    };

    private String[] quotes = {
            "The best thing to hold onto in life is each other.",
            "I love you not only for what you are, but for what I am when I am with you.",
            "To love is nothing. To be loved is something. But to love and be loved, that’s everything.",
            "I saw that you were perfect, and so I loved you. Then I saw that you were not perfect and I loved you even more.",
            "In all the world, there is no heart for me like yours. In all the world, there is no love for you like mine.",
            "If I know what love is, it is because of you.",
            "My heart is and always will be yours.",
            "I love you more than words can say.",
            "You are my sun, my moon, and all my stars.",
            "Being deeply loved by someone gives you strength, while loving someone deeply gives you courage.",
            "Love isn't something you find. Love is something that finds you.",
            "We loved with a love that was more than love.",
            "The greatest happiness of life is the conviction that we are loved; loved for ourselves, or rather, loved in spite of ourselves.",
            "All that you are is all that I’ll ever need.",
            "I love you, and that's the beginning and end of everything.",
            "You don't marry someone you can live with – you marry someone you cannot live without.",
            "Love is composed of a single soul inhabiting two bodies.",
            "My love for you has no depth, its boundaries are ever-expanding.",
            "When I saw you I fell in love, and you smiled because you knew.",
            "Your love shines in my heart as the sun that shines upon the earth."
    };

    private String[] fonts = {
            "fonts/alegreya_regular.ttf",
            "fonts/allura_regular.ttf",
            "fonts/playfair_display_italic.ttf",
            "fonts/great_vibes_regular.ttf",
            "fonts/inter_regular.ttf",
            "fonts/lato_regular.ttf",
            "fonts/opensans_regular.ttf",
            "fonts/playfair_display_regular.ttf",
            "fonts/roboto_regular.ttf",
            "fonts/sansation_bold.ttf",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        quoteText = findViewById(R.id.quoteText);
        prevBtn = findViewById(R.id.prevQuote);
        nextBtn = findViewById(R.id.nextQuote);
        bgSelector = findViewById(R.id.bgContainer);
        quoteFrame = findViewById(R.id.quoteFrame);

        Button btnShare = findViewById(R.id.btnShare);

        // Initial Quote
        updateQuote();

        // Apply shadow to text
        quoteText.setShadowLayer(8, 4, 4, getResources().getColor(R.color.black));

        // Background selector
        setupBackgroundSelector();

        // Font change button
        findViewById(R.id.btnFont).setOnClickListener(v -> changeFont());

        // Copy button
        findViewById(R.id.btnCopy).setOnClickListener(v -> {
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setPrimaryClip(ClipData.newPlainText("quote", quotes[currentQuoteIndex]));
            Toast.makeText(this, "Quote copied!", Toast.LENGTH_SHORT).show();
        });

        // Share button
        btnShare.setOnClickListener(v -> {
            Bitmap bitmap = captureQuoteImage();
            Uri uri = saveImageToCache(bitmap);
            if (uri != null) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/*");
                share.putExtra(Intent.EXTRA_STREAM, uri);
                share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(share, "Share Quote"));
            }
        });

        // Download button (simulate saving)
        findViewById(R.id.btnDownload).setOnClickListener(v -> {
            Bitmap bitmap = captureQuoteImage();
            saveImageToGallery(bitmap);
        });

        // Prev/Next buttons
        prevBtn.setOnClickListener(v -> showPreviousQuote());
        nextBtn.setOnClickListener(v -> showNextQuote());

        // Swipe gestures
        quoteText.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeLeft() {
                showNextQuote();
            }
            public void onSwipeRight() {
                showPreviousQuote();
            }
        });
    }

    private void setupBackgroundSelector() {
        for (int bg : backgroundImages) {
            ImageView img = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(200, 200);
            params.setMargins(10, 0, 10, 0);
            img.setLayoutParams(params);
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            img.setImageResource(bg);

            img.setOnClickListener(v -> findViewById(R.id.quoteFrame).setBackgroundResource(bg));

            bgSelector.addView(img);
        }
    }

    private void updateQuote() {
        applyFadeAnimation(quoteText);
        quoteText.setText(quotes[currentQuoteIndex]);
        applyFont();
    }

    private void showNextQuote() {
        currentQuoteIndex = (currentQuoteIndex + 1) % quotes.length;
        updateQuote();
    }

    private void showPreviousQuote() {
        currentQuoteIndex = (currentQuoteIndex - 1 + quotes.length) % quotes.length;
        updateQuote();
    }

    private void applyFadeAnimation(View view) {
        Animation fade = new AlphaAnimation(0, 1);
        fade.setDuration(500);
        view.startAnimation(fade);
    }

    private void changeFont() {
        currentFontIndex = (currentFontIndex + 1) % fonts.length;
        applyFont();
    }

    private void applyFont() {
        try {
            Typeface tf = Typeface.createFromAsset(getAssets(), fonts[currentFontIndex]);
            quoteText.setTypeface(tf);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Font not found!", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap captureQuoteImage() {
        Bitmap bitmap = Bitmap.createBitmap(quoteFrame.getWidth(), quoteFrame.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        quoteFrame.draw(canvas);
        return bitmap;
    }

    private Uri saveImageToCache(Bitmap bitmap) {
        try {
            File cachePath = new File(getCacheDir(), "images");
            cachePath.mkdirs();
            File file = new File(cachePath, "quote.png");
            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();
            return FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveImageToGallery(Bitmap bitmap) {
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "LoveQuotes");
            dir.mkdirs();
            File file = new File(dir, "quote_" + timeStamp + ".png");
            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();
            Toast.makeText(this, "Saved to gallery", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

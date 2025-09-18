package com.iphone15.ipone15promaxlauncherwallpaper.atunaz.iphone;

public class Quote {
    private String text;
    private int backgroundResId;
    private String fontPath;

    public Quote(String text, int backgroundResId, String fontPath) {
        this.text = text;
        this.backgroundResId = backgroundResId;
        this.fontPath = fontPath;
    }

    public String getText() { return text; }
    public int getBackgroundResId() { return backgroundResId; }
    public String getFontPath() { return fontPath; }

    public void setBackgroundResId(int resId) { this.backgroundResId = resId; }
    public void setFontPath(String path) { this.fontPath = path; }
}

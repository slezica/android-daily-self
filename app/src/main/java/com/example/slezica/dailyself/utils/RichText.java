package com.example.slezica.dailyself.utils;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;

public class RichText extends SpannableStringBuilder {

    private static final int FLAGS = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE;

    public interface OnClickListener {
        void onClick();
    }

    public RichText() {
        super();
    }

    public RichText(@NonNull CharSequence text) {
        super(text);
    }

    public RichText setLink(@NonNull OnClickListener listener) {
        setSpan(new LinkSpan(listener));
        return this;
    }

    public RichText setRelativeSize(float factor) {
        setSpan(new RelativeSizeSpan(factor));
        return this;
    }

    public RichText setForegroundColor(int color) {
        setSpan(new ForegroundColorSpan(color));
        return this;
    }

    public RichText setBackgroundColor(int color) {
        setSpan(new BackgroundColorSpan(color));
        return this;
    }

    public RichText setBold() {
        setSpan(new StyleSpan(Typeface.BOLD));
        return this;
    }

    public RichText setItalic() {
        setSpan(new StyleSpan(Typeface.ITALIC));
        return this;
    }

    private void setSpan(Object span) {
        setSpan(span, 0, length(), FLAGS);
    }

    private static class LinkSpan extends ClickableSpan {
        private final OnClickListener clickListener;

        public LinkSpan(@NonNull OnClickListener clickListener) {
            this.clickListener = clickListener;
        }

        @Override
        public void onClick(View textView) {
            clickListener.onClick();
        }

        @Override
        public void updateDrawState(TextPaint textPaint) {
            super.updateDrawState(textPaint);
            textPaint.setUnderlineText(false);
        }
    }
}

package org.bohverkill.pointsundercover.util;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

public class Util {
    public static void setMultilineTextInTextView(TextView tv, String header, String text) {
        Spannable headerText = new SpannableString(header + "\n");
        headerText.setSpan(new ForegroundColorSpan(Color.BLACK), 0, headerText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(headerText);

        Spannable timeText = new SpannableString(text);
        tv.append(timeText);
    }
}

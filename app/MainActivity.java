package com.sari.downloader;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    static {
        try {
            System.loadLibrary("sari-downloader");
        } catch (Throwable ignored) {
            // Prevent startup crash if native library fails.
        }
    }

    private EditText urlInput;
    private Button downloadButton;
    private ProgressBar progressBar;
    private TextView statusText;

    private final Handler mainHandler =
            new Handler(Looper.getMainLooper());

    private native String nativeVersion();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            buildInterface();
        } catch (Throwable error) {
            showSafeError(error);
        }
    }

    private void buildInterface() {

        LinearLayout root = new LinearLayout(this);

        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(32, 40, 32, 32);
        root.setBackgroundColor(Color.WHITE);

        TextView title = new TextView(this);

        title.setText("SARI Downloader");
        title.setTextSize(28);
        title.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );
        title.setTextColor(Color.BLACK);
        title.setGravity(Gravity.CENTER);

        root.addView(
                title,
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                )
        );

        TextView subtitle = new TextView(this);

        subtitle.setText(
                "تنزيل الملفات والفيديوهات المسموح بتنزيلها"
        );

        subtitle.setTextSize(15);
        subtitle.setTextColor(Color.DKGRAY);
        subtitle.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams subtitleParams =
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                );

        subtitleParams.setMargins(0, 12, 0, 30);

        root.addView(subtitle, subtitleParams);

        urlInput = new EditText(this);

        urlInput.setHint("ألصق رابط الفيديو هنا");
        urlInput.setTextSize(16);
        urlInput.setSingleLine(false);
        urlInput.setPadding(24, 18, 24, 18);

        root.addView(
                urlInput,
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                )
        );

        downloadButton = new Button(this);

        downloadButton.setText("تحميل");
        downloadButton.setTextSize(16);

        LinearLayout.LayoutParams buttonParams =
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                );

        buttonParams.setMargins(0, 20, 0, 10);

        root.addView(
                downloadButton,
                buttonParams
        );

        progressBar =
                new ProgressBar(
                        this,
                        null,
                        android.R.attr.progressBarStyleHorizontal
                );

        progressBar.setMax(100);
        progressBar.setProgress(0);

        root.addView(
                progressBar,
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                )
        );

        statusText = new TextView(this);

        statusText.setText(
                "جاهز للتنزيل"
        );

        statusText.setTextSize(15);
        statusText.setTextColor(Color.DKGRAY);

        LinearLayout.LayoutParams statusParams =
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                );

        statusParams.setMargins(0, 20, 0, 0);

        root.addView(
                statusText,
                statusParams
        );

        downloadButton.setOnClickListener(
                v -> startDownload()
        );

        setContentView(root);
    }

    private void startDownload() {

        String url =
                urlInput.getText()
                        .toString()
                        .trim();

        if (url.isEmpty()) {

            Toast.makeText(
                    this,
                    "أدخل الرابط أولاً",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (!url.startsWith("http://") &&
                !url.startsWith("https://")) {

            statusText.setText(
                    "الرابط يجب أن يبدأ بـ http:// أو https://"
            );

            return;
        }

        downloadButton.setEnabled(false);

        progressBar.setProgress(0);

        statusText.setText(
                "جاري تجهيز التنزيل..."
        );

        /*
         * سنضع محرك C++ الحقيقي هنا في المرحلة التالية.
         * لا ننفذ الشبكة على UI thread حتى لا يتجمد التطبيق.
         */

        mainHandler.postDelayed(() -> {

            try {

                String version = nativeVersion();

                statusText.setText(
                        "محرك C++ جاهز — " + version
                );

            } catch (Throwable error) {

                statusText.setText(
                        "حدث خطأ آمن أثناء تشغيل المحرك"
                );
            }

            downloadButton.setEnabled(true);

        }, 500);
    }

    private void showSafeError(Throwable error) {

        try {

            TextView errorView =
                    new TextView(this);

            errorView.setText(
                    "تعذر تشغيل التطبيق.\n" +
                    "يرجى إعادة فتح التطبيق."
            );

            errorView.setTextSize(18);
            errorView.setGravity(Gravity.CENTER);
            errorView.setPadding(30, 30, 30, 30);

            setContentView(errorView);

        } catch (Throwable ignored) {
            finish();
        }
    }
                                  }

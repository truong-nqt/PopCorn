package com.truongnqt.popcorn.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.HapticFeedbackConstants;
import android.view.MenuItem;

import com.truongnqt.popcorn.R;
import com.truongnqt.popcorn.databinding.ActivityAboutBinding;

import java.util.Objects;

public class AboutActivity extends AppCompatActivity {
    private ActivityAboutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        setTitle(R.string.about);

        binding.included.imageViewFeatureGraphicAbout.getLayoutParams().width = getResources().getDisplayMetrics().widthPixels;
        binding.included.imageViewFeatureGraphicAbout.getLayoutParams().height = (int) ((double) getResources().getDisplayMetrics().widthPixels * (500.0 / 1024.0));

        loadActivity();

    }

    private void loadActivity() {

        binding.included.imageButtonShareAbout.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            String packageName = getApplicationContext().getPackageName();
            Intent appShareIntent = new Intent(Intent.ACTION_SEND);
            appShareIntent.setType("text/plain");
            String extraText = "Hey! Check out this amazing app.\n";
            extraText += "https://play.google.com/store/apps/details?id=" + packageName;
            appShareIntent.putExtra(Intent.EXTRA_TEXT, extraText);
            startActivity(appShareIntent);
        });

        binding.included.imageButtonRateUsAbout.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            String packageName = getApplicationContext().getPackageName();
            String playStoreLink = "https://play.google.com/store/apps/details?id=" + packageName;
            Intent appRateUsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(playStoreLink));
            startActivity(appRateUsIntent);
        });

        binding.included.imageButtonFeedbackAbout.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            Intent feedbackIntent = new Intent(Intent.ACTION_SENDTO);
            feedbackIntent.setData(Uri.parse("mailto:"));
            feedbackIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"truongnqt.dev@gmail.com"});
            feedbackIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback: " + getResources().getString(R.string.app_name));
            startActivity(feedbackIntent);
        });

        binding.included.cardViewSourceCodeOnGithub.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            String githubLink = "https://github.com/truongnqt-d/" + getResources().getString(R.string.app_name);
            Intent githubIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(githubLink));
            startActivity(githubIntent);
        });

        binding.included.frameLayoutOpenSourceLicenses.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            String attributionsLink = "https://github.com/hitanshu-dhawan/" + getResources().getString(R.string.app_name) + "/blob/master/ATTRIBUTIONS.md";
            Intent attributionsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(attributionsLink));
            startActivity(attributionsIntent);
        });

        try {
            binding.included.textViewVersionNumber.setText((getPackageManager().getPackageInfo(getPackageName(), 0)).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}

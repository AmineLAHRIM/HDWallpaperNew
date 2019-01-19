package com.vpapps.hdwallpaper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import com.google.ads.consent.*;
import com.vpapps.utils.Constant;

import java.net.MalformedURLException;
import java.net.URL;

public class Gdpr extends AppCompatActivity {

    Button gotosplash;
    private ConsentForm form;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gdpr);

        Gdpr();
    }

    private void Gdpr() {
        final Intent i = new Intent(Gdpr.this, MainActivity.class);
        ConsentInformation consentInformation = ConsentInformation.getInstance(Gdpr.this);
        String[] publisherIds = {Constant.ad_publisher_id+""};
        consentInformation.requestConsentInfoUpdate(publisherIds, new ConsentInfoUpdateListener() {
            @Override
            public void onConsentInfoUpdated(ConsentStatus consentStatus) {
                // User's consent status successfully updated.
            }

            @Override
            public void onFailedToUpdateConsentInfo(String errorDescription) {
                // User's consent status failed to update.

                //startActivity(i);
                Gdpr.this.finish();
            }
        });

        URL privacyUrl = null;
        try {
            // TODO: Replace with your app's privacy policy URL.

            privacyUrl = new URL(Constant.url_privacy_policy);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            // Handle error.
        }
        form = new ConsentForm.Builder(Gdpr.this, privacyUrl)
                .withListener(new ConsentFormListener() {

                    @Override
                    public void onConsentFormLoaded() {
                        // Consent form loaded successfully.

                        form.show();
                    }

                    @Override
                    public void onConsentFormOpened() {
                        // Consent form was displayed.
                    }

                    @Override
                    public void onConsentFormClosed(
                            ConsentStatus consentStatus, Boolean userPrefersAdFree) {
                        // Consent form was closed.
                        startActivity(i);
                        Gdpr.this.finish();
                    }

                    @Override
                    public void onConsentFormError(String errorDescription) {
                        // Consent form error.
                        startActivity(i);
                        Gdpr.this.finish();

                    }
                })
                .withPersonalizedAdsOption()
                .withNonPersonalizedAdsOption()
                .withAdFreeOption()
                .build();

        form.load();
    }

}

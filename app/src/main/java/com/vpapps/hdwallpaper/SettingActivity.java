package com.vpapps.hdwallpaper;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.onesignal.OneSignal;
import com.vpapps.utils.AdConsent;
import com.vpapps.utils.AdConsentListener;
import com.vpapps.utils.Methods;
import com.vpapps.utils.SharedPref;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SettingActivity extends AppCompatActivity {

    Toolbar toolbar;
    Methods methods;
    SharedPref sharedPref;
    AdConsent adConsent;
    LinearLayout ll_consent;
    Switch switch_consent, switch_noti;
    Boolean isNoti = true;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        sharedPref = new SharedPref(this);
        methods = new Methods(this);
        methods.setStatusColor(getWindow());
        methods.forceRTLIfSupported(getWindow());

        isNoti = sharedPref.getIsNotification();

        toolbar = this.findViewById(R.id.toolbar_setting);
        toolbar.setTitle(getString(R.string.action_settings));
        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adConsent = new AdConsent(this, new AdConsentListener() {
            @Override
            public void onConsentUpdate() {
                setConsentSwitch();
            }
        });

        ll_consent = findViewById(R.id.ll_consent);
        switch_noti = findViewById(R.id.switch_noti);
        switch_consent = findViewById(R.id.switch_consent);

        if (adConsent.isUserFromEEA()) {
            setConsentSwitch();
        } else {
            ll_consent.setVisibility(View.GONE);
        }

        if (isNoti) {
            switch_noti.setChecked(true);
        } else {
            switch_noti.setChecked(false);
        }

        switch_noti.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e("aaa-noti", "" + isChecked);
                OneSignal.setSubscription(isChecked);
                sharedPref.setIsNotification(isChecked);
            }
        });

        switch_consent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e("aaa-consent", "" + isChecked);
                if (isChecked) {
                    ConsentInformation.getInstance(SettingActivity.this).setConsentStatus(ConsentStatus.PERSONALIZED);
                } else {
                    ConsentInformation.getInstance(SettingActivity.this).setConsentStatus(ConsentStatus.NON_PERSONALIZED);
                }
            }
        });

        ll_consent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adConsent.requestConsent();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }

    private void setConsentSwitch() {
        if (ConsentInformation.getInstance(this).getConsentStatus() == ConsentStatus.PERSONALIZED) {
            switch_consent.setChecked(true);
        } else {
            switch_consent.setChecked(false);
        }
    }
}
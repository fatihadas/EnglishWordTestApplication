package com.projects.ingilizcekelimecalis;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class Sonuclar extends AppCompatActivity {
    TextView txtdogru, txtyanlis, basari;
    PercentView cizim;
    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sonuclar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.sonuclarbaslik));

        Bundle extras = getIntent().getExtras();
        float dogru = Float.valueOf(extras.getString("dogru"));
        float yanlis = Float.valueOf(extras.getString("yanlis"));
        float toplam = dogru + yanlis;
        float totalbasari = (100 * dogru / toplam);

        txtdogru = (TextView) findViewById(R.id.dogrusayisi);
        txtyanlis = (TextView) findViewById(R.id.yanlissayisi);
        basari = (TextView) findViewById(R.id.basari);

        txtdogru.setText(txtdogru.getText() + String.valueOf((int) dogru));
        txtyanlis.setText(txtyanlis.getText() + String.valueOf((int) yanlis));
        basari.setText(basari.getText() + String.valueOf(String.format("%.2f", totalbasari)));

        cizim = (PercentView) findViewById(R.id.percentview);
        cizim.setPercentage(totalbasari);

        reklamyuklegecis();
    }

    public void reklamyuklegecis() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.kelimetestbitirgecis));

        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                //reklamyuklegecis();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                mInterstitialAd.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

package com.projects.ingilizcekelimecalis;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class MainActivity extends AppCompatActivity {
    Button kelimecalis, kelimetesti, ayarlar, kelimelerim;
    CoordinatorLayout coordinatorLayout;

    InterstitialAd mInterstitialAd;
    AdView adView, adView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        kelimecalis = (Button) findViewById(R.id.kelimecalis);
        kelimetesti = (Button) findViewById(R.id.kelimetesti);
        ayarlar = (Button) findViewById(R.id.ayarlar);
        kelimelerim = (Button) findViewById(R.id.kelimelerim);

        kelimecalis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isNetworkAvailable(MainActivity.this)) {
                    startActivity(new Intent(MainActivity.this, KelimeCalis.class));
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                } else {
                    Snackbar.make(coordinatorLayout, getString(R.string.internetyok), Snackbar.LENGTH_LONG).show();
                }
            }
        });

        kelimetesti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isNetworkAvailable(MainActivity.this)) {
                    startActivity(new Intent(MainActivity.this, KelimeTesti.class));
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                } else {
                    Snackbar.make(coordinatorLayout, getString(R.string.internetyok), Snackbar.LENGTH_LONG).show();
                }
            }
        });

        kelimelerim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Kelimelerim.class));
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        ayarlar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Ayarlar.class));
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        if (Utils.isNetworkAvailable(MainActivity.this)) {
            Snackbar.make(coordinatorLayout, getString(R.string.hosgeldin), Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(coordinatorLayout, getString(R.string.internetyok), Snackbar.LENGTH_LONG).show();
        }

        reklamyuklealt();
        reklamyukleust();
        reklamyuklegecis();

        Utils.isFirstTime(getApplicationContext());
    }

    public void reklamyuklealt() {
        adView2 = (AdView) this.findViewById(R.id.anasayfaalt);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView2.loadAd(adRequest);
    }

    public void reklamyukleust() {
        adView = (AdView) this.findViewById(R.id.anasayfaust);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    public void reklamyuklegecis() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.anasayfagecis));

        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
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
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle(getString(R.string.app_name));
        dialog.setMessage(getString(R.string.backbutonmesaj));
        dialog.setPositiveButton(getString(R.string.btnevet), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reklamyuklegecis();
                finish();
            }
        });
        dialog.setNegativeButton(getString(R.string.btnkapat), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
            }
        });
        dialog.show();
        //super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.mainmenu, menu);
        return true;
    }

    void uygulamayıpaylas() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, getString(R.string.urluygulama));
        startActivity(Intent.createChooser(share, getString(R.string.uygpaylas)));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.uygulamapaylaş:
                uygulamayıpaylas();
                return true;
            case R.id.storedaaç:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.urluygulama))));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.urluygulama))));
                }
                return true;
            case R.id.hakkinda:
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle(getString(R.string.hakkindabaslik));
                dialog.setMessage(getString(R.string.hakkindaicerik));
                dialog.setPositiveButton(getString(R.string.btnkapat), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog.show();
                return true;
            case R.id.diğerapps:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.appsparametre))));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.appsurl))));
                }
                return true;
            case R.id.katkıdabulun:
                AlertDialog.Builder dialog2 = new AlertDialog.Builder(MainActivity.this);
                dialog2.setTitle(getString(R.string.app_name));
                dialog2.setMessage(getString(R.string.backbutonmesaj));
                dialog2.setPositiveButton(getString(R.string.btnevet), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reklamyuklegecis();
                    }
                });
                dialog2.setNegativeButton(getString(R.string.btnkapat), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //dialog2.cancel();
                    }
                });
                dialog2.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

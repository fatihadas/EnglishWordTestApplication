package com.projects.ingilizcekelimecalis;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class KelimeCalis extends AppCompatActivity {
    Button btningilizce, btnturkce, kelimelerimeekle, kelimegetir;
    ImageView btnkopyala, btnpaylas, btndinleing;
    CoordinatorLayout coordinatorLayout;
    TextToSpeech textToSpeech;

    AdView adView, adView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelime_calis);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.app_name));

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        btningilizce = (Button) findViewById(R.id.btningilizce);
        btnturkce = (Button) findViewById(R.id.btnturkce);
        kelimelerimeekle = (Button) findViewById(R.id.kelimelerimeekle);
        kelimegetir = (Button) findViewById(R.id.kelimegetir);

        btnkopyala = (ImageView) findViewById(R.id.btnkopyala);
        btnpaylas = (ImageView) findViewById(R.id.btnpaylas);
        btndinleing = (ImageView) findViewById(R.id.btndinleing);


        kelimelerimeekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kelimelereEkle(btningilizce.getText().toString(), btnturkce.getText().toString());
            }
        });

        kelimegetir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executeGetMethod();
            }
        });

        btnkopyala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kopyalabuton(btningilizce.getText().toString(), btnturkce.getText().toString());
            }
        });

        btnpaylas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paylasbuton(btningilizce.getText().toString(), btnturkce.getText().toString());
            }
        });

        btndinleing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textToSpeech.speak(btningilizce.getText().toString().trim(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });


        if (!Utils.isNetworkAvailable(KelimeCalis.this)) {
            Snackbar.make(coordinatorLayout, getString(R.string.internetyok), Snackbar.LENGTH_LONG).show();
        }

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.US);
                }
            }
        });
        ayargetir();

        reklamyuklealt();
        reklamyukleust();

        executeGetMethod();

    }

    boolean otomatikdinle = false;
    boolean otomatikkaydet = false;

    void ayargetir() {
        otomatikdinle = Utils.getSharedAyarla(getApplicationContext(), "kcotomatikdinle");
        otomatikkaydet = Utils.getSharedAyarla(getApplicationContext(), "kcotomatikekle");
        if (otomatikkaydet) {
            kelimelerimeekle.setEnabled(false);
            kelimelerimeekle.setText(getString(R.string.otoekle));
        }
    }

    void kelimelereEkle(String eng, String tur) {
        Database database = new Database(getApplicationContext());
        if (database.kelimeVarMi(eng)) {
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.eklenmedi), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        } else {
            database.kelimeEkle(eng, tur);
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.eklendi), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            if (!otomatikkaydet)
                toast.show();
        }
    }

    void kopyalabuton(String ing, String tur) {
        ClipboardManager myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData myClip = ClipData.newPlainText("text", ing + " = " + tur);
        myClipboard.setPrimaryClip(myClip);
        Toast.makeText(getApplicationContext(), getString(R.string.kopyala), Toast.LENGTH_SHORT).show();
    }

    void paylasbuton(String ing, String tur) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, ing + " = " + tur);
        startActivity(Intent.createChooser(share, getString(R.string.ceviripaylas)));
    }

    private ProgressDialog progressDialog;

    private void showdialog() {
        progressDialog = new ProgressDialog(KelimeCalis.this);
        progressDialog.setMessage(getString(R.string.kelimeloading));
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.show();
    }

    private void executeGetMethod() {
        showdialog();
        StringRequest jsonForGetRequest = new StringRequest(
                Request.Method.POST, Utils.URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONArray array = new JSONArray(response);
                            JSONObject obj = array.getJSONObject(0);
                            btningilizce.setText(obj.getString("eng"));
                            btnturkce.setText(obj.getString("tur"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (otomatikdinle) {
                            btndinleing.performClick();
                        }

                        if (otomatikkaydet) {
                            kelimelerimeekle.performClick();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {
                    JSONObject jsonObject = null;
                    String errorMessage = null;

                    switch (response.statusCode) {
                        case 400:
                            errorMessage = new String(response.data);

                            try {
                                jsonObject = new JSONObject(errorMessage);
                                String serverResponseMessage = (String) jsonObject.get("hata");
                                Toast.makeText(getApplicationContext(), getString(R.string.bilinmeyenhata) + serverResponseMessage, Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                    }
                }
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("requestid", Utils.requestid);
                params.put("limit", "1");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                return param;
            }
        };

        jsonForGetRequest.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonForGetRequest);

    }

    public void reklamyuklealt() {
        adView2 = (AdView) this.findViewById(R.id.kelimecalisalt);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView2.loadAd(adRequest);
    }

    public void reklamyukleust() {
        adView = (AdView) this.findViewById(R.id.kelimecalisust);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    @Override
    public void onBackPressed() {
        //finish();
        super.onBackPressed();
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

package com.projects.ingilizcekelimecalis;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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

public class KelimeTesti extends AppCompatActivity {
    Button btnkelime, btnkelimesor, btnbitir, btnsorusayisi;
    TextView cevap1, cevap2, cevap3, cevap4, cevap5;
    TextToSpeech textToSpeech;
    ImageView btndinle;
    AdView adView, adView2;
    MediaPlayer dses, yses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelime_testi);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.kelimetest));

        btnkelime = (Button) findViewById(R.id.btnkelime);
        btnkelimesor = (Button) findViewById(R.id.btnkelimesor);
        btnbitir = (Button) findViewById(R.id.btnbitir);
        btnsorusayisi = (Button) findViewById(R.id.btnsorusayisi);
        btndinle = (ImageView) findViewById(R.id.btndinle);
        cevap1 = (TextView) findViewById(R.id.cevap1);
        cevap2 = (TextView) findViewById(R.id.cevap2);
        cevap3 = (TextView) findViewById(R.id.cevap3);
        cevap4 = (TextView) findViewById(R.id.cevap4);
        cevap5 = (TextView) findViewById(R.id.cevap5);

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.US);
                }
            }
        });

        btndinle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textToSpeech.speak(btnkelime.getText().toString().trim(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        btnbitir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sınavbitir();
            }
        });
        btnkelimesor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (secildimi) {
                    onClear();
                    executeGetMethod();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.secimyapmadiniz), Toast.LENGTH_SHORT).show();
                }
            }
        });
        cevap1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPressed(cevap1.getText().toString(), cevap1);
            }
        });
        cevap2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPressed(cevap2.getText().toString(), cevap2);
            }
        });
        cevap3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPressed(cevap3.getText().toString(), cevap3);
            }
        });
        cevap4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPressed(cevap4.getText().toString(), cevap4);
            }
        });
        cevap5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPressed(cevap5.getText().toString(), cevap5);
            }
        });
        ayargetir();

        reklamyuklealt();
        reklamyukleust();

        executeGetMethod();

    }

    boolean otomatikdinle = false;
    boolean otomatikkaydet = false;//dogru
    boolean otomatikses = false;
    boolean bilmedigiotokaydet = false;
    boolean otomatikgecis = false;
    long ktwaittime = 2000;


    void ayargetir() {
        otomatikdinle = Utils.getSharedAyarla(getApplicationContext(), "ktotomatikdinle");
        otomatikkaydet = Utils.getSharedAyarla(getApplicationContext(), "ktotomatikkaydet");
        bilmedigiotokaydet = Utils.getSharedAyarla(getApplicationContext(), "ktbilmedigiotokaydet");
        otomatikgecis = Utils.getSharedAyarla(getApplicationContext(), "ktotomatikgecis");
        otomatikses = Utils.getSharedAyarla(getApplicationContext(), "ktotomatikses");
        ktwaittime = Utils.getwaitTime(getApplicationContext(), "ktwaittime");

        dses = MediaPlayer.create(getApplicationContext(), R.raw.dogru);
        yses = MediaPlayer.create(getApplicationContext(), R.raw.yanlis);
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
            if (!otomatikkaydet && !bilmedigiotokaydet)
                toast.show();

        }
    }

    void sınavbitir() {
        try {
            if (secildimi) {
                thread.interrupt();
                //thread.stop();//System.exit(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (secildimi) {
            bitirildi = true;
            finish();//System.exit(0);
            Intent sonuclar = new Intent(getApplicationContext(), Sonuclar.class);
            sonuclar.putExtra("dogru", String.valueOf(dogrusorusayisi));
            sonuclar.putExtra("yanlis", String.valueOf(yanlissorusayisi));
            startActivity(sonuclar);
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.secimyapbitir), Toast.LENGTH_SHORT).show();
        }
    }

    static Boolean secildimi = false;
    int sorusayisi = 0;
    int dogrusorusayisi = 0;
    int yanlissorusayisi = 0;
    private ProgressDialog progressDialog;

    private void showdialog() {
        progressDialog = new ProgressDialog(KelimeTesti.this);
        progressDialog.setMessage(getString(R.string.soruloading));
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
                        secildimi = false;
                        try {
                            JSONArray array = new JSONArray(response);
                            JSONObject obj = array.getJSONObject(0);
                            cevap1.setText(obj.getString("tur"));
                            obj = array.getJSONObject(1);
                            cevap2.setText(obj.getString("tur"));
                            obj = array.getJSONObject(2);
                            cevap3.setText(obj.getString("tur"));
                            obj = array.getJSONObject(3);
                            cevap4.setText(obj.getString("tur"));
                            obj = array.getJSONObject(4);
                            cevap5.setText(obj.getString("tur"));

                            obj = array.getJSONObject((int) (Math.random() * 5));
                            btnkelime.setText(obj.getString("eng"));
                            dogrucevap = obj.getString("tur");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        sorusayisi++;
                        btnsorusayisi.setText(String.valueOf(sorusayisi));

                        if (otomatikdinle) {
                            btndinle.performClick();
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
                params.put("limit", "5");
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

    public void onClear() {
        setEnable(true);
        cevap1.setBackgroundResource(R.color.bg_login);//cevap1.setBackgroundColor(getColor(R.color.bg_login));
        cevap2.setBackgroundResource(R.color.bg_login);//cevap2.setBackgroundColor(getColor(R.color.bg_login));
        cevap3.setBackgroundResource(R.color.bg_login);//cevap3.setBackgroundColor(getColor(R.color.bg_login));
        cevap4.setBackgroundResource(R.color.bg_login);//cevap4.setBackgroundColor(getColor(R.color.bg_login));
        cevap5.setBackgroundResource(R.color.bg_login);//cevap5.setBackgroundColor(getColor(R.color.bg_login));
    }

    public void onPressed(String secilencevap, TextView tiklanan) {
        if (secilencevap.equals(dogrucevap)) {
            tiklanan.setBackgroundColor(Color.GREEN);
            if (otomatikses)
                dses.start();
            dogrusorusayisi++;
            if (otomatikkaydet)
                kelimelereEkle(btnkelime.getText().toString(), dogrucevap);
        } else {
            tiklanan.setBackgroundColor(Color.RED);
            if (otomatikses)
                yses.start();
            yanlissorusayisi++;
            dogruyusec(dogrucevap);
            if (bilmedigiotokaydet)
                kelimelereEkle(btnkelime.getText().toString(), dogrucevap);
        }
        setEnable(false);
        secildimi = true;

        if (otomatikgecis) {
            thread = new Thread() {
                @Override
                public void run() {
                    try {
                        synchronized (this) {
                            wait(ktwaittime);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!bitirildi)
                                    btnkelimesor.performClick();
                            }
                        });
                    }
                }
            };
            thread.start();
        }
    }

    Thread thread;
    boolean bitirildi = false;

    public void setEnable(Boolean deger) {
        cevap1.setEnabled(deger);
        cevap2.setEnabled(deger);
        cevap3.setEnabled(deger);
        cevap4.setEnabled(deger);
        cevap5.setEnabled(deger);
    }

    public void dogruyusec(String dogru) {
        if (cevap1.getText().toString().equals(dogru))
            cevap1.setBackgroundColor(Color.GREEN);
        else if (cevap2.getText().toString().equals(dogru))
            cevap2.setBackgroundColor(Color.GREEN);
        else if (cevap3.getText().toString().equals(dogru))
            cevap3.setBackgroundColor(Color.GREEN);
        else if (cevap4.getText().toString().equals(dogru))
            cevap4.setBackgroundColor(Color.GREEN);
        else if (cevap5.getText().toString().equals(dogru))
            cevap5.setBackgroundColor(Color.GREEN);
    }

    static String dogrucevap = "";

    public void reklamyuklealt() {
        adView2 = (AdView) this.findViewById(R.id.kelimetestialt);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView2.loadAd(adRequest);
    }

    public void reklamyukleust() {
        adView = (AdView) this.findViewById(R.id.kelimetestiust);
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

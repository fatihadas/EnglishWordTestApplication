package com.projects.ingilizcekelimecalis;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class Kelimelerim extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CustomAdapter customAdapter;
    private ArrayList<Kelime> kelimeList;
    Database database;
    TextToSpeech textToSpeech;
    int longselectdbid = -1;

    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelimelerim);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.kelimelerim));

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        database = new Database(getApplicationContext());
        kelimeList = database.kelimelerList();
        customAdapter = new CustomAdapter(getApplicationContext(), kelimeList,
                new CustomItemClickListener() {
                    @Override
                    public void onItemClick(Kelime kelime, int position) {
                        textToSpeech.speak(kelime.getEnglish(), TextToSpeech.QUEUE_FLUSH, null);
                        //Toast.makeText(getApplicationContext(), kelime.getId() + " - " + kelime.getTurkish() + " - " + kelime.getEnglish(), Toast.LENGTH_LONG).show();
                        //Toast.makeText(getApplicationContext(), "db id:" + kelime.getId() + " - position:" + position, Toast.LENGTH_SHORT).show();
                    }
                },
                new CustomItemLongClickListener() {
                    @Override
                    public void onItemLongClick(Kelime kelime, int position) {
                        //Toast.makeText(getApplicationContext(), kelime.getId() + " - lololo", Toast.LENGTH_LONG).show();
                        longselectdbid = Integer.parseInt(kelime.getId());
                    }
                });
        recyclerView.setAdapter(customAdapter);
        registerForContextMenu(recyclerView);

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.US);
                }
            }
        });

        reklamyukleust();
    }

    public void reklamyukleust() {
        adView = (AdView) this.findViewById(R.id.kelimelerimust);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            public void onAdLoaded() {
                adView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        HashMap<String, String> kelimem = database.kelimeDetay(longselectdbid);

        switch (item.getTitle().toString()) {
            case "Sil":
                database.kelimeSil(longselectdbid);
                longselectdbid = -1;
                kelimeList = database.kelimelerList();
                customAdapter.setKelimeList(kelimeList);
                break;
            case "Kopyala":
                kopyalabuton(kelimem.get("ingilizce"), kelimem.get("turkce"));
                break;
            case "Paylaş":
                paylasbuton(kelimem.get("ingilizce"), kelimem.get("turkce"));
                break;
            default:
                break;
        }//return super.onContextItemSelected(item);
        return true;
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

    @Override
    public void onBackPressed() {
        //finish();
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.resettablo:
                AlertDialog.Builder dialog = new AlertDialog.Builder(Kelimelerim.this);
                dialog.setTitle(getString(R.string.tumunusil));
                dialog.setMessage(getString(R.string.tumunusilicerik));
                dialog.setPositiveButton(getString(R.string.btnevet), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        database.resetTable();
                        Toast.makeText(getApplicationContext(), getString(R.string.tumusilindi), Toast.LENGTH_SHORT).show();
                        recyclerView.setAdapter(null);
                        /////customAdapter.notifyDataSetChanged();
                    }
                });
                dialog.setNegativeButton(getString(R.string.btnhayir), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog.show();
                return true;
            case R.id.sayfahakkinda:
                AlertDialog.Builder dialog2 = new AlertDialog.Builder(Kelimelerim.this);
                dialog2.setTitle(getString(R.string.sayfahakkinda));
                dialog2.setMessage(getString(R.string.sayfahakkindaicerik));
                dialog2.setNegativeButton(getString(R.string.btnkapat), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog2.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.kelimelermenu, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_search);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                customAdapter.getFilter().filter(newText);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}

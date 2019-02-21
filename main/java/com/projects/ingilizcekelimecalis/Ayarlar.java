package com.projects.ingilizcekelimecalis;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class Ayarlar extends AppCompatActivity {
    Switch kcotomatikekle, kcotomatikdinle, ktotomatikdinle, ktotomatikkaydet, ktbilmedigiotokaydet, ktotomatikgecis, ktotomatikses;
    SeekBar ktotomatikgecissure;
    TextView suretext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayarlar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.ayarlar));

        kcotomatikekle = (Switch) findViewById(R.id.kcotomatikekle);
        kcotomatikdinle = (Switch) findViewById(R.id.kcotomatikdinle);
        ktotomatikdinle = (Switch) findViewById(R.id.ktotomatikdinle);
        ktotomatikkaydet = (Switch) findViewById(R.id.ktotomatikkaydet);
        ktbilmedigiotokaydet = (Switch) findViewById(R.id.ktbilmedigiotokaydet);
        ktotomatikgecis = (Switch) findViewById(R.id.ktotomatikgecis);
        ktotomatikses = (Switch) findViewById(R.id.ktotomatikses);
        ktotomatikgecissure = (SeekBar) findViewById(R.id.ktotomatikgecissure);
        suretext = (TextView) findViewById(R.id.suretext);

        setswitchvalue();

    }

    public void setswitchvalue() {

        //kcotomatikekle.setChecked(getkcotomatikekle());
        kcotomatikekle.setChecked(Utils.getSharedAyarla(getApplicationContext(), "kcotomatikekle"));
        kcotomatikekle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //setkcotomatikekle(isChecked);
                Utils.setSharedAyarla(getApplicationContext(), "kcotomatikekle", isChecked);
            }
        });

        //kcotomatikdinle.setChecked(getkcotomatikdinle());
        kcotomatikdinle.setChecked(Utils.getSharedAyarla(getApplicationContext(), "kcotomatikdinle"));
        kcotomatikdinle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //setkcotomatikdinle(isChecked);
                Utils.setSharedAyarla(getApplicationContext(), "kcotomatikdinle", isChecked);
            }
        });

        //ktotomatikdinle.setChecked(getktotomatikdinle());
        ktotomatikdinle.setChecked(Utils.getSharedAyarla(getApplicationContext(), "ktotomatikdinle"));
        ktotomatikdinle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //setktotomatikdinle(isChecked);
                Utils.setSharedAyarla(getApplicationContext(), "ktotomatikdinle", isChecked);
            }
        });

        //ktotomatikkaydet.setChecked(getktotomatikkaydet());
        ktotomatikkaydet.setChecked(Utils.getSharedAyarla(getApplicationContext(), "ktotomatikkaydet"));
        ktotomatikkaydet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //setktotomatikkaydet(isChecked);
                Utils.setSharedAyarla(getApplicationContext(), "ktotomatikkaydet", isChecked);
            }
        });

        //ktbilmedigiotokaydet.setChecked(getktbilmedigiotokaydet());
        ktbilmedigiotokaydet.setChecked(Utils.getSharedAyarla(getApplicationContext(), "ktbilmedigiotokaydet"));
        ktbilmedigiotokaydet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //setktbilmedigiotokaydet(isChecked);
                Utils.setSharedAyarla(getApplicationContext(), "ktbilmedigiotokaydet", isChecked);
            }
        });

        //ktotomatikgecis.setChecked(getktotomatikgecis());
        ktotomatikgecis.setChecked(Utils.getSharedAyarla(getApplicationContext(), "ktotomatikgecis"));
        ktotomatikgecis.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //setktotomatikgecis(isChecked);
                Utils.setSharedAyarla(getApplicationContext(), "ktotomatikgecis", isChecked);
            }
        });

        //ktotomatikses.setChecked(getktotomatikses());
        ktotomatikses.setChecked(Utils.getSharedAyarla(getApplicationContext(), "ktotomatikses"));
        ktotomatikses.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //setktotomatikses(isChecked);
                Utils.setSharedAyarla(getApplicationContext(), "ktotomatikses", isChecked);
            }
        });

        suretext.setText(Utils.getwaitTime(getApplicationContext(), "ktwaittime").toString() + " ms");
        ktotomatikgecissure.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                suretext.setText(Integer.toString((i)) + " ms");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Utils.setwaitTime(getApplicationContext(), "ktwaittime", Long.valueOf(seekBar.getProgress()));
            }
        });
    }

    void ayarlarısıfırla() {
        Utils.setSharedAyarla(getApplicationContext(), "kcotomatikekle", false);
        Utils.setSharedAyarla(getApplicationContext(), "kcotomatikdinle", true);
        Utils.setSharedAyarla(getApplicationContext(), "ktotomatikdinle", true);
        Utils.setSharedAyarla(getApplicationContext(), "ktotomatikkaydet", false);
        Utils.setSharedAyarla(getApplicationContext(), "ktbilmedigiotokaydet", false);
        Utils.setSharedAyarla(getApplicationContext(), "ktotomatikgecis", false);
        Utils.setSharedAyarla(getApplicationContext(), "ktotomatikses", true);
        Utils.setwaitTime(getApplicationContext(), "ktwaittime", Long.valueOf("2000"));

        kcotomatikekle.setChecked(false);
        kcotomatikdinle.setChecked(true);
        ktotomatikdinle.setChecked(true);
        ktotomatikkaydet.setChecked(false);
        ktbilmedigiotokaydet.setChecked(false);
        ktotomatikgecis.setChecked(false);
        ktotomatikses.setChecked(true);
        suretext.setText("2000 ms");
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
            case R.id.ayarlariresetle:
                AlertDialog.Builder dialog2 = new AlertDialog.Builder(Ayarlar.this);
                dialog2.setTitle(getString(R.string.ayarreset));
                dialog2.setMessage(getString(R.string.ayarreseticerik));
                dialog2.setPositiveButton(getString(R.string.btnevet), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ayarlarısıfırla();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.ayarlarmenu, menu);
        return true;
    }
}
/*
public void setktotomatikses(Boolean durum) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("ktotomatikses", durum);
        editor.commit();
    }

    public Boolean getktotomatikses() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Boolean deger = preferences.getBoolean("ktotomatikses", true);
        return deger;
    }

    public void setktotomatikgecis(Boolean durum) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("ktotomatikgecis", durum);
        editor.commit();
    }

    public Boolean getktotomatikgecis() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Boolean deger = preferences.getBoolean("ktotomatikgecis", true);
        return deger;
    }

    public void setktbilmedigiotokaydet(Boolean durum) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("ktbilmedigiotokaydet", durum);
        editor.commit();
    }

    public Boolean getktbilmedigiotokaydet() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Boolean deger = preferences.getBoolean("ktbilmedigiotokaydet", true);
        return deger;
    }

    public void setktotomatikkaydet(Boolean durum) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("ktotomatikkaydet", durum);
        editor.commit();
    }

    public Boolean getktotomatikkaydet() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Boolean deger = preferences.getBoolean("ktotomatikkaydet", true);
        return deger;
    }

    public void setktotomatikdinle(Boolean durum) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("ktotomatikdinle", durum);
        editor.commit();
    }

    public Boolean getktotomatikdinle() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Boolean deger = preferences.getBoolean("ktotomatikdinle", true);
        return deger;
    }

    public void setkcotomatikdinle(Boolean durum) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("kcotomatikdinle", durum);
        editor.commit();
    }

    public Boolean getkcotomatikdinle() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Boolean deger = preferences.getBoolean("kcotomatikdinle", true);
        return deger;
    }

    public void setkcotomatikekle(Boolean durum) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("kcotomatikekle", durum);
        editor.commit();
    }

    public Boolean getkcotomatikekle() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Boolean deger = preferences.getBoolean("kcotomatikekle", true);
        return deger;
    }
 */

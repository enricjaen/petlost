package guau.com.mascota;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;


public class MascotasActivity extends ActionBarActivity {

    private MySQLiteDAO db;
    MascotasAdapter adapter;
    private RadioGroup especieGroup;
    private RecyclerView recyclerView;
    private Mascota.STATE estado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mascotas);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            estado = Mascota.STATE.valueOf(extras.getString("estado"));
            listarMascotas(estado);
        }
        ImageButton add = (ImageButton)findViewById(R.id.add_button);
        especieGroup = (RadioGroup) findViewById(R.id.especie);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MascotasActivity.this, NuevaMascotaActivity.class);
                i.putExtra("estado", estado.toString());
                startActivityForResult(i, 1);
            }
        });

        especieGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(-1 == checkedId) {
                    //TODO validar
                }
                RadioButton especie = (RadioButton)findViewById(checkedId);

                Mascota m = new Mascota();
                m.especie = Mascota.ESPECIE.valueOf(especie.getText().toString().toUpperCase());
                adapter.filter(db.filter(m));

            }
        });

        switch(estado) {
            case PERDIDO:
                setTitle(getTitle()+" perdidas");
                break;
            case ENCONTRADO:
                setTitle(getTitle() + " encontradas");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK && requestCode == 1) {
            Mascota m = (Mascota)data.getSerializableExtra("mascota");
            adapter.add(m);
        }

    }


    private void listarMascotas(Mascota.STATE estado) {
        db = new MySQLiteDAO(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        adapter = new MascotasAdapter(this, db.getAll(estado), R.layout.mascota);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mascota, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

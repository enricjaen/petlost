package guau.com.mascota;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;


public class NuevaMascotaActivity extends ActionBarActivity {

    private static final int IMAGEN_GALERIA = 2;
    private ImageView foto;
    private Button galeria;
    private Button fotografiar;

    private Uri uri;
    private final FotoHelper helper = new FotoHelper(NuevaMascotaActivity.this);
    //private Button publicar;
    private EditText ubicacion;
    private EditText cuando;
    private EditText details;
    private Mascota.STATE estado = Mascota.STATE.PERDIDO;
    private RadioGroup especieGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_mascota);
        fotografiar = (Button) findViewById(R.id.fotografiar);
        galeria = (Button) findViewById(R.id.galeria);
        details = (EditText) findViewById(R.id.descripcion);
       // publicar = (Button) findViewById(R.id.publicar);
        foto = (ImageView) findViewById(R.id.foto);
        ubicacion = (EditText) findViewById(R.id.ubicacion);
        cuando = (EditText) findViewById(R.id.cuando);
        especieGroup = (RadioGroup) findViewById(R.id.especie);

        fotografiar.requestFocus();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            estado = Mascota.STATE.valueOf(extras.getString("estado"));
            if (null != extras.getParcelable("direccion")) {
                Address a = (Address) getIntent().getParcelableExtra("direccion");
                if (null != a) {
                    ubicacion.setText(a.getAddressLine(0) + " " + a.getAddressLine(1));
                }
            }
            if (null != extras.getString("cuando")) {
                cuando.setText(extras.getString("cuando"));
            }

            if(null != extras.getParcelable("foto")) {
                uri = extras.getParcelable("foto");
                foto.setImageURI(uri);
            }
        }


        switch(estado) {
            case PERDIDO:
      //          publicar.setText("Publicar en perdidos");
                setTitle(getTitle()+" perdida");
                break;
            case ENCONTRADO:
      //          publicar.setText("Publicar en encontrados");
                setTitle(getTitle() + " encontrada");
                break;
            case ADOPTADO:
                setTitle(getTitle() + " en adopción");
                break;
        }

        fotografiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uri = helper.tomarFoto();
            }
        });

        galeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, IMAGEN_GALERIA);
            }
        });
        /*
        publicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publicar();
            }
        });
        */
    }

    private void publicar()  {
        Mascota m = new Mascota();
        m.state = estado;
        int especieId = especieGroup.getCheckedRadioButtonId();

        if(-1 == especieId) {
            //TODO no se puede dar, siempre hay un radio por defecto
        }

        if ("".equals(ubicacion.getText().toString())) {
            ubicacion.setError("Indica una dirección");
            return;
        }

        RadioButton especie = (RadioButton)findViewById(especieId);

        m.especie = Mascota.ESPECIE.valueOf(especie.getText().toString().toUpperCase());

        if (null != uri) {
            m.image_url = uri.toString();
        }
        m.where = ubicacion.getText().toString();
        m.when = cuando.getText().toString();
        m.details = details.getText().toString();

        MySQLiteDAO dao = new MySQLiteDAO(NuevaMascotaActivity.this);

        long id = dao.addMascota(m);

        launchNotification(m);

        Intent i = new Intent();
        i.putExtra("mascota", m);
        setResult(Activity.RESULT_OK, i);
        finish();

    }
    private void launchNotification(Mascota m){

        //construimos la notificacion
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.huella);
        String title = m.especie.toString().toLowerCase() + " "+m.state.toString().toLowerCase();
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(title + " en "+ m.where + " a las " + m.when);
        mBuilder.setAutoCancel(true);
        mBuilder.setTicker(title);
        mBuilder.setVisibility(NotificationCompat.VISIBILITY_SECRET); //pantalla de bloqueo

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MascotaActivity.class); //activity al que hay q volver (indicado en el manifest)

        Intent resultIntent = new Intent(this, MascotaActivity.class);
        resultIntent.putExtra("mascota",m);

        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent( 0,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationCompat.Action actionA =
                new NotificationCompat.Action(0, "Ver detalles", resultPendingIntent);
        mBuilder.addAction(actionA);

        //lanzamos la notificacion
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (FotoHelper.TAKE_PHOTO_CODE == requestCode) {
            foto = helper.createScaledBitmap(foto, uri);
        }
        if (IMAGEN_GALERIA == requestCode) {
            uri = data.getData();
            foto.setImageURI(uri);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_nueva_mascota, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.publicar) {
            publicar();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

package guau.com.mascota;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainActivity extends ActionBarActivity {

    Uri fotoLocation;

    private static final int TAKE_PHOTO_CODE = 1;

    static final String[] OPCIONES = new String[] {
            "Urgente! Se me ha perdido",
            "Perdidos",
            "Me he encontrado uno",
            "Encontrados",
            "Doy en adopción",
            "En adopción"
    };
    private final FotoHelper fotoHelper = new FotoHelper(this);
    private TextView ubicacion;
    private LocationManager manager;
    Criteria locationCriteria = new Criteria();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView gridView = (GridView) findViewById(R.id.gridView1);

        gridView.setAdapter(new ImageAdapter(this, OPCIONES));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0) {  //urgente perdido
                    Intent i = new Intent(MainActivity.this, NuevaMascotaActivity.class);
                    i.putExtra("estado", Mascota.STATE.PERDIDO.toString());

                    String timeStamp = "hoy a las " + new SimpleDateFormat("HH:mm").format(new Date());
                    i.putExtra("cuando",timeStamp);
                    i.putExtra("direccion",direccion );
                    startActivity(i);
                }

                if(position==1) {  //perdidos
                    Intent i = new Intent(MainActivity.this, MascotasActivity.class);
                    i.putExtra("estado", Mascota.STATE.PERDIDO.toString());
                    startActivity(i);
                }
                if(position==2) {  //nuevo encontrado
                    Intent i = new Intent(MainActivity.this, NuevaMascotaActivity.class);
                    i.putExtra("estado", Mascota.STATE.ENCONTRADO.toString());

                    String timeStamp = "hoy a las " + new SimpleDateFormat("HH:mm").format(new Date());
                    i.putExtra("cuando",timeStamp);
                    i.putExtra("direccion",direccion );
                    startActivity(i);
                }

                if(position==3) { //encontrados
                    Intent i = new Intent(MainActivity.this, MascotasActivity.class);
                    i.putExtra("estado", Mascota.STATE.ENCONTRADO.toString());
                    startActivity(i);
                }
                if(position==4) {  //nuevo adoptado
                    Intent i = new Intent(MainActivity.this, NuevaMascotaActivity.class);
                    i.putExtra("estado", Mascota.STATE.ADOPTADO.toString());

                    String timeStamp = "hoy a las " + new SimpleDateFormat("HH:mm").format(new Date());
                    i.putExtra("cuando",timeStamp);
                    i.putExtra("direccion",direccion );
                    startActivity(i);
                }

                if(position==5) { //adoptados
                    Intent i = new Intent(MainActivity.this, MascotasActivity.class);
                    i.putExtra("estado", Mascota.STATE.ADOPTADO.toString());
                    startActivity(i);
                }

            }
        });

        ubicacion = (TextView)findViewById(R.id.ubicacion);

        actualizarProveedor();


    }




    private void actualizarProveedor() {
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String providerName = manager.getBestProvider(locationCriteria, true);

//        Toast.makeText(this, "proveedor obtenido: " + providerName, Toast.LENGTH_SHORT).show();

        manager.requestLocationUpdates(providerName,1,1,new Localization());
    }



    private class Localization implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            updateLocation(location);
            ubicacion.setText("Estoy en: "+direccion.getAddressLine(1));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {
            updateLocation(null);
        }
    }

    Address direccion;

    private void updateLocation(Location location) {
        if(null != location ) {

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                List<Address> direcciones = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                if(null!=direcciones && 0 < direcciones.size()) {
                    direccion = direcciones.get(0);
                    //Toast.makeText(this, "proveedor obtenido: " + a.toString(), Toast.LENGTH_SHORT).show();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode == TAKE_PHOTO_CODE) {
            Intent i = new Intent(MainActivity.this, NuevaMascotaActivity.class);
            i.putExtra("estado", Mascota.STATE.ENCONTRADO.toString());
            i.putExtra("foto", fotoLocation);
            i.putExtra("direccion",direccion );
            startActivity(i);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.camera) {
            fotoLocation = fotoHelper.tomarFoto();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }




}

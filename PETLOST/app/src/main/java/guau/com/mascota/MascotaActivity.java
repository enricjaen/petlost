package guau.com.mascota;

import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class MascotaActivity extends ActionBarActivity {

    private ImageView foto;
    private TextView ubicacion;
    private TextView cuando;
    private TextView details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mascota);
        details = (TextView) findViewById(R.id.descripcion);
        foto = (ImageView) findViewById(R.id.foto);
        ubicacion = (TextView) findViewById(R.id.ubicacion);
        cuando = (TextView) findViewById(R.id.cuando);

        Mascota m = (Mascota) getIntent().getSerializableExtra("mascota");

        if(null != m.image_url) {
            if(null!=m.image_url && m.image_url.length()>0) {
                foto.setImageBitmap(FotoHelper.getImageBitmapFromUri(this, Uri.parse(m.image_url)));
            }
        }

        if(null != m.details) {
            details.setText(m.details);
        }

        if(null != m.where) {
            ubicacion.setText(m.where);
        }

        if(null != m.when) {
            cuando.setText(m.when);
        }
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

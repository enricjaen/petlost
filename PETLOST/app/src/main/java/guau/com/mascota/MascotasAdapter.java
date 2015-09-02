package guau.com.mascota;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;


public class MascotasAdapter extends RecyclerView.Adapter<MascotasAdapter.ViewHolder> {


    private List<Mascota> items;
    private int itemLayout;
    private Context context;

    public MascotasAdapter(Context context, List<Mascota> items, int itemLayout) {
        this.context = context;
        this.items = items;
        this.itemLayout = itemLayout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d("mascota","items "+items);
        Log.d("mascota","items size"+items.size());
        Log.d("mascota","pos="+position);
        final Mascota mascota = items.get(position);

        if(null!= mascota.details) {
            holder.details.setText(mascota.details);
        }
        if(null!=mascota.image_url && mascota.image_url.length()>0) {
            //Picasso.with(context).load(mascota.image_url).fit().into(holder.image);

            //da un error de tamaño 0 --> holder.image = FotoHelper.createScaledBitmap(holder.image, Uri.parse(mascota.image_url));

            holder.image.setImageBitmap(FotoHelper.getImageBitmapFromUri(context,Uri.parse(mascota.image_url)));
        }

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent( MascotasAdapter.this.context, MascotaActivity.class);
                i.putExtra("mascota",mascota);
                MascotasAdapter.this.context.startActivity(i);
            }
        });

        /*
        holder.itemView.setTag(mascota);

        holder.ckSelected.setChecked(mascota.isSelected());
        holder.ckSelected.setTag(mascota);
        holder.ckSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                Pelicula pelicula = (Pelicula)cb.getTag();
                pelicula.setSelected(cb.isChecked());
            }
        });
*/
    }

    public void filter(List<Mascota> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void add(Mascota item) {
        items.add(item);
        notifyDataSetChanged();
        //notifyItemInserted(position);
    }

    public void remove(Mascota item) {
        int position = items.indexOf(item);
        items.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public List<Mascota> getItems() {
        return items;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView details;
        //public CheckBox ckSelected;

        public ViewHolder(View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.foto);
            details = (TextView) itemView.findViewById(R.id.descripcion);
            //info = (Button) itemView.findViewById(R.id.buttonRowInfo);
            //ckSelected = (CheckBox) itemView.findViewById(R.id.checkBox);
        }
    }
}

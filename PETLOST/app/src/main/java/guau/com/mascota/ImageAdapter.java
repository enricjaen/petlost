package guau.com.mascota;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter {
    private Context context;
    private final String[] gridValues;

    public ImageAdapter(Context context, String[] gridValues) {
        this.context = context;
        this.gridValues = gridValues;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {

            gridView = new View(context);

            // get layout from grid.xml
            gridView = inflater.inflate(R.layout.grid_item, null);

            // set value into textview
            TextView textView = (TextView) gridView
                    .findViewById(R.id.grid_item_label);
            textView.setText(gridValues[position]);

            // set image based on selected text
            ImageView imageView = (ImageView) gridView
                    .findViewById(R.id.grid_item_image);

            String gridValue = gridValues[position];

            if (gridValue.equals("Perdidos")) {
                imageView.setImageResource(R.drawable.perro_perdido);
            }
            /*
            else if (gridValue.equals("iOS")) {
                imageView.setImageResource(R.drawable.ios_logo);
            } else if (gridValue.equals("Blackberry")) {
                imageView.setImageResource(R.drawable.blackberry_logo);
            } else {
                imageView.setImageResource(R.drawable.android_logo);
            }*/

        } else {
            gridView = (View) convertView;
        }

        return gridView;
    }

    @Override
    public int getCount() {
        return gridValues.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}
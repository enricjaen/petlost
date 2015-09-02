package guau.com.mascota;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FotoHelper {

    public static final int TAKE_PHOTO_CODE = 1;

    Activity activity;
    private Uri uri;


    public FotoHelper(Activity activity) {
        this.activity = activity;
    }

    public Uri getUri() {
        return uri;
    }

    Uri tomarFoto() {
        File f = null;
        try {
            f = getTmpFile();
            uri = Uri.fromFile(f);

        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        activity.startActivityForResult(i, TAKE_PHOTO_CODE);
        return uri;
    }

    private File getTmpFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMDD_HHMMSS").format(new Date());
        String imageFile = timeStamp + "_";
        File image = File.createTempFile(imageFile, ".jpg", Environment.getExternalStorageDirectory());
        return image;
    }


    public static ImageView createScaledBitmap(ImageView foto, Uri uri) {
        foto.setImageURI(uri);
        BitmapDrawable drawable = (BitmapDrawable) foto.getDrawable();
        Bitmap bmp = drawable.getBitmap();
        Bitmap b = Bitmap.createScaledBitmap(bmp, foto.getWidth(), foto.getHeight(), false);
        foto.setImageBitmap(b);
        return foto;
    }

    public static Bitmap getImageBitmapFromUri(Context context, Uri uri) {
        try {
            InputStream is = context.getContentResolver().openInputStream(uri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;

            return BitmapFactory.decodeStream(is, null, options);
        } catch (FileNotFoundException e) {
            return null;
        }

    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
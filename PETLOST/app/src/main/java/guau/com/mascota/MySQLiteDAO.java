package guau.com.mascota;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class MySQLiteDAO extends SQLiteOpenHelper {

    // Versión de la base de datos
    private static final int DATABASE_VERSION = 1;
    // Nombre de la base de datos
    private static final String DATABASE_NAME = "MascotaDB";

    public MySQLiteDAO(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public MySQLiteDAO(Context context, String dbname) {
        super(context, dbname, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Sentencia SQL de creación de la tabla peliculas
        String CREATE_MASCOTA_TABLE = "CREATE TABLE "+ TABLE_MASCOTA + " ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "state TEXT, " +
                "image_url TEXT, " +
                "address TEXT, " +
                "time TEXT, " +
                "details TEXT," +
                "specie TEXT" +
                ")";

        // Crear tabla "peliculas"
        db.execSQL(CREATE_MASCOTA_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Borrar tablas antiguas si existen
        db.execSQL("DROP TABLE IF EXISTS perdidos");

        // Crear tabla peliculas nueva
        this.onCreate(db);
    }
    //---------------------------------------------------------------------

    /**
     * Completar las operaciones CRUD de Pelicula
     */

    private static final String TABLE_MASCOTA = "mascotas";

    // Nombres de las columnas de la tabla de Peliculas
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_GENRE = "genre";
    private static final String KEY_WHERE = "address";
    private static final String KEY_WHEN = "time";
    private static final String KEY_DETAILS = "details";
    private static final String KEY_IMAGE_URL = "image_url";
    private static final String KEY_STATE = "state";
    private static final String KEY_ESPECIE = "specie";

    private static final String[] COLUMNS = {KEY_ID, KEY_STATE, KEY_WHEN, KEY_WHERE, KEY_IMAGE_URL, KEY_DETAILS, KEY_ESPECIE};

    public long addMascota(Mascota mascota) {

        SQLiteDatabase db = this.getWritableDatabase();

        //TODO Insertar una Pelicula en la BD
        ContentValues valores = new ContentValues();

        valores.put(KEY_STATE, mascota.state.toString());
        valores.put(KEY_IMAGE_URL, mascota.image_url);
        valores.put(KEY_WHERE, mascota.where);
        valores.put(KEY_WHEN, mascota.when);
        valores.put(KEY_DETAILS, mascota.details);
        valores.put(KEY_ESPECIE, mascota.especie.toString());


        long res = db.insert(TABLE_MASCOTA, null, valores);
        //db.close();
        return res;
    }

    public Mascota getMascota(long id) {

        Mascota mascota = null;

        SQLiteDatabase db = this.getReadableDatabase();

        //TODO Obtener una Pelicula

        Cursor cursor = db.query(TABLE_MASCOTA, COLUMNS, "id = ?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (null != cursor) {
            cursor.moveToFirst();
        }
        mascota = new Mascota();
        mascota.id = Integer.parseInt(cursor.getString(0));
        mascota.state = Mascota.STATE.valueOf(cursor.getString(1));
        mascota.when = cursor.getString(2);
        mascota.where = cursor.getString(3);
        mascota.image_url = cursor.getString(4);
        mascota.details = cursor.getString(5);

        db.close();

        return mascota;
    }

    public List<Mascota> getAll(Mascota.STATE state) {
        List<Mascota> mascotas = new ArrayList<Mascota>();

        SQLiteDatabase db = this.getWritableDatabase();

        //TODO Obtener todas las Peliculas
        Cursor cursor = db.rawQuery("SELECT * from " + TABLE_MASCOTA + " WHERE state = ?", new String[]{state.toString()});

        if (cursor.moveToFirst()) {
            do {
                Mascota mascota = new Mascota();
                mascota.id = Integer.parseInt(cursor.getString(0));
                mascota.state = Mascota.STATE.valueOf(cursor.getString(1));
                mascota.image_url = cursor.getString(2);
                mascota.where = cursor.getString(3);
                mascota.when = cursor.getString(4);
                mascota.details = cursor.getString(5);
                mascota.especie = Mascota.ESPECIE.valueOf(cursor.getString(6));

                mascotas.add(mascota);
            } while (cursor.moveToNext());
        }
        db.close();

        return mascotas;
    }

    public List<Mascota> filter(Mascota m) {
        List<Mascota> mascotas = new ArrayList<Mascota>();

        SQLiteDatabase db = this.getWritableDatabase();

        //TODO Obtener todas las Peliculas

        String sqlClause = "SELECT * from " + TABLE_MASCOTA;
        List<String> selectionArgs = new ArrayList<String>();

        if(null != m.especie  && Mascota.ESPECIE.TODOS != m.especie) {
            sqlClause+=" WHERE "+KEY_ESPECIE+" = ? ";
            selectionArgs.add(m.especie.toString());
        }
        Cursor cursor = db.rawQuery(sqlClause, selectionArgs.toArray(new String[selectionArgs.size()]));

        if (cursor.moveToFirst()) {
            do {
                Mascota mascota = new Mascota();
                mascota.id = Integer.parseInt(cursor.getString(0));
                mascota.state = Mascota.STATE.valueOf(cursor.getString(1));
                mascota.image_url = cursor.getString(2);
                mascota.where = cursor.getString(3);
                mascota.when = cursor.getString(4);
                mascota.details = cursor.getString(5);
                mascota.especie = Mascota.ESPECIE.valueOf(cursor.getString(6));

                mascotas.add(mascota);
            } while (cursor.moveToNext());
        }
        db.close();

        return mascotas;
    }



/*
    public void updatePelicula(Pelicula pelicula) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("title", pelicula.getTitle());
        values.put("genre", pelicula.getGenre());
        //TODO Actualizar una Pelicula

        db.update(TABLE_MASCOTA,values, KEY_ID+ " =?", new String[] {String.valueOf(pelicula.getId())});

        db.close();



    }

    public void deletePelicula(Pelicula pelicula) {

        SQLiteDatabase db = this.getWritableDatabase();

        //TODO Borrar una Pelicula
        db.delete(TABLE_MASCOTA,KEY_ID+ " = ?",new String[] {String.valueOf(pelicula.getId())} );

        db.close();


    }

    public void deleteAllPeliculas(){


        SQLiteDatabase db = this.getWritableDatabase();

        //TODO Borrar todas las Peliculas

        db.execSQL("DELETE FROM "+ TABLE_MASCOTA);
        db.close();
    }
   */
}
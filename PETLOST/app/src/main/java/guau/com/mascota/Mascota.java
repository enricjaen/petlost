package guau.com.mascota;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Usuario on 15/06/2015.
 */
public class Mascota implements Serializable {
    public long id;
    public String image_url;
    public String when;
    public String where;
    public String details;
    public STATE state;
    public ESPECIE especie;

    public enum STATE {
        PERDIDO, ENCONTRADO, ADOPTADO;

    }

    public enum ESPECIE {
        PERRO, GATO, TODOS;
    }
}

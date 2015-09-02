package guau.com.mascota;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.RenamingDelegatingContext;

import java.util.List;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class DBTest extends ApplicationTestCase<Application> {

    private MySQLiteDAO dao;
    public DBTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");

        dao = new MySQLiteDAO(context,null);
    }

    public void testAllVistos() {
        List<Mascota> allVistos = dao.getAll(Mascota.STATE.AVISTADO);
        assertEquals(0,allVistos.size());
    }

    public void testAddVisto() {
        Mascota m = createMascota(Mascota.ESPECIE.PERRO);
        long id  = dao.addMascota(m);
        assertNotSame(-1,id);

        m=dao.getMascota(id);
        assertEquals("today",m.when);

        assertEquals(Mascota.STATE.AVISTADO, m.state);
    }

    public void testFilterByPerro() {
        dao.addMascota(createMascota(Mascota.ESPECIE.PERRO));
        dao.addMascota(createMascota(Mascota.ESPECIE.GATO));
        Mascota m = new Mascota();
        m.especie = Mascota.ESPECIE.PERRO;
        List<Mascota> items = dao.filter(m);

        assertEquals(1, items.size());
    }



    public void testFilterByCualquiera() {
        dao.addMascota(createMascota(Mascota.ESPECIE.PERRO));
        dao.addMascota(createMascota(Mascota.ESPECIE.GATO));
        Mascota m = new Mascota();
        m.especie = Mascota.ESPECIE.TODOS;
        List<Mascota> items = dao.filter(m);

        assertEquals(2, items.size());
    }

    private Mascota createMascota(Mascota.ESPECIE especie) {
        Mascota m = new Mascota();
        m.details="bla";
        m.where="some address";
        m.image_url="file:///";
        m.when="today";
        m.state= Mascota.STATE.AVISTADO;
        m.especie = especie;
        return m;
    }
}


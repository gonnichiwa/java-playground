import com.sun.istack.internal.Nullable;
import org.junit.Test;

public class InterfaceExtendTest {

    @Test
    public void executable(){
//        ParentIF parent = new ParentIfImpl();
//        parent.Pa();
        SonIF son = new SonIfImpl();
        son.Pa();
    }

}

interface ParentIF {
    void Pa();
}

class ParentIfImpl implements ParentIF {
    @Override
    public void Pa() {
        System.out.println("Pa of ParentIF");
    }

    @Nullable
    private String name;
}

interface SonIF extends ParentIF {
    void Sa();
}

class SonIfImpl implements SonIF {

    @Override
    public void Pa() {

    }

    @Override
    public void Sa() {
        System.out.println("Sa of SonIF");
    }
}
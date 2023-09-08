package AnalizadorSintactico;

import AnalizadorLexico.*;

import java.io.IOException;

public class AnalizadorSintactico {

    private AnalizadorLexico analizadorLexico;
    private Token tokenActual;

    public AnalizadorSintactico(AnalizadorLexico analizadorLexico) throws ExcepcionLexica, ExcepcionSintactica, IOException {
        this.analizadorLexico = analizadorLexico;
        this.tokenActual = analizadorLexico.proximoToken();
        inicial();
    }

    private void match(String nombreToken) throws ExcepcionLexica, IOException, ExcepcionSintactica {
        if(nombreToken.equals(tokenActual.getToken_id()))
            tokenActual = analizadorLexico.proximoToken();
        else
            throw new ExcepcionSintactica();
    }

    private void inicial() throws ExcepcionLexica, ExcepcionSintactica, IOException {
        //luego de hacer todas las derivaciones
        match("EOF");
    }
}

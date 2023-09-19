package AnalizadorSemantico;
import AnalizadorLexico.Token;
public class ErrorSemantico {

    private Token token;
    private String mensajeError;

    public ErrorSemantico(Token token, String mensajeError){
        this.token = token;
        this.mensajeError = mensajeError;
    }

    public Token obtenerTokenConError(){
        return this.token;
    }

    public String obtenerMensajeError(){
        return this.mensajeError;
    }
}

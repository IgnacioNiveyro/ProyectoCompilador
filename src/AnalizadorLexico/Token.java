package AnalizadorLexico;

public class Token {
    private String token_id;
    private String lexema;
    private int nro_linea;
    public Token(String token_id, String lexema, int nro_linea){
        this.token_id = token_id;
        this.lexema = lexema;
        this.nro_linea = nro_linea;
    }

    public String toString(){
        return "("+this.token_id+","+this.lexema+","+this.nro_linea+")";
    }

    public String getToken_id(){
        return this.token_id;
    }

    public int getNro_linea(){return this.nro_linea;}

    public String getLexema() { return this.lexema;}
}

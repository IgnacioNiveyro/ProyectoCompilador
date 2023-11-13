///exitosamente

// Este test pasa trivialmente. Esto es porque si des-comento esa linea se queda ciclando
// esperando un int que no llega nunca. Use el codigo que salio de compilar para correrlo
// manualmente, y ahi si anduvo
class Init {
    static void main() {
        var i = 0;
        //i = System.read();
        debugPrint(i);
    }
}

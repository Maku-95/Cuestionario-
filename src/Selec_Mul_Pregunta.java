public class Selec_Mul_Pregunta extends Pregunta {
    private final String[] elecciones;
    private final int correcta;  

    public Selec_Mul_Pregunta(String text, String[] elecciones, int correcta, int peso) {
        super(text, peso);
        if (elecciones == null || elecciones.length < 2)
            throw new IllegalArgumentException("Debe haber al menos 2 elecciones");
        if (correcta < 0 || correcta >= elecciones.length)
            throw new IllegalArgumentException("Indice de respuesta correcta fuera de rango");
        this.elecciones = elecciones.clone();  
        this.correcta = correcta;
    }

    @Override
    public boolean buscar() {
        System.out.println(getText());
        for (int i = 0; i < elecciones.length; i++)
            System.out.println("    " + (char) ('a' + i) + ". " + elecciones[i]);

        while (true) {
            System.out.print("ingrese la letra de su eleccion (STOP para cancelar): ");
            String r = leerLinea().toLowerCase();
            if (r.length() == 1) {
                int indice = r.charAt(0) - 'a';
                if (indice >= 0 && indice < elecciones.length) {
                    if (indice == correcta) System.out.println("correcta!");
                    else System.out.println("no, la respuesta correcta es "
                            + (char) ('a' + correcta) + ". " + elecciones[correcta]);
                    return indice == correcta;
                }
            }
            System.out.println("Opcion invalida. Use una letra entre 'a' y '"
                    + (char) ('a' + elecciones.length - 1) + "'.");
        }
    }
}
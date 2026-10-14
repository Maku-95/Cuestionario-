public class TFpregunta extends Pregunta {
    private final boolean correcta;

    public TFpregunta(String text, boolean correcta, int peso) {
        super(text, peso);
        this.correcta = correcta;
    }

    @Override
    public boolean buscar() {
        while (true) {   
            System.out.print(getText() + " (ingrese T o F, STOP para cancelar): ");
            String r = leerLinea();
            if (r.equalsIgnoreCase("t") || r.equalsIgnoreCase("f")) {
                boolean resp = r.equalsIgnoreCase("t");
                if (resp == correcta) System.out.println("correcta!");
                else System.out.println("no, la respuesta es "
                        + (correcta ? "verdadera" : "falsa"));
                return resp == correcta;
            }
            System.out.println("Entrada invalida, intente otra vez.");
        }
    }
}
public class Resp_Cortas_Pregunta extends Pregunta {
    private final String correcta;

    public Resp_Cortas_Pregunta(String text, String correcta, int peso) {
        super(text, peso);
        if (correcta == null || correcta.trim().isEmpty())
            throw new IllegalArgumentException("La respuesta correcta no puede estar vacia");
        this.correcta = correcta.trim();
    }

    @Override
    public boolean buscar() {
        while (true) {
            System.out.print(getText() + " (STOP para cancelar): ");
            String r = leerLinea();
            if (r.isEmpty()) {  
                System.out.println("Debe escribir una respuesta.");
                continue;
            }
            if (r.equalsIgnoreCase(correcta)) {
                System.out.println("correcta!");
                return true;
            }
            System.out.println("no, la respuesta correcta es " + correcta);
            return false;
        }
    }
}
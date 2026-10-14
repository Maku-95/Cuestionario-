import java.util.Scanner;

public abstract class Pregunta {
    private int peso;
    private final String text;
    protected static final Scanner sc = new Scanner(System.in);

    public Pregunta(String text, int peso) {
        this.text = text;
        setPeso(peso);
    }

    public int getPeso() { return peso; }

    public void setPeso(int peso) {
        if (peso <= 0) throw new IllegalArgumentException("Peso debe ser > 0");
        this.peso = peso;
    }

    public String getText() { return text; }


    protected static String leerLinea() {
        String r = sc.nextLine().trim();
        if (r.equalsIgnoreCase("stop"))
            throw new ExamenCancelado("Examen cancelado por el usuario");
        return r;
    }

    public abstract boolean buscar();
}
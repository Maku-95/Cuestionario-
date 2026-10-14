public class Exam {
    private static final int MAX_PREGUNTAS = 10;
    private final Pregunta[] preguntas = new Pregunta[MAX_PREGUNTAS];
    private int contador = 0; 

    public void agregaPregunta(Pregunta p) {
        if (p == null)
            throw new IllegalArgumentException("La pregunta no puede ser null");
        if (contador >= MAX_PREGUNTAS)
            throw new IllegalStateException("El examen ya tiene " + MAX_PREGUNTAS + " preguntas");
        preguntas[contador++] = p;
    }

    public int darExam() {
        if (contador == 0)
            throw new IllegalStateException("El examen no tiene preguntas");

        int pesoTotal = 0;
        int pesoObtenido = 0;

        for (int i = 0; i < contador; i++) {
            Pregunta p = preguntas[i];
            pesoTotal += p.getPeso();
            if (p.buscar()) { 
                pesoObtenido += p.getPeso();
            }
        }
        return (int) Math.round(pesoObtenido * 100.0 / pesoTotal);
    }
}
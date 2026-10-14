import java.util.*;

public class ExamDemo {
    public static void main(String[] args) {

        List<Pregunta> bancoTF = new ArrayList<>();
        bancoTF.add(new TFpregunta("La Revolucion Francesa comenzo en 1789", true, 1));
        bancoTF.add(new TFpregunta("Cristobal Colon llego a America en 1592", false, 1));
        bancoTF.add(new TFpregunta("La Segunda Guerra Mundial comenzo en 1914", false, 1));
        bancoTF.add(new TFpregunta("Napoleon Bonaparte nacio en Inglaterra", false, 1));
        bancoTF.add(new TFpregunta("La capital del Imperio inca era Cusco", true, 1));

        List<Pregunta> bancoSM = new ArrayList<>();
        bancoSM.add(new Selec_Mul_Pregunta("Quien fue el primer presidente de Estados Unidos?",
            new String[]{"Thomas Jefferson", "George Washington",
                         "Abraham Lincoln", "John Adams"}, 1, 1));
        bancoSM.add(new Selec_Mul_Pregunta("Que civilizacion construyo Machu Picchu?",
            new String[]{"Maya", "Azteca", "Inca"}, 2, 1));
        bancoSM.add(new Selec_Mul_Pregunta("Cuando se proclamo la independencia de Chile?",
            new String[]{"1810", "1818", "1823", "1879"}, 1, 1));
        bancoSM.add(new Selec_Mul_Pregunta("Que navegante llego a America en 1492?",
            new String[]{"Vasco da Gama", "Cristobal Colon", "Fernando de Magallanes"}, 1, 1));

        List<Pregunta> bancoRC = new ArrayList<>();
        bancoRC.add(new Resp_Cortas_Pregunta("En que ciudad esta el Coliseo?", "Roma", 1));
        bancoRC.add(new Resp_Cortas_Pregunta("En que pais estan las piramides de Giza?",
            "Egipto", 1));
        bancoRC.add(new Resp_Cortas_Pregunta("En que pais cayo el Muro de Berlin?",
            "Alemania", 1));
        bancoRC.add(new Resp_Cortas_Pregunta("Que lider inca fue capturado por Pizarro?",
            "Atahualpa", 1));

        boolean otroExamen = true;
        while (otroExamen) {
            try {
                Exam miExam = armarExamen(bancoTF, bancoSM, bancoRC);
                int puntaje = miExam.darExam();
                System.out.println("su resultado es " + puntaje + "%");
            } catch (ExamenCancelado e) {
                System.out.println(e.getMessage() + ". Vaya a estudiar!");
            } catch (IllegalStateException | IllegalArgumentException e) {
                System.out.println("Error en el examen: " + e.getMessage());
                return;   
            }
            otroExamen = quiereOtro();
        }
        System.out.println("Hasta luego!");
    }

    private static Exam armarExamen(List<Pregunta> tf, List<Pregunta> sm, List<Pregunta> rc) {
        List<Pregunta> seleccion = new ArrayList<>();
        seleccion.addAll(elegir(tf, 3));
        seleccion.addAll(elegir(sm, 3));
        seleccion.addAll(elegir(rc, 3));

        Exam examen = new Exam();
        for (Pregunta p : seleccion) examen.agregaPregunta(p);
        return examen;
    }

    private static List<Pregunta> elegir(List<Pregunta> banco, int n) {
        if (banco.size() < n)
            throw new IllegalArgumentException(
                "El banco tiene " + banco.size() + " preguntas y se piden " + n);
        List<Pregunta> copia = new ArrayList<>(banco);
        Collections.shuffle(copia);
        return copia.subList(0, n);
    }

    private static boolean quiereOtro() {
        while (true) {
            System.out.print("Desea comenzar un nuevo examen? (S/N): ");
            String r = Pregunta.sc.nextLine().trim();
            if (r.equalsIgnoreCase("s")) return true;
            if (r.equalsIgnoreCase("n")) return false;
            System.out.println("Responda S o N.");
        }
    }
}
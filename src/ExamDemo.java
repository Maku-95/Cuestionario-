import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ExamDemo {
    public static void main(String[] args) {

        List<Pregunta> bancoTF = new ArrayList<>();
        bancoTF.add(new TFpregunta("La Revolucion Francesa comenzo en 1789", true, 1));
        bancoTF.add(new TFpregunta("La primera bandera de chile fue creada por Diego Portales", false, 1));
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

        System.out.println("=== Examen de Historia basica ===");
        String[] alumno = pedirAlumno();   // alumno[0] = nombre, alumno[1] = rut

        boolean otroExamen = true;
        while (otroExamen) {
            try {
                Exam miExam = armarExamen(bancoTF, bancoSM, bancoRC);
                int puntaje = miExam.darExam();
                System.out.println("su resultado es " + puntaje + "%");
                guardarResultado(alumno, miExam.getCorrectas() + " de "
                        + miExam.getTotalPreguntas() + " | " + puntaje + "% | COMPLETADO");
            } catch (Pregunta.ExamenCanceladoException e) {
                System.out.println(e.getMessage() + ". Vaya a estudiar!");
                guardarResultado(alumno, "- | - | CANCELADO");
            } catch (IllegalStateException | IllegalArgumentException e) {
                System.out.println("Error en el examen: " + e.getMessage());
                return;   // un error de configuracion no se arregla repitiendo
            }
            otroExamen = confirmar("Desea comenzar un nuevo examen?");
            if (otroExamen && !confirmar("Es el mismo alumno?")) {
                alumno = pedirAlumno();
            }
        }
        System.out.println("Hasta luego!");
    }
    // Pide nombre y RUT, y muestra los resultados anteriores de ese alumno
    private static String[] pedirAlumno() {
        String nombre = pedirNombre();
        String rut = pedirRut();
        mostrarHistorial(rut);
        return new String[]{nombre, rut};
    }
    private static String pedirNombre() {
        while (true) {
            System.out.print("Ingrese su nombre completo: ");
            String n = Pregunta.sc.nextLine().trim().replaceAll("\\s+", " ");
            if (n.length() >= 3 && n.length() <= 50 && !n.matches(".*\\d.*")) return n;
            System.out.println("Nombre invalido: use entre 3 y 50 caracteres, sin numeros.");
        }
    } 
    private static String pedirRut() {
        while (true) {
            System.out.print("Ingrese su RUT (ej: 12.345.678-5): ");
            String rut = Pregunta.sc.nextLine().trim()
                    .replace(".", "").replace("-", "").toUpperCase();
            if (rutValido(rut)) return rut;
            System.out.println("RUT invalido, revise el numero y el digito verificador.");
        }
    }
    private static boolean rutValido(String rut) {
        if (!rut.matches("\\d{7,8}[0-9K]")) return false;
        String cuerpo = rut.substring(0, rut.length() - 1);
        char dv = rut.charAt(rut.length() - 1);

        int suma = 0;
        int factor = 2;  
        for (int i = cuerpo.length() - 1; i >= 0; i--) {
            suma += (cuerpo.charAt(i) - '0') * factor;
            factor = (factor == 7) ? 2 : factor + 1;
        }
        int resto = 11 - (suma % 11);
        char esperado = (resto == 11) ? '0' : (resto == 10) ? 'K' : (char) ('0' + resto);
        return dv == esperado;
    }
    // 123456785 -> 12.345.678-5
    private static String formatearRut(String rut) {
        String cuerpo = rut.substring(0, rut.length() - 1);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cuerpo.length(); i++) {
            if (i > 0 && (cuerpo.length() - i) % 3 == 0) sb.append('.');
            sb.append(cuerpo.charAt(i));
        }
        return sb + "-" + rut.charAt(rut.length() - 1);
    }
    private static void guardarResultado(String[] alumno, String detalle) {
        try {
            Path carpeta = Paths.get("resultados");
            Files.createDirectories(carpeta);
            Path archivo = carpeta.resolve(alumno[1] + ".txt");
            String fecha = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            String linea = fecha + " | " + alumno[0] + " | " + formatearRut(alumno[1])
                    + " | " + detalle + System.lineSeparator();
            Files.write(archivo, linea.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            System.out.println("Resultado guardado en " + archivo);
        } catch (IOException e) {
            System.out.println("No se pudo guardar el resultado: " + e.getMessage());
        }
    }
    private static void mostrarHistorial(String rut) {
        Path archivo = Paths.get("resultados", rut + ".txt");
        if (!Files.exists(archivo)) {
            System.out.println("Es el primer examen de este alumno.");
            return;
        }
        try {
            System.out.println("Resultados anteriores:");
            for (String linea : Files.readAllLines(archivo, StandardCharsets.UTF_8))
                System.out.println("  " + linea);
        } catch (IOException e) {
            System.out.println("No se pudo leer el historial: " + e.getMessage());
        }
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
    private static boolean confirmar(String pregunta) {
        while (true) {
            System.out.print(pregunta + " (S/N): ");
            String r = Pregunta.sc.nextLine().trim();
            if (r.equalsIgnoreCase("s")) return true;
            if (r.equalsIgnoreCase("n")) return false;
            System.out.println("Responda S o N.");
        }
    }
}
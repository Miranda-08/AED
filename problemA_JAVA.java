import java.io.*;
import java.util.*;

// CHAT GPT:
//      https://chatgpt.com/s/t_6935967fa8488191b0f19e51d858bd04


public class problemA_JAVA {

    public static void main(String[] args) throws Exception {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        // ---- LEITURA nmovies ----
        int nmovies = Integer.parseInt(br.readLine());

        // genre -> sorted set of titles
        TreeMap<String, TreeSet<String>> genres = new TreeMap<>();

        // ---- LER OS FILMES ----
        for (int i = 0; i < nmovies; i++) {
            String line = br.readLine();
            // formato: movie_id<TAB>movie_title<TAB>movie_genres

            String[] parts = line.split("\t");

            String title = parts[1];
            String genreField = parts[2];

            // filmes sem género
            if (genreField.equals("(no genres listed)"))
                continue;

            // vários géneros separados por "|"
            String[] gs = genreField.split("\\|");

            for (String g : gs) {
                genres.putIfAbsent(g, new TreeSet<>()); // criar se não existir
                genres.get(g).add(title);               // inserir filme ordenado
            }
        }

        // ---- IGNORAR A PARTE DAS RATINGS ----

        // ---- IMPRIMIR RESULTADO ORDENADO ----
        for (String g : genres.keySet()) {
            System.out.println(g);
            for (String title : genres.get(g)) {
                System.out.println(title);
            }
        }
    }
}

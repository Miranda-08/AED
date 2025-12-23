import java.io.*;
import java.util.*;

public class problemC_JAVA {

    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream));
            tokenizer = null;
        }

        public String nextLine() {
            try {
                return reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public int nextInt() {
            return Integer.parseInt(nextLine());
        }
  }

    static class MovieScore implements Comparable<MovieScore> {
        String title;
        double avg;

        MovieScore(String t, double a) {
            title = t;
            avg = a;
        }

        @Override
        public int compareTo(MovieScore o) {
            if (this.avg > o.avg) return -1;
            if (this.avg < o.avg) return 1;
            return this.title.compareTo(o.title);
        }
    }

    public static void main(String[] args) throws Exception {
        InputReader in = new InputReader(System.in);
        PrintWriter out = new PrintWriter(System.out);

        // ---- 1) LER MOVIES ----
        int nmovies = in.nextInt();

        HashMap<Integer,Integer> idToIndex = new HashMap<>(nmovies * 2);
        String[] titles = new String[nmovies];
        @SuppressWarnings("unchecked")
        ArrayList<String>[] genresOfMovie = new ArrayList[nmovies];

        @SuppressWarnings("unchecked")
        ArrayList<Integer>[] movieToUsers = new ArrayList[nmovies];
        for (int i = 0; i < nmovies; i++) {
            movieToUsers[i] = new ArrayList<>();
        }


        for (int i = 0; i < nmovies; i++) {
            String line = in.nextLine();
            String[] parts = line.split("\t");

            int movieId = Integer.parseInt(parts[0]);
            String title = parts[1];
            String genreField = parts[2];

            idToIndex.put(movieId, i);
            titles[i] = title;

            genresOfMovie[i] = new ArrayList<>();

            if (!genreField.equals("(no genres listed)")) {
                String[] gs = genreField.split("\\|");
                for (String g : gs) genresOfMovie[i].add(g);
            }
        }

        // ---- 2) LER RATINGS ----
        int nratings = in.nextInt();

        double[] sum = new double[nmovies];
        int[] count = new int[nmovies];

        // user -> set of movies seen
        HashMap<Integer, HashSet<Integer>> userToMovies = new HashMap<>();

        for (int i = 0; i < nratings; i++) {
            String line = in.nextLine();
            String[] parts = line.split("\t");

            int rater = Integer.parseInt(parts[0]);
            int movieId = Integer.parseInt(parts[1]);
            double rating = Double.parseDouble(parts[2]);

            Integer idxObj = idToIndex.get(movieId);
            if (idxObj == null) continue;
            int idx = idxObj;

            sum[idx] += rating;
            count[idx]++;

            userToMovies.putIfAbsent(rater, new HashSet<>());
            userToMovies.get(rater).add(idx);

            movieToUsers[idx].add(rater);
        }

        // ---- 3) LER USER U ----
        int targetUser = in.nextInt();

        // ---- 4) LER GÉNERO PREFERIDO ----
        String preferredGenre = in.nextLine();

        // ---- 5) MARCAR FILMES VISTOS PELO U ----
        HashSet<Integer> moviesU = userToMovies.getOrDefault(targetUser, new HashSet<>());
        boolean[] watchedByU = new boolean[nmovies];
        for (int idx : moviesU) watchedByU[idx] = true;

        // ---- 6) DETERMINAR UTILIZADORES COMPATÍVEIS V ----
        // U e V partilham pelo menos 1 filme
        HashSet<Integer> compatibleUsers = new HashSet<>();

        for (int m : moviesU) {
            for (int v : movieToUsers[m]) {
                if (v != targetUser)
                    compatibleUsers.add(v);
            }
        }

        // ---- 7) CANDIDATOS: filmes do género preferido que U NÃO viu
        ArrayList<MovieScore> results = new ArrayList<>();

        for (int idx = 0; idx < nmovies; idx++) {

            if (watchedByU[idx]) continue;

            if (!genresOfMovie[idx].contains(preferredGenre))
                continue;

            if (count[idx] == 0) continue; // nunca foi avaliado

            // verificar se existe V que viu este filme
            boolean ok = false;

            for (int v : movieToUsers[idx]) {
                if (compatibleUsers.contains(v)) {
                    ok = true;
                    break;
                }
            }

            if (!ok) continue;

            double avg = sum[idx] / count[idx];
            results.add(new MovieScore(titles[idx], avg));
        }

        // ---- 8) ORDENAR E IMPRIMIR ----
        Collections.sort(results);

        out.println(targetUser);

        int limit = Math.min(5, results.size());
        for (int i = 0; i < limit; i++) {
            out.println(results.get(i).title);
        }

        out.close();
    }
}

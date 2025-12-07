import java.io.*;
import java.util.*;

// CHAT GPT:
//          https://chatgpt.com/s/t_693599e3bb3081918bcce8891ffcd3bd

public class problemB_JAVA {

    static class MovieScore implements Comparable<MovieScore> {
        String title;
        double avg;

        MovieScore(String t, double a) {
            title = t;
            avg = a;
        }

        @Override
        public int compareTo(MovieScore o) {
            // média descrescente
            if (this.avg > o.avg) return -1;
            if (this.avg < o.avg) return 1;
            // depois título crescente
            return this.title.compareTo(o.title);
        }
    }

    public static void main(String[] args) throws Exception {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        // ---- 1) LER MOVIES ----
        int nmovies = Integer.parseInt(br.readLine());

        HashMap<Integer,Integer> idToIndex = new HashMap<>(nmovies * 2);
        String[] titles = new String[nmovies];

        for (int i = 0; i < nmovies; i++) {
            String line = br.readLine();
            String[] parts = line.split("\t");

            int movieId = Integer.parseInt(parts[0]);
            String movieTitle = parts[1];

            idToIndex.put(movieId, i);
            titles[i] = movieTitle;
        }

        // ---- 2) LER RATINGS ----
        int nratings = Integer.parseInt(br.readLine());

        // arrays compactos
        int[] allRaters = new int[nratings];
        int[] allMovieIdx = new int[nratings];

        double[] sum = new double[nmovies];
        int[] count = new int[nmovies];

        for (int i = 0; i < nratings; i++) {
            String line = br.readLine();
            String[] parts = line.split("\t");

            int rater = Integer.parseInt(parts[0]);
            int movieId = Integer.parseInt(parts[1]);
            double rating = Double.parseDouble(parts[2]);

            Integer idxObj = idToIndex.get(movieId);
            if (idxObj == null) continue;

            int idx = idxObj;

            allRaters[i] = rater;
            allMovieIdx[i] = idx;

            sum[idx] += rating;
            count[idx]++;
        }

        // ---- 3) LER UTILIZADOR U ----
        int targetUser = Integer.parseInt(br.readLine().trim());

        // ignorar linha género
        br.readLine();

        // ---- 4) MARCAR FILMES VISTOS PELO U ----
        boolean[] watchedByU = new boolean[nmovies];

        for (int i = 0; i < nratings; i++) {
            if (allRaters[i] == targetUser) {
                int idx = allMovieIdx[i];
                watchedByU[idx] = true;
            }
        }

        // ---- 5) CRIAR LISTA DE FILMES DE U COM MÉDIA ----
        ArrayList<MovieScore> list = new ArrayList<>();

        for (int idx = 0; idx < nmovies; idx++) {
            if (watchedByU[idx] && count[idx] > 0) {
                double avg = sum[idx] / count[idx];
                list.add(new MovieScore(titles[idx], avg));
            }
        }

        // ordenar: média decrescente , título crescente
        Collections.sort(list);

        // ---- 6) OUTPUT ----
        System.out.println(targetUser);

        int limit = Math.min(5, list.size());
        for (int i = 0; i < limit; i++) {
            System.out.println(list.get(i).title);
        }
    }
}

import java.io.*;   // inclui InputStream, InputStreamReader, BufferedReader, PrintWriter, IOException
import java.util.*; // inclui TreeMap, TreeSet, StringTokenizer, Map, Set

// CHAT GPT:
//          https://chatgpt.com/s/t_6943ec022858819190743bc3d5571dfd

public class problemB_JAVA {

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
    
    // classe para guardar um par (title, avg) e permitir ordenar. (Comparable<MovieScore> obriga a implementar compareTo.)
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
        InputReader in = new InputReader(System.in);
        PrintWriter out = new PrintWriter(System.out);

        // ---- 1) LER MOVIES ----
        int nmovies = in.nextInt();

        HashMap<Integer,Integer> idToIndex = new HashMap<>(nmovies * 2);
        String[] titles = new String[nmovies];
            //Porquê estas estruturas?

            // 1) HashMap<Integer,Integer> idToIndex
            //      Problema: movie_id não é garantido ser 1..nmovies (pode ter “buracos”).
            //      Solução AED: transformar chaves “esparsas” (IDs não densos) em índices densos 0..n-1.
            //  HashMap dá lookup/inserção O(1) médio.

            //  2) String[] titles
            //      Uma vez que tens um índice interno, guardas títulos num array: acesso direto O(1).
            //      Arrays são a es trutura mais rápida (memória contígua).

        for (int i = 0; i < nmovies; i++) {
            String line = in.nextLine();
            String[] parts = line.split("\t");

            int movieId = Integer.parseInt(parts[0]);
            String movieTitle = parts[1];

            idToIndex.put(movieId, i);
            titles[i] = movieTitle;
            //Guardas só o que precisas para T2:
            //  º titles para imprimir
            //  º idToIndex para mapear movie_id nos ratings para índice
            //  º Não guardas genres porque T2 não usa.
        }

        // ---- 2) LER RATINGS ----
        int nratings = in.nextInt();

        // arrays compactos
        int[] allRaters = new int[nratings];
        int[] allMovieIdx = new int[nratings];

        double[] sum = new double[nmovies];
        int[] count = new int[nmovies];

            //Porquê estas estruturas?
            //sum[] e count[] (arrays por filme); Queremos a média global por filme: avg[m] = sum[...]/countRatings[...]
            //Arrays permitem atualizar em O(1)
            //Isto permite, mais tarde, descobrir rapidamente “quais os filmes vistos por U” fazendo uma única passagem pelos ratings.

        for (int i = 0; i < nratings; i++) {
            String line = in.nextLine();
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
            //AED: porquê HashMap.get aqui?
            //Porque movieId do rating pode ser grande e esparso; não podes fazer sum[movieId] diretamente.
            //O mapeamento garante arrays densos 0..nmovies-1.

            //Cada iteração faz:
            // split de string (custo proporcional ao tamanho da linha)
            // HashMap lookup O(1) médio
            // atualizações de arrays O(1)
            //Em AED, o bloco é essencialmente O(nratings).
        }

        // ---- 3) LER UTILIZADOR U ----
        int targetUser = in.nextInt();

        // ignorar linha género
        in.nextLine();

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
        out.println(targetUser);

        int limit = Math.min(5, list.size());
        for (int i = 0; i < limit; i++) {
            out.println(list.get(i).title);
        }

        out.close();

        // Complexidade
            // Definições
            //  º N = nmovies
            //  º R = nratings
            //  º K= nº de filmes vistos por U (K ≤ M)

            //Tempo
            // º Ler filmes + mapear: O(M)
            // º Ler ratings + acumular sum/count + guardar arrays: O(R)
            // º Marcar vistos por U (varrer ratings): O(R)
            // º Construir lista: O(M)
            // º Ordenar lista: O(K log K)
            // º Imprimir top 5: O(1)
            //TOTAL: O(R)+O(R)+O(M)+O(M)+O(KlogK)≈O(R)(dominante)
    }
}

import java.io.*;   // inclui InputStream, InputStreamReader, BufferedReader, PrintWriter, IOException
import java.util.*; // inclui TreeMap, TreeSet, StringTokenizer, Map, Set
        //Porquê
        // Vais ler input grande → BufferedReader é muito mais rápido do que Scanner.
        // Vais precisar de estruturas (árvores/set/map) → vêm de java.util.

        // CHAT GPT:
        //      https://chatgpt.com/s/t_6943eb8f5b508191907b3307fd774642


public class problemA_JAVA {
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

    public static void main(String[] args) throws Exception {
        InputReader in = new InputReader(System.in);
        PrintWriter out = new PrintWriter(System.out);

        // ---- LEITURA nmovies ----
        int nmovies = in.nextInt();

        // genre -> sorted set of titles
        TreeMap<String, TreeSet<String>> genres = new TreeMap<>();
            // Estrutura principal: TreeMap<String, TreeSet<String>>

            // Isto é o coração do T1.
            // O que representa
            //  - genres é um mapa:
            //      º chave = String (nome do género)
            //      º valor = TreeSet<String> (conjunto ordenado de títulos desse género)
            //  - Ou seja:
            //      º género → (conjunto ordenado de títulos)

            // Porquê TreeMap?
            // O output exige: “Genres should be listed in ascending alphabetical order” 
            // TreeMap mantém as chaves ordenadas (ordem natural de String, ou seja, ordem lexicográfica).
            // AED: TreeMap é implementado tipicamente como árvore balanceada (Red-Black Tree).
            // Operações put/get/containsKey são O(log G) onde G é número de géneros distintos.
            // Porquê TreeSet nos títulos?
            // O output exige: “for each genre, the associated movies should also be sorted in ascending alphabetical order.” 
            // TreeSet mantém os títulos ordenados e evita duplicados.
            // AED: TreeSet também é árvore balanceada.
            // Inserção add é O(log M_g) onde M_g é número de filmes naquele género.

            // Porque não HashMap + ordenar no fim?
            // Poderias usar HashMap<String, ArrayList<String>> e no fim:
            //  - ordenar lista de géneros: O(G log G)
            //  - ordenar lista de filmes por género: soma O(M_g log M_g)
            // Isso também funcionava. A tua abordagem com TreeMap/TreeSet “paga” log em cada inserção, mas simplifica e garante ordenação sempre.
            
            // Defesa oral (frase pronta)
            // “Escolhi TreeMap e TreeSet porque o enunciado obriga a imprimir em ordem alfabética; usando estruturas baseadas em árvores balanceadas, a ordenação fica garantida estruturalmente sem um passo extra de sorting.”

        // ---- LER OS FILMES ----
        for (int i = 0; i < nmovies; i++) {
            String line = in.nextLine();
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
                genres.get(g).add(title);               // inserir filme ordenado (genres.get(g) devolve o TreeSet associado àquele género)
            }
                    //AED: porquê “set” e não “lista”?
                    // Um Set evita duplicados automaticamente. Mesmo que por alguma razão aparecesse repetido (ou input com inconsistências), o set não duplica.
                    // Como tens de imprimir ordenado, TreeSet dá-te:
                    //  - sem duplicados
                    //  - ordenado automaticamente
        }

        // ---- IGNORAR A PARTE DAS RATINGS ----

        // ---- IMPRIMIR RESULTADO ORDENADO ----
        for (String g : genres.keySet()) {
            out.println(g);
            for (String title : genres.get(g)) {
                out.println(title);
            }
        }
        out.close();


        // Complexidade
            // Definições
            //  º N = nmovies
            //  º G = nº géneros distintos
            //  º k = nº médio de géneros por filme (pequeno)
            //  º A = total de associações filme-género = Σ k (aprox N*k)

            //Tempo
            //  º Parse/splits: O(tamanho total do texto lido) ≈ O(input)
            //  º Inserções em árvores: para cada associação há log: O(A * (log G + log M_g)) [Na prática: O(A log N) como bound grosseiro, mas com constantes boas porque G e k são pequenos]

            //Frase curta de defesa
            //  º “Tempo dominado por inserir cada título no TreeSet do género: O(A log M) no total; memória O(A).”
}

}

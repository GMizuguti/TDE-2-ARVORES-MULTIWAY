public class Main {
    public static void main(String[] args) {
        BTree arvoreB = new BTree(3); // grau mínimo t = 3



        int[] valores = {20, 9, 10, 15, 16, 21, 5, 6, 12, 30, 7, 17, 3, 1, 2, 22, 27, 36, 77 ,54, 46, 99,47,48,49,50,51,53,5266,67,68,69};



        for (int valor : valores) {
            arvoreB.inserir(valor);
        }


        System.out.println("\nExibindo estrutura da Árvore B:");
        arvoreB.exibirArvore();

        System.out.println("\nremovendo chave 77");
        arvoreB.remover(77);

        System.out.println("\nExibindo estrutura da Árvore B após a remoção:");
        arvoreB.exibirArvore();

        int chaveBusca = 22;
        System.out.println("\n\nBuscando a chave " + chaveBusca + ":");
        BTreeNode resultado = arvoreB.buscar(chaveBusca);
        System.out.println(resultado != null ? "Chave encontrada!" : "Chave não encontrada.");

        chaveBusca = 77;
        System.out.println("\n\nBuscando a chave " + chaveBusca + ":");
        resultado = arvoreB.buscar(chaveBusca);
        System.out.println(resultado != null ? "Chave encontrada!" : "Chave não encontrada.");
    }
}

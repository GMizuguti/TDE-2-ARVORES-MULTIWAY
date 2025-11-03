public class BTree {
    BTreeNode raiz;
    int grauMinimo;

    public BTree(int grauMinimo) {
        this.raiz = null;
        this.grauMinimo = grauMinimo;
    }

    public void percorrer() {
        if (raiz != null) {
            raiz.percorrer();
        }
    }

    public BTreeNode buscar(int chave) {
        return (raiz == null) ? null : raiz.buscar(chave);
    }

    public void inserir(int chave) {
        if (raiz == null) {
            raiz = new BTreeNode(grauMinimo, true);
            raiz.chaves[0] = chave;
            raiz.totalChaves = 1;
        } else {
            if (raiz.totalChaves == 2 * grauMinimo - 1) {
                BTreeNode novaRaiz = new BTreeNode(grauMinimo, false);
                novaRaiz.filhos[0] = raiz;
                novaRaiz.dividirFilho(0, raiz);

                int i = (novaRaiz.chaves[0] < chave) ? 1 : 0;
                novaRaiz.filhos[i].inserirEmNoNaoCheio(chave);

                raiz = novaRaiz;
            } else {
                raiz.inserirEmNoNaoCheio(chave);
            }
        }
    }

    public void exibirArvore() {
        if (raiz != null) {
            raiz.exibirEstrutura(0);
        } else {
            System.out.println("Árvore está vazia.");
        }
    }


    public void remover(int chave) {
        if (raiz == null) {
            System.out.println("Árvore vazia.");
            return;
        }

        raiz.remover(chave);

        if (raiz.totalChaves == 0) {
            if (raiz.ehFolha) {
                raiz = null;
            } else {
                raiz = raiz.filhos[0];
            }
        }
    }
}

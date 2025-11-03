public class BTreeNode {
    int[] chaves;
    int grauMinimo;
    BTreeNode[] filhos;
    int totalChaves;
    boolean ehFolha;

    public BTreeNode(int grauMinimo, boolean ehFolha) {
        this.grauMinimo = grauMinimo;
        this.ehFolha = ehFolha;
        this.chaves = new int[2 * grauMinimo - 1];
        this.filhos = new BTreeNode[2 * grauMinimo];
        this.totalChaves = 0;
    }

    public void percorrer() {
        for (int i = 0; i < totalChaves; i++) {
            if (!ehFolha) {
                filhos[i].percorrer();
            }
            System.out.print(chaves[i] + " ");
        }
        if (!ehFolha) {
            filhos[totalChaves].percorrer();
        }
    }

    public BTreeNode buscar(int chave) {
        int i = 0;
        while (i < totalChaves && chave > chaves[i]) {
            i++;
        }

        if (i < totalChaves && chaves[i] == chave) {
            return this;
        }

        if (ehFolha) {
            return null;
        }

        return filhos[i].buscar(chave);
    }

    public void inserirEmNoNaoCheio(int chave) {
        int i = totalChaves - 1;

        if (ehFolha) {
            while (i >= 0 && chaves[i] > chave) {
                chaves[i + 1] = chaves[i];
                i--;
            }
            chaves[i + 1] = chave;
            totalChaves++;
        } else {
            while (i >= 0 && chaves[i] > chave) {
                i--;
            }

            if (filhos[i + 1].totalChaves == 2 * grauMinimo - 1) {
                dividirFilho(i + 1, filhos[i + 1]);

                if (chaves[i + 1] < chave) {
                    i++;
                }
            }

            filhos[i + 1].inserirEmNoNaoCheio(chave);
        }
    }

    public void dividirFilho(int indice, BTreeNode filhoCheio) {
        BTreeNode novoFilho = new BTreeNode(filhoCheio.grauMinimo, filhoCheio.ehFolha);
        novoFilho.totalChaves = grauMinimo - 1;

        for (int j = 0; j < grauMinimo - 1; j++) {
            novoFilho.chaves[j] = filhoCheio.chaves[j + grauMinimo];
        }

        if (!filhoCheio.ehFolha) {
            for (int j = 0; j < grauMinimo; j++) {
                novoFilho.filhos[j] = filhoCheio.filhos[j + grauMinimo];
            }
        }

        filhoCheio.totalChaves = grauMinimo - 1;

        for (int j = totalChaves; j >= indice + 1; j--) {
            filhos[j + 1] = filhos[j];
        }
        filhos[indice + 1] = novoFilho;

        for (int j = totalChaves - 1; j >= indice; j--) {
            chaves[j + 1] = chaves[j];
        }
        chaves[indice] = filhoCheio.chaves[grauMinimo - 1];
        totalChaves++;
    }


    public void exibirEstrutura(int nivel) {
        int i;

        // Se não for folha, exibe o filho antes de cada chave
        for (i = 0; i < totalChaves; i++) {
            if (!ehFolha && filhos[i] != null) {
                filhos[i].exibirEstrutura(nivel + 1);
            }

            // Exibe a chave atual com indentação
            for (int j = 0; j < nivel; j++) {
                System.out.print("    ");
            }
            System.out.println("Nível " + nivel + " - Chave: [" + chaves[i] + "]");
        }

        // Exibe o último filho
        if (!ehFolha && filhos[i] != null) {
            filhos[i].exibirEstrutura(nivel + 1);
        }
    }



// funções para remover chave e tratar os filhos



    public void remover(int chave) {
        int indice = encontrarIndice(chave);

        // Caso 1: chave está neste nó
        if (indice < totalChaves && chaves[indice] == chave) {
            if (ehFolha) {
                removerDeFolha(indice);
            } else {
                removerDeInterno(indice);
            }
        } else {
            // Caso 2: chave não está neste nó
            if (ehFolha) {
                System.out.println("Chave " + chave + " não encontrada.");
                return;
            }

            boolean ultimoFilho = (indice == totalChaves);
            if (filhos[indice].totalChaves < grauMinimo) {
                preencher(indice);
            }

            if (ultimoFilho && indice > totalChaves) {
                filhos[indice - 1].remover(chave);
            } else {
                filhos[indice].remover(chave);
            }
        }
    }

    private int encontrarIndice(int chave) {
        int i = 0;
        while (i < totalChaves && chaves[i] < chave) {
            i++;
        }
        return i;
    }

    private void removerDeFolha(int indice) {
        for (int i = indice + 1; i < totalChaves; i++) {
            chaves[i - 1] = chaves[i];
        }
        totalChaves--;
    }

    private void removerDeInterno(int indice) {
        int k = chaves[indice];

        if (filhos[indice].totalChaves >= grauMinimo) {
            int pred = obterPredecessor(indice);
            chaves[indice] = pred;
            filhos[indice].remover(pred);
        } else if (filhos[indice + 1].totalChaves >= grauMinimo) {
            int succ = obterSucessor(indice);
            chaves[indice] = succ;
            filhos[indice + 1].remover(succ);
        } else {
            fundir(indice);
            filhos[indice].remover(k);
        }
    }

    private int obterPredecessor(int indice) {
        BTreeNode atual = filhos[indice];
        while (!atual.ehFolha) {
            atual = atual.filhos[atual.totalChaves];
        }
        return atual.chaves[atual.totalChaves - 1];
    }

    private int obterSucessor(int indice) {
        BTreeNode atual = filhos[indice + 1];
        while (!atual.ehFolha) {
            atual = atual.filhos[0];
        }
        return atual.chaves[0];
    }

    private void preencher(int indice) {
        if (indice != 0 && filhos[indice - 1].totalChaves >= grauMinimo) {
            pegarDoAnterior(indice);
        } else if (indice != totalChaves && filhos[indice + 1].totalChaves >= grauMinimo) {
            pegarDoProximo(indice);
        } else {
            if (indice != totalChaves) {
                fundir(indice);
            } else {
                fundir(indice - 1);
            }
        }
    }

    private void pegarDoAnterior(int indice) {
        BTreeNode filho = filhos[indice];
        BTreeNode irmao = filhos[indice - 1];

        for (int i = filho.totalChaves - 1; i >= 0; i--) {
            filho.chaves[i + 1] = filho.chaves[i];
        }

        if (!filho.ehFolha) {
            for (int i = filho.totalChaves; i >= 0; i--) {
                filho.filhos[i + 1] = filho.filhos[i];
            }
        }

        filho.chaves[0] = chaves[indice - 1];

        if (!filho.ehFolha) {
            filho.filhos[0] = irmao.filhos[irmao.totalChaves];
        }

        chaves[indice - 1] = irmao.chaves[irmao.totalChaves - 1];

        filho.totalChaves++;
        irmao.totalChaves--;
    }

    private void pegarDoProximo(int indice) {
        BTreeNode filho = filhos[indice];
        BTreeNode irmao = filhos[indice + 1];

        filho.chaves[filho.totalChaves] = chaves[indice];

        if (!filho.ehFolha) {
            filho.filhos[filho.totalChaves + 1] = irmao.filhos[0];
        }

        chaves[indice] = irmao.chaves[0];

        for (int i = 1; i < irmao.totalChaves; i++) {
            irmao.chaves[i - 1] = irmao.chaves[i];
        }

        if (!irmao.ehFolha) {
            for (int i = 1; i <= irmao.totalChaves; i++) {
                irmao.filhos[i - 1] = irmao.filhos[i];
            }
        }

        filho.totalChaves++;
        irmao.totalChaves--;
    }

    private void fundir(int indice) {
        BTreeNode filho = filhos[indice];
        BTreeNode irmao = filhos[indice + 1];

        filho.chaves[grauMinimo - 1] = chaves[indice];

        for (int i = 0; i < irmao.totalChaves; i++) {
            filho.chaves[i + grauMinimo] = irmao.chaves[i];
        }

        if (!filho.ehFolha) {
            for (int i = 0; i <= irmao.totalChaves; i++) {
                filho.filhos[i + grauMinimo] = irmao.filhos[i];
            }
        }

        for (int i = indice + 1; i < totalChaves; i++) {
            chaves[i - 1] = chaves[i];
            filhos[i] = filhos[i + 1];
        }

        filho.totalChaves += irmao.totalChaves + 1;
        totalChaves--;
    }




}

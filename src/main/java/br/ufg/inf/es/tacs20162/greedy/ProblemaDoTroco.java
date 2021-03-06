package br.ufg.inf.es.tacs20162.greedy;

import java.text.DecimalFormat;
import java.util.HashMap;

/**
 *
 * @author gustavosotnas
 */
public class ProblemaDoTroco {

    /**
     * Estrutura de dados ("struct") que armazena as informações sobre o troco
     * a ser devolvido.
     */
    public static class Troco {

        /** Valor a ser devolvido de troco. */
        public double troco;

        /** A forma de distribuição de células que será entregue de troco:
         * ("valor das cédulas", "quantidade de cédulas daquele valor")*/
        public HashMap<Integer, Integer> notas = new HashMap<Integer, Integer>();

        /** A forma de distribuição de moedas que será entregue de troco:
         * ("valor das moedas", "quantidade de moedas daquele valor")*/
        public HashMap<Integer, Integer> moedas = new HashMap<Integer, Integer>();

        @Override
        public String toString() {

            StringBuilder output = new StringBuilder();
            DecimalFormat formatter = new DecimalFormat("###,##0.00");

            output.append("Troco: R$ " + formatter.format(this.troco) + "\n\n");

            // print HashMap de notas
            for (HashMap.Entry<Integer, Integer> nota : this.notas.entrySet()) {
                output.append(
                    (nota.getValue()+" notas de R$ "+formatter.format(nota.getKey())+"\n"));
            }

            // print HashMap de moedas
            for (HashMap.Entry<Integer, Integer> moeda : this.moedas.entrySet()) {
                output.append(
                    (moeda.getValue()+" moedas de R$ "+formatter.format((double) moeda.getKey()/100)+"\n"));
            }

            return output.toString();
        }
    }

    /**
     * Calcula o troco a ser dado para um cliente com a menor quantidade de
     * cédulas possível, dada uma quantidade infinita de notas e moedas de
     * determinados valores, passado pelo usuário.
     * 
     * Obs.: Algoritmo guloso, haverá casos em que a menor
     * quantidade dada pelo código não será a ideal (a solução ótima).
     * 
     * @param notasDisponiveis que valores de notas existem no caixa para
     * fazer troco
     * @param moedasDisponiveis que valores de moedas existem no caixa para
     * fazer troco
     * @param conta a quantia a ser paga pelo cliente
     * @param pago a quantia que o cliente pagou pela conta
     * @return a quantia a ser dada de troco, com informação de quantas notas e
     * moedas de cada valor devem ser dadas para o cliente receber seu troco
     * @throws IllegalArgumentException quando o cliente dá menos dinheiro que
     * deveria
     */
    public static Troco calculaTroco(int[] notasDisponiveis, int[] moedasDisponiveis,
            double conta, double pago) throws IllegalArgumentException {

        Troco resultado = new Troco();

        if (pago >= conta) {

            resultado.troco = pago - conta;

            resultado.notas = calculaNotas(notasDisponiveis, resultado.troco);
            resultado.moedas = calculaMoedas(moedasDisponiveis, resultado.troco);

            return resultado;

        } else {
            DecimalFormat formatador = new DecimalFormat("###,##0.00");
            throw new IllegalArgumentException("Pagamento insuficiente, faltam "
                    + "R$" + formatador.format(conta - pago) + "\n");
        }
    }

    /**
     * Implementação do "Problema do Troco" em Java.
     * O "Problema do Troco" é um algoritmo guloso que, dado um caixa que tem 
     * um conjunto de infinitas notas de determinados valores e um troco que
     * tem de ser dado a um cliente, o algoritmo sugere supostamente a melhor
     * forma de dar o troco para ele.
     * 
     * Obs.: Esta implementação calcula apenas a parte inteira do troco (cédulas).
     * Veja `calculaMoedas()`, que trata da parte decimal do troco (moedas / centavos).
     * 
     * @param notasDisponiveis que valores de notas existem no caixa para
     * fazer troco
     * @param troco a quantia que o caixa deve dar para o cliente como troco
     * @return quantas notas de cada valor devem ser dadas para o cliente 
     * receber seu troco
     */
    private static HashMap<Integer, Integer> calculaNotas (int[] notasDisponiveis, double troco) {

        int[] notas = sortReverse(notasDisponiveis);

        int valor;
        int qtdNotasNecessarias;

        int contadorNota = 0;

        HashMap<Integer, Integer> resultado = new HashMap<Integer, Integer>();

        valor = (int) troco;
        while (valor != 0) {
            qtdNotasNecessarias = valor / notas[contadorNota];
            if (qtdNotasNecessarias != 0) {
                resultado.put(notas[contadorNota], qtdNotasNecessarias); 
                valor = valor % notas[contadorNota]; // sobra
            }
            contadorNota++; // próxima nota
        }
        return resultado;
    }

    /**
     * Implementação do "Problema do Troco" em Java, adaptado para o cálculo do
     * troco em moedas, apenas.
     * 
     * Funciona de forma análoga ao `calculaNotas()`, com algumas diferenças.
     * 
     * @param moedasDisponiveis que valores de moedas existem no caixa para
     * fazer troco
     * @param troco a quantia que o caixa deve dar para o cliente como troco
     * @return quantas moedas de cada valor devem ser dadas para o cliente 
     * receber seu troco
     */
    private static HashMap<Integer, Integer> calculaMoedas (int[] moedasDisponiveis, double troco) {

        int[] moedas = sortReverse(moedasDisponiveis);

        int valor;
        int qtdMoedasNecessarias;

        int contadorMoeda = 0;

        HashMap<Integer, Integer> resultado = new HashMap<Integer, Integer>();

        valor = (int) Math.round((troco - (int) troco) * 100);
        while (valor != 0) {
            qtdMoedasNecessarias = valor / moedas[contadorMoeda];
            if (qtdMoedasNecessarias != 0) {
                resultado.put(moedas[contadorMoeda], qtdMoedasNecessarias);
                valor = valor % moedas[contadorMoeda]; // sobra
            }
            contadorMoeda++; // próxima moeda
        }
        return resultado;
    }

    /**
     * Ordena um vetor de números inteiros qualquer na ordem decrescente.
     * 
     * Código baseado em: http://stackoverflow.com/a/3523066
     * 
     * @param vector o vetor desordenado.
     * @return o vetor ordenado decrescentemente.
     */
    private static int[] sortReverse(int[] vector) {

        java.util.Arrays.sort(vector); // sort

        int left = 0;
        int right = vector.length - 1;

        while (left < right) {
            // swap the values at the left and right indices
            int temp = vector[left];
            vector[left] = vector[right];
            vector[right] = temp;

            // move the left and right index pointers in toward the center
            left++;
            right--;
        }
        return vector;
    }
}

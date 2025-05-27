package dev.tiago.tabelaFIP.principal;

import dev.tiago.tabelaFIP.model.Dados;
import dev.tiago.tabelaFIP.model.Modelos;
import dev.tiago.tabelaFIP.model.Veiculo;
import dev.tiago.tabelaFIP.service.ConsumoApi;
import dev.tiago.tabelaFIP.service.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private Scanner sc = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados converor = new ConverteDados();


    private final String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";

    public void exibeMenu(){
        var menu = """
                *** OPÇÕES ***
                Carro
                Moto
                Caminhão
                
                Digite uma das opções para consultar:
                """;

        System.out.println(menu);
        var opcao = sc.nextLine();
        String endereco;

        if (opcao.toLowerCase().contains("car")){
            endereco = URL_BASE + "carros/marcas";
        }else if (opcao.toLowerCase().contains("mot")){
            endereco = URL_BASE + "motos/marcas";
        }else {
            endereco = URL_BASE + "caminhoes/marcas";
        }

        var json = consumo.obterDados(endereco);
        System.out.println(json);
        var marcas = converor.obterLista(json, Dados.class);
        marcas.stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.println("Informe o codigo da marca: ");
        var consulta = sc.nextLine();

        endereco = endereco + "/" + consulta + "/modelos";
        json = consumo.obterDados(endereco);
        var modeloLista = converor.obterDados(json, Modelos.class);

        System.out.println("\nModelos da marca selecionada");
        modeloLista.modelos().stream().sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.println("\nDigite um trecho do nome do carro a ser buscado");
        var nomeVeiculo = sc.nextLine();

        List<Dados> modelosFiltrados = modeloLista
                .modelos()
                .stream()
                .filter(m -> m.nome().toLowerCase().contains(nomeVeiculo.toLowerCase()))
                .collect(Collectors.toList());

        System.out.println("\nModelos filtrados");
        modelosFiltrados.forEach(System.out::println);

        System.out.println("Digite o código do modelo");
        var codigoModelo = sc.nextLine();

        endereco = endereco + "/" + codigoModelo + "/anos";
        json = consumo.obterDados(endereco);
        List<Dados> anos = converor.obterLista(json, Dados.class);

        List<Veiculo> veiculos = new ArrayList<>();

        for (int i = 0; i < anos.size(); i++) {
            var enderecoAnos = endereco + "/" + anos.get(i).codigo();
            json = consumo.obterDados(enderecoAnos);
            Veiculo veiculo = converor.obterDados(json, Veiculo.class);
            veiculos.add(veiculo);
        }

        System.out.println("\nTodos os veículos filtrados por ano");
        veiculos.forEach(System.out::println);
    }
}

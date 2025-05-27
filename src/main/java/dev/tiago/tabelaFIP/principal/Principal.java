package dev.tiago.tabelaFIP.principal;

import dev.tiago.tabelaFIP.model.Dados;
import dev.tiago.tabelaFIP.model.Modelos;
import dev.tiago.tabelaFIP.service.ConsumoApi;
import dev.tiago.tabelaFIP.service.ConverteDados;

import java.util.Comparator;
import java.util.Scanner;

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


    }
}

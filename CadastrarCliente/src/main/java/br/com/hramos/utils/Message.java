package main.java.br.com.hramos.utils;

import java.text.MessageFormat;

public class Message {

    public static String getCodigo(String operationType) {
        String uniqueField = operationType.equals("Cliente") ? "cpf" : "codigo";
        return MessageFormat.format("Infome o {0} do {1}:", uniqueField, operationType);
    }

    public static String menuPrincipal(String operationType) {
        return MessageFormat.format("Digite a opção desejada:\n 1 - Cadastrar novo {0}\n 2 - Consultar {0}\n 3 - Alterar {0}\n 4 - excluir {0}\n 5 - Voltar para seleção \n 6 - Sair", operationType);
    }

    public static String newEntity(Integer operationType) {
        if(operationType == 1) {
            return "Cadastro de novo cliente deve seguir o seguinte padrao:\n nome, cpf, telefone, endereço, numero, cidade, estado";
        }
        return "Cadastro de novo produto deve seguir o seguinte padrao:\n nome, codigo";
    }

    public static String alterarEntity(Integer operationType) {
        if(operationType == 1) {
            return "Os novos dados devem seguir o seguinte padrao:\n nome, telefone, endereço, numero, cidade, estado";
        }
        return "Insira o novo nome do produto:";
    }
}

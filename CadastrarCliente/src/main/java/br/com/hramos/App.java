package main.java.br.com.hramos;

import main.java.br.com.hramos.dao.ClienteDAO;
import main.java.br.com.hramos.dao.ProdutoDAO;
import main.java.br.com.hramos.dao.generic.IGenericDAO;
import main.java.br.com.hramos.domain.Cliente;
import main.java.br.com.hramos.domain.Persistente;
import main.java.br.com.hramos.domain.Produto;
import main.java.br.com.hramos.exception.*;
import main.java.br.com.hramos.factory.Factory;
import main.java.br.com.hramos.factory.IFabricaPersistente;
import main.java.br.com.hramos.factory.IFactory;
import main.java.br.com.hramos.utils.Message;

import javax.swing.*;
import java.sql.SQLException;

public class App {
    private static Integer selection;

    private static IGenericDAO genericDAO;

    private static Integer operationType;

    public static void main(String[] args) {
        operationTypePanelLoop();

        genericDAO = operationType == 1 ? new ClienteDAO() : new ProdutoDAO();

        do {
            String message = Message.menuPrincipal(operationType == 1 ? "Cliente" : "Produto");
            String option = JOptionPane.showInputDialog(null, message, "Menu", JOptionPane.INFORMATION_MESSAGE);
            selection = Integer.parseInt(option);
            choiceMenu();
        } while (selection != 6);
    }

    private static void operationTypePanelLoop() {
        do {
            String option = JOptionPane.showInputDialog(null, "Qual o tipo de operação?\n 1 - Cliente\n 2 - Produto", "Type", JOptionPane.INFORMATION_MESSAGE);
            operationType = Integer.parseInt(option);
        } while (operationType != 1 && operationType != 2);
    }

    private static void choiceMenu() {
        try{
            switch (selection) {
                case 1:
                    cadastrarNovoEntity();
                    break;
                case 2:
                    buscarCliente();
                    break;
                case 3:
                    alterarCliente();
                    break;
                case 4:
                    excluirCliente();
                    break;
                case 5:
                    operationTypePanelLoop();
                    break;
                default:
                    break;
            }
        } catch (DadosInvalidosException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "ERRO DE VALIDACAO DE DADOS", JOptionPane.ERROR_MESSAGE);
        } catch (DAOException | TableException | MoreThanOneRegisterException | SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private static void excluirCliente() throws DAOException, TableException, MoreThanOneRegisterException, SQLException {
        Long codigo = getCodigo();
        Persistente entity = genericDAO.consultar(codigo);
        String clientName = (entity instanceof Cliente) ? ((Cliente) entity).getNome() : ((Produto) entity).getNome();

        if (entity == null) {
            JOptionPane.showMessageDialog(null,String.format("%s não encontrado", operationType == 1 ? "Cliente": "Produto"), "Error", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        genericDAO.excluir(codigo);
        JOptionPane.showMessageDialog(null, String.format("%s %s excluido com sucesso!", operationType == 1 ? "Cliente": "Produto", clientName), "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void alterarCliente() throws DadosInvalidosException, DAOException, TableException, MoreThanOneRegisterException, SQLException {
        Long codigo = getCodigo();
        Persistente entity = genericDAO.consultar(codigo);
        if (entity == null) {
            JOptionPane.showMessageDialog(null,String.format("Nenhum %s encontrado", operationType == 1 ? "cliente": "produto"), "Error", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        alterarClienteInputPanel(codigo);
        JOptionPane.showMessageDialog(null,String.format("%s alterado com sucesso", operationType == 1 ? "Cliente": "Produto"), "Sucesso", JOptionPane.INFORMATION_MESSAGE);

    }

    private static void alterarClienteInputPanel(Long codigo) throws DadosInvalidosException {
        String message = Message.alterarEntity(operationType);
        String option = JOptionPane.showInputDialog(null, message, "Alterar entity", JOptionPane.INFORMATION_MESSAGE);
        String[] campos = option.split(",");
        Persistente entity = createEntity(operationType == 1 ? new String[]{campos[0],String.valueOf(codigo), campos[1], campos[2], campos[3], campos[4], campos[5]} : new String[]{campos[0],String.valueOf(codigo)});
        try{
            genericDAO.alterar(entity);
        } catch (TipoChaveNaoEncontradaException e) {
            JOptionPane.showMessageDialog(null, "ERRO DE SISTEMA. CONTATE O ADMINISTRADOR DESTE SISTEMA", "Erro", JOptionPane.INFORMATION_MESSAGE);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }

    }

    private static void buscarCliente() throws DAOException, TableException, MoreThanOneRegisterException, SQLException {
        Long codigo = getCodigo();
        Persistente entity = genericDAO.consultar(codigo);
        if (entity != null) {
            JOptionPane.showMessageDialog(null, entity, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null,String.format("Nenhum %s encontrado", operationType == 1 ? "cliente": "produto"), "Error", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private static void cadastrarNovoEntity() throws DadosInvalidosException {
        String message = Message.newEntity(operationType);
        String option = JOptionPane.showInputDialog(null, message, "Nova Entity", JOptionPane.INFORMATION_MESSAGE);
        String[] campos = option.split(",");
        Persistente entity = createEntity(campos);
        try{
            Boolean isEntityCreate = genericDAO.cadastrar(entity);
            if (isEntityCreate) {
                JOptionPane.showMessageDialog(null, String.format("%s cadastrado com sucesso!", operationType == 1 ? "cliente": "produto"),"Sucesso!", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, String.format("%s já cadastrado", operationType == 1 ? "cliente": "produto"),"Error", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (DAOException e) {
            JOptionPane.showMessageDialog(null, "ERRO DE SISTEMA. CONTATE O ADMINISTRADOR DESTE SISTEMA", "Erro", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private static Persistente createEntity(String[] campos) throws DadosInvalidosException {
        IFactory factory = new Factory();
        IFabricaPersistente fabricaPersistente = factory.createFactory(operationType == 1 ? Cliente.class : Produto.class);
        return fabricaPersistente.createObjectLocal(campos);
    }

    private static Long getCodigo() {
        String message = Message.getCodigo(operationType == 1 ? "Cliente" : "Produto");
        String option = JOptionPane.showInputDialog(null, message, JOptionPane.INFORMATION_MESSAGE);
        return Long.parseLong(option);
    }
}

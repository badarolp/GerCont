package javaapplicationcadastrocliente;

import apresentacao.JFramePrincipal;
import controle.Cliente;
import controle.Movimentacao;
import java.sql.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author Lucas
 */
//public class JavaApplicationCadastroCliente {
public class Main {
    public static void main(String[] args) {   
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JFramePrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFramePrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFramePrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFramePrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }         
        JFramePrincipal jFramePrincipal = new JFramePrincipal();
        jFramePrincipal.setLocationRelativeTo(null);
        jFramePrincipal.setVisible(true);
        jFramePrincipal.setExtendedState(jFramePrincipal.getExtendedState() | JFramePrincipal.MAXIMIZED_BOTH);
        /*
        Cliente cliente = new Cliente(); // Cria o usuário
        cliente.setNome("Paulo"); // Configura o nome 
        cliente.setEndereco("Rua SP 221"); // Configura um endereço
        cliente.setCidade("São Paulo"); // Configura uma cidade
        cliente.setCpf("123456"); // Configura um CPF
        cliente.setEstado("SP"); // Configura um Estado
        if (cliente.armazenado()) { // Se armazenado o usuário no BD

            Movimentacao compra1 = new Movimentacao(); // Criar uma compra1
            compra1.setData(Date.valueOf("2014-01-01")); // Configura Data
            compra1.setDescricao("TV"); // Configura descrição da compra1
            compra1.setQtde(2);
            compra1.setPrecoUnitario(3500);
            compra1.setIdCliente(cliente); // Configura o cliente Paulo

            Movimentacao compra2 = new Movimentacao();
            compra2.setData(Date.valueOf("2014-01-01")); // Configura Data
            compra2.setDescricao("Fogão"); // Configura descrição da compra1
            compra2.setQtde(1);
            compra2.setPrecoUnitario(500);
            compra2.setIdCliente(cliente); // Configura o cliente Paulo


            if (!compra1.armazenado() || !compra2.armazenado()) { // Se não gravada compra1 ou compra2
                JOptionPane.showMessageDialog(null, "Falha em gravação de compra");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Falha em gravação de cliente");
        }
        */
    }    
}

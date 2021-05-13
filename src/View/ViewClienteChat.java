/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Infra.Caminhos;
import Infra.ClienteService;
import Model.ModelChat;
import Model.ModelChat.Action;
import java.awt.LayoutManager;
import java.awt.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.text.View;

/**
 *
 * @author charles
 */
public class ViewClienteChat extends javax.swing.JFrame {

    private Socket socket;
    private ModelChat modelChat;
    private ClienteService clienteService;
    private ModelChat message;
    private ClienteService service;
    public static ViewClienteChat chat;
    public static String usuario;
    public static String grupo;

    /**
     * Creates new form ViewClienteChat
     */
    public ViewClienteChat() {
        initComponents();
        setLocationRelativeTo(null);
        this.btConectar.setEnabled(true);
        this.btEnviar.setEnabled(false);
        this.btLimpar.setEnabled(false);
        this.txMensagemEnviar.setEnabled(false);
        carregaUsuario();
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        //this.btConectar();
    }

    private class ListenerSocket implements Runnable {

        private ObjectInputStream input;

        public ListenerSocket(Socket socket) {
            try {
                this.input = new ObjectInputStream(socket.getInputStream());
            } catch (IOException ex) {
                Logger.getLogger(ViewClienteChat.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        @Override
        public void run() {
            ModelChat message = null;
            try {

                while ((message = (ModelChat) input.readObject()) != null) {
                    Action action = message.getAction();
                    if (action.equals(Action.CONNECT)) {
                        connect(message);
                    } else if (action.equals(Action.DISCONNECT)) {
                        disconnect();
                        socket.close();
                    } else if (action.equals(Action.SEND_ONE)) {
                        receiveOne(message);
                    } else if (action.equals(Action.USERS_ONLINE)) {
                        refreshOnlines(message);
                    }

                }
            } catch (IOException ex) {

                Logger.getLogger(ViewClienteChat.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ViewClienteChat.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void carregaUsuario() {
        try {
            Properties arquivoConfiguracao = new Properties();
            arquivoConfiguracao.load(new FileInputStream(new File(Caminhos.ARQUIVO_DADOS_TOKEN)));
            usuario = arquivoConfiguracao.getProperty("usuario");
            grupo = arquivoConfiguracao.getProperty("grupo");
            txNome.setText(usuario);
            txGrupo.setText(grupo);

        } catch (Exception e) {
        }
    }

    private void connect(ModelChat message) {

        if (message.getTexto().equals("nao")) {
            //this.txNome.setText("");
            JOptionPane.showMessageDialog(this, "Conexão não realizada!\n tente novamente com um nome diferente");
            return;
        }
        this.message = message;
        JOptionPane.showMessageDialog(this, "Conectado com sucesso!");
    }

    private void receiveOne(ModelChat message) {

        this.txMensagemRecebida.append(message.getNome() + " diz: " + message.getTexto() + "\n");
        txMensagemRecebida.setCaretPosition(txMensagemRecebida.getText().length());
        this.txMensagemRecebida.requestFocus();
        //this.txMensagemEnviar.requestFocus();

    }

    private void disconnect() {

        JOptionPane.showMessageDialog(this, "Você saiu do chat");
        txMensagemEnviar.setText("");
        txMensagemRecebida.setText("");

    }

    private void refreshOnlines(ModelChat message) {

        Set<String> grupo = message.getSetGrupo();
        grupo.remove(message.getGrupo());
        String[] lista = (String[]) grupo.toArray(new String[grupo.size()]);

        this.jGrupo.setListData(lista);
        this.jGrupo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.jGrupo.setLayoutOrientation(JList.VERTICAL);

        Set<String> nomes = message.getSetOnlines();
        nomes.remove(message.getNome());
        String[] array = (String[]) nomes.toArray(new String[nomes.size()]);

        //this.jlOnline.setListData(lista);
        this.jlOnline.setListData(array);
        this.jlOnline.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.jlOnline.setLayoutOrientation(JList.VERTICAL);

    }

    private void btConectar() {
        String name = this.txNome.getText();
        String grupo = this.txGrupo.getText();
        if (!name.isEmpty()) {
            this.message = new ModelChat();
            this.message.setAction(Action.CONNECT);
            this.message.setNome(name);
            this.message.setGrupo(grupo);
            this.service = new ClienteService();
            this.socket = this.service.connect();

            new Thread(new ListenerSocket(this.socket)).start();

            this.service.send(message);
            setTitle(message.getNome());

            this.btConectar.setEnabled(false);
            this.btEnviar.setEnabled(true);
            this.btLimpar.setEnabled(true);
            this.txMensagemEnviar.setEnabled(true);

        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        txNome = new javax.swing.JTextField();
        btConectar = new javax.swing.JButton();
        txGrupo = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txMensagemEnviar = new javax.swing.JTextArea();
        btEnviar = new javax.swing.JButton();
        btLimpar = new javax.swing.JButton();
        chbTodos = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jlOnline = new javax.swing.JList<>();
        jScrollPane4 = new javax.swing.JScrollPane();
        jGrupo = new javax.swing.JList<>();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txMensagemRecebida = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jmPrincipal = new javax.swing.JMenu();
        jmFechar = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Conectar"));

        btConectar.setText("Conectar");
        btConectar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btConectarActionPerformed(evt);
            }
        });

        jLabel2.setText("Grupo");

        jLabel3.setText("Nome");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(0, 149, Short.MAX_VALUE))
                    .addComponent(txNome))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btConectar))
                    .addComponent(jLabel2))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btConectar, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txGrupo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txNome, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txMensagemEnviar.setColumns(20);
        txMensagemEnviar.setRows(5);
        jScrollPane3.setViewportView(txMensagemEnviar);

        btEnviar.setText("Enviar");
        btEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btEnviarActionPerformed(evt);
            }
        });

        btLimpar.setText("Limpar");
        btLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btLimparActionPerformed(evt);
            }
        });

        chbTodos.setText("Todos");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(chbTodos)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btLimpar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btEnviar))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 389, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btEnviar)
                    .addComponent(btLimpar)
                    .addComponent(chbTodos))
                .addGap(19, 19, 19))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jScrollPane1.setViewportView(jlOnline);

        jScrollPane4.setViewportView(jGrupo);

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/User group.png"))); // NOI18N
        jLabel4.setText("Grupo");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addGap(0, 130, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        txMensagemRecebida.setEditable(false);
        txMensagemRecebida.setColumns(20);
        txMensagemRecebida.setLineWrap(true);
        txMensagemRecebida.setRows(5);
        jScrollPane2.setViewportView(txMensagemRecebida);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/People.png"))); // NOI18N
        jLabel1.setText("Online");

        jmPrincipal.setText("Sobre");
        jmPrincipal.setFocusable(false);
        jmPrincipal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmPrincipalActionPerformed(evt);
            }
        });

        jmFechar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jmFechar.setText("Fechar");
        jmFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmFecharActionPerformed(evt);
            }
        });
        jmPrincipal.add(jmFechar);

        jMenuBar1.add(jmPrincipal);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btConectarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btConectarActionPerformed
        btConectar();
    }//GEN-LAST:event_btConectarActionPerformed

    private void btLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btLimparActionPerformed
        this.txMensagemEnviar.setText("");
    }//GEN-LAST:event_btLimparActionPerformed

    private void btEnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btEnviarActionPerformed
//        this.enviarMsg();

        if (!chbTodos.isSelected() && this.jlOnline.getSelectedIndex() == -1&& this.jGrupo.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "É necessário selecionar um usuário, grupo ou "
                    + "\n marcar a caixa 'Todos'");

        } else {
            this.enviarMsg();
            chbTodos.setSelected(false);

        }


    }//GEN-LAST:event_btEnviarActionPerformed

    private void jmPrincipalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmPrincipalActionPerformed

    }//GEN-LAST:event_jmPrincipalActionPerformed

    private void jmFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmFecharActionPerformed

        try {

            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ViewClienteChat.class.getName()).log(Level.SEVERE, null, ex);
        }
        dispose();
    }//GEN-LAST:event_jmFecharActionPerformed

    private void enviarMsg() {
        String texto = this.txMensagemEnviar.getText();
        String nome = message.getNome();
        this.message = new ModelChat();

        if (this.jlOnline.getSelectedIndex() > -1) {
            message.setNomeReservado(this.jlOnline.getSelectedValue());
            message.setAction(Action.SEND_ONE);
            this.jlOnline.clearSelection();

        } else if (this.jGrupo.getSelectedIndex() > -1) {
            message.setGrupoReservado(this.jGrupo.getSelectedValue());
            message.setAction(Action.SEND_GROUP);
            this.jGrupo.clearSelection();

        } else {
            message.setAction(Action.SEND_ALL);
        }

        if (!texto.isEmpty()) {
            this.message.setNome(nome);
            this.message.setTexto(texto);
            this.service.send(message);
            this.txMensagemRecebida.append("Você disse: " + texto + "\n");
        }

        this.txMensagemEnviar.setText("");

    }

    /**
     * método para avisar que recebeu uma nova mensagem Charles Müller
     */
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ViewClienteChat.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ViewClienteChat.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ViewClienteChat.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ViewClienteChat.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ViewClienteChat().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btConectar;
    private javax.swing.JButton btEnviar;
    private javax.swing.JButton btLimpar;
    private javax.swing.JCheckBox chbTodos;
    private javax.swing.JList<String> jGrupo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JList<String> jlOnline;
    private javax.swing.JMenuItem jmFechar;
    private javax.swing.JMenu jmPrincipal;
    private javax.swing.JTextField txGrupo;
    private javax.swing.JTextArea txMensagemEnviar;
    private javax.swing.JTextArea txMensagemRecebida;
    private javax.swing.JTextField txNome;
    // End of variables declaration//GEN-END:variables
}

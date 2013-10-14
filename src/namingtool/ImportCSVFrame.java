/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package namingtool;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Admin
 */
public class ImportCSVFrame extends javax.swing.JFrame {
    private DBTools database;
    /**
     * Creates new form manualDB
     */
    public ImportCSVFrame() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        resultTextArea = new javax.swing.JTextArea();
        jFileChooser1 = new javax.swing.JFileChooser();
        jProgressBar1 = new javax.swing.JProgressBar();

        setTitle("Manual DB tool");

        resultTextArea.setBackground(new java.awt.Color(215, 213, 196));
        resultTextArea.setColumns(20);
        resultTextArea.setRows(5);
        jScrollPane2.setViewportView(resultTextArea);

        jFileChooser1.setApproveButtonText("Import");
        jFileChooser1.setApproveButtonToolTipText("");
        jFileChooser1.setDialogTitle("Open .CSV file to import data");
        jFileChooser1.setSelectedFiles(null);
        jFileChooser1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFileChooser1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jFileChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jFileChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 397, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void doSmthng(){
        try {
            database = DBTools.getInstance();
            String query = "";
            String result = database.executeQuery(query);            
            logger(query.substring(0, query.indexOf(" ")) + "... : " + result);
        } finally {
            database.disconnect();
        }        
    }
    
    private int findNode(String node, int parent_id) throws SQLException{
        String query = "SELECT ID FROM MAIN WHERE NAME = '" + node + "' AND PARENT_ID = " + parent_id; // search
        ResultSet rs = database.sendQuery(query);    
        logger(query);
        if (null != rs && rs.next()){            
            return rs.getInt("ID");            
        } else {
            return 0;
        }
    }
    
    
       
    private void read(){ // TODO split this! It's work fine
        String csvFile = "G:/Dropbox/Naming Tool/Investigator_convert.csv"; //TODO
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";";
        database = DBTools.getInstance();
        String query = "";
        final int ROOT = 0;
        
        try {            
            br = new BufferedReader(new FileReader(csvFile));            
            int limit = 0;
            while ((line = br.readLine()) != null && limit++ < 10){
                String[] tree = line.split(cvsSplitBy);                      
                
                final int folderIndex = 1;
                String toc = tree[folderIndex].substring(0, tree[folderIndex].indexOf(" "));
                String name = tree[folderIndex].trim().substring(tree[folderIndex].indexOf(" ") + 1).replaceAll("'", "''"); // Folder name
                int parent_id = ROOT;
                int node_id = findNode(name, parent_id);
                
                final int topLevelIndex = 2;
                for (int i = topLevelIndex; i < tree.length - 1; i++){
                    int offset = (i - 2) * 3;
                    toc = tree[i].substring(offset, offset + 2);                    
                    name = tree[i].trim().substring(tree[i].indexOf(" ") + 1).replaceAll("'", "''");
                    
                    parent_id = node_id;
                    node_id = findNode(name, parent_id);
                    if (0 == node_id){                    
                        query = "INSERT INTO MAIN (NAME, TOC, PARENT_ID) VALUES ('" + name + "', '" + toc + "', " + parent_id + ")";
                        database.executeQuery(query);
                        node_id = findNode(name, parent_id);
                    }                    
                }    
                
                parent_id = node_id;
                toc = "file";
                name = tree[tree.length - 1].trim().replaceAll("'", "''");                
                String notes = tree[0].trim().replaceAll("'", "''");                                 
                query = "INSERT INTO MAIN (NAME, TOC, NOTES, PARENT_ID) VALUES ('" + name + "', '" + toc + "', '" + notes + "', " + parent_id + ")";
                database.executeQuery(query);                
            }            
        } catch (FileNotFoundException ex){
            logger(ex.toString());
        } catch (IOException ex){
            logger(ex.toString());            
        } catch (SQLException ex){
            logger(ex.toString());
        } finally {
            if (br != null){
                try {
                    br.close();                    
                } catch (IOException ex){
                    logger(ex.toString());
                }
            }
        }
    }
    
    private void jFileChooser1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFileChooser1ActionPerformed
        if ("ApproveSelection".equals(evt.getActionCommand())){
            logger(jFileChooser1.getSelectedFile().getName()); 
            read(); // put a file name here.
        } else if ("CancelSelection".equals(evt.getActionCommand())){
            this.setVisible(false);
        }
    }//GEN-LAST:event_jFileChooser1ActionPerformed

    public void logger(String str){
        resultTextArea.setText(resultTextArea.getText()+ str + "\n");
    }
    
    public void loggerLine(String str){
        resultTextArea.setText(resultTextArea.getText()+ str);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ImportCSVFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ImportCSVFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ImportCSVFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ImportCSVFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new ImportCSVFrame().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea resultTextArea;
    // End of variables declaration//GEN-END:variables
}
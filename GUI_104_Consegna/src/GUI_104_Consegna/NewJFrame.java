/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package GUI_104_Consegna;

import java.awt.Color;
import jssc.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author lenovo
 */
public class NewJFrame extends javax.swing.JFrame {

    /**
     * Creates new form NewJFrame
     */
    // ------ variabili per la gestione della RxPacchetto-------
        //variabili uso interno
        byte rxState=0;  
        byte rxConta=0;   
        int rxCheck;    
        byte[] rxBuff=new byte[256];
        //variabili di  interfaccia 
        byte[] rxInfo=new byte[256];
        byte rxPacc_OK=0;
        byte rxLenInfo;
        int Test2=0;//Flag per Test2
        byte[] dati_tx=new byte[256];
        int Test3=0,Test3_1=0;//Flag per Test3
        int temp=0;
        int Test4=0,Test4_1=0;//Flag per Test4
        int CountLista=0;
        // preleva dalla ComboBox il nome della porta
        Object porta;
        Object bitrate;
        String NomeCom;
        int speed;
    
    int statoMenu=0;  // gestione livello menu atttivo
    String[] com_list;
    SerialPort uart1;
    
    Timer Timer_01=new Timer();//creo l'oggetto di tipo Timer
    boolean PORTA_APERTA=false;
    
    public NewJFrame() {
       initComponents();
       statoMenu=1;
       selMenu();
       CercaCom();
    }
    
    
     class Timer_01_Task extends TimerTask{
        @Override
        public void run(){
            // inserire qui il codice da eseguire ad tick timer
            if(Test3==1&& Test3_1<5){
                dati_tx[0]=0x55;//invio solo questo dato
                TxPacchetto((byte)1, dati_tx);
                Test3_1++;
            }
            if(Test3_1==4){
                Test3_1=0;//azzero il flag qunado arrivo alla fine
            }
            if (Test4==1){
                dati_tx[0]=0x55;//invio solo questo dato
                TxPacchetto((byte)1, dati_tx);
            }
                 
        }
    }
    
     
    
    /**
    * 
    * metodo per ricerca porte COM 
    *
    */
    private void CercaCom(){
    int i;
    com_list=SerialPortList.getPortNames();
    for(i=0;i<com_list.length;i++) 
        jComboListaCom.addItem(com_list[i]);
        
    } 
    
   /**
    * 
    * Open porta COM
    *
    */
    private void OpenCom(){
        
        porta=jComboListaCom.getSelectedItem();
        NomeCom=porta.toString();
        
        bitrate=jComboBox2.getSelectedItem();
        CountLista=jComboBox2.getItemCount();//leggo se ci sono valori disponibili nella lista della seriale
        speed=Integer.parseInt(bitrate.toString());
        
        
        // istanziaoggetto uart1
        uart1=new SerialPort(NomeCom);
        try{
            uart1.openPort();
            uart1.setParams(speed,
                            SerialPort.DATABITS_8,
                            SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE
                            );
            
            
            
            
            uart1.addEventListener(new PortReader(), SerialPort.MASK_RXCHAR);
        }
        catch(SerialPortException ex) {System.out.println("Errore porta"+ex);
        }  
    }
    private void ClosePorta(){
        porta=null;
        speed=0;
    }
    
    /**
    * 
    * metodo da installare sull'evento di ricezione 
    *
    */
    private  class PortReader implements SerialPortEventListener {
        @Override
        public void serialEvent(SerialPortEvent event) {
            int  j;int x;int conta;
            byte inBuff[]=new byte[256];
            if(event.isRXCHAR() && event.getEventValue() > 0) {
                try {
                    conta=uart1.getInputBufferBytesCount();
                    inBuff=uart1.readBytes(conta);
                   
                    for(j=0;j<conta;j++){
                        x=inBuff[j];
                        x&=0x00FF;
                        jTextAreaRx.setText(jTextAreaRx.getText()+x+"\n");
                    }
                }
                catch (SerialPortException ex) {
                    System.out.println("Error rx from port: " + ex);
                }
            }
        }
    }
     
    
    /**
     * ------------------------------------------------------------------------
     * Metodo per la trasmissione di un pacchetto  secondo il protocollo:
     *  STX | Len | Info1 | ....| InfoN | Check | ETX
     *--------------------------------------------------------------------------
    */
     public  void TxPacchetto(byte len, byte info[]){
        int i;
        int check;
        int check_L;
        int check_H;
        check=0;
        // calcola il check del pacchetto
        for(i=0;i<len;i++){
            check=check+info[i];
        }
        
        check=check+len;
        check_L=check&0x00FF;//ricavo il check usando il bytr meno significatico della check calcolata
        check_H=(check>>8)&0x00FF;//non serve ai fini della trasmissione
        try{//costruisco il mio dato da trasmettere a partire dallo startTx fino ad endTx
            uart1.writeByte((byte)2);// STX
            uart1.writeByte((byte)2);// STX
            if (Test2==1){//-----------------vardar---------------------------
                uart1.writeByte((byte)(len-1));//se schiaccio il pulsante di Test2 simulo errore di len
                Test2=0;//abbasso il flag
            }
            else{
                uart1.writeByte((byte)len);//altrimenti fallo normale
            }
            
            
            for(i=0;i<len;i++)
                uart1.writeByte(info[i]);
            uart1.writeByte((byte)check_L);
            
        }
        catch (SerialPortException ex) {
        }
    }// end TxPacchetto
     
  
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel0 = new javax.swing.JPanel();
        jPan1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaDebugWindow = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaRx = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPan2 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jComboListaCom = new javax.swing.JComboBox<>();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel0.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel0.setDoubleBuffered(false);
        jPanel0.setLayout(new java.awt.CardLayout());

        jPan1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "setup seriale", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jLabel5.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("SPEED");
        jLabel5.setToolTipText("");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "9600", "19200", "57600" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jTextAreaDebugWindow.setColumns(20);
        jTextAreaDebugWindow.setRows(5);
        jScrollPane1.setViewportView(jTextAreaDebugWindow);

        jTextAreaRx.setColumns(20);
        jTextAreaRx.setRows(5);
        jScrollPane2.setViewportView(jTextAreaRx);

        jButton1.setText("Clear");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Clear");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("DEBUG WINDOW");

        jLabel4.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("RX");

        javax.swing.GroupLayout jPan1Layout = new javax.swing.GroupLayout(jPan1);
        jPan1.setLayout(jPan1Layout);
        jPan1Layout.setHorizontalGroup(
            jPan1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPan1Layout.createSequentialGroup()
                .addGroup(jPan1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPan1Layout.createSequentialGroup()
                        .addGap(88, 88, 88)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 90, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPan1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addGap(42, 42, 42)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(126, 126, 126))
            .addGroup(jPan1Layout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(175, 175, 175))
            .addGroup(jPan1Layout.createSequentialGroup()
                .addGap(138, 138, 138)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addGap(180, 180, 180))
        );
        jPan1Layout.setVerticalGroup(
            jPan1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPan1Layout.createSequentialGroup()
                .addGroup(jPan1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addGroup(jPan1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPan1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPan1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap(164, Short.MAX_VALUE))
        );

        jPanel0.add(jPan1, "card2");

        jPan2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "test seriale", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jButton3.setText("TX Byte");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton5.setText("TX FRAME");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPan2Layout = new javax.swing.GroupLayout(jPan2);
        jPan2.setLayout(jPan2Layout);
        jPan2Layout.setHorizontalGroup(
            jPan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPan2Layout.createSequentialGroup()
                .addGap(115, 115, 115)
                .addGroup(jPan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(408, Short.MAX_VALUE))
        );
        jPan2Layout.setVerticalGroup(
            jPan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPan2Layout.createSequentialGroup()
                .addContainerGap(394, Short.MAX_VALUE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33))
        );

        jPanel0.add(jPan2, "card3");
        jPan2.getAccessibleContext().setAccessibleName("TEST SERIALE");

        jLabel2.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("COM SEL");
        jLabel2.setToolTipText("");

        jComboListaCom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboListaComActionPerformed(evt);
            }
        });

        jButton6.setText("TEST1");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setText("TEST2");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setText("TEST3");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setText("TEST4");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton4.setText("OPEN COM");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                        .addComponent(jComboListaCom, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                            .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                            .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jPanel0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jComboListaCom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jButton4)
                        .addGap(20, 20, 20)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        
      try{
            uart1.writeByte((byte)130);// STX
        }
        catch (SerialPortException ex) {
        }       
        
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
         
         int flag=0;
         if(CountLista!=0 && flag==0){//solo se la seriale è davvero disponibile sul dispositivo in uso abilitami il flag della porta aperta
             OpenCom();
             jButton4.setText("CLOSE COM");
             PORTA_APERTA=true;
         }
         else{
             ClosePorta();
             jButton4.setText("OPEN COM");
         }
         
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        jTextAreaRx.setText("");
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        jTextAreaDebugWindow.setText("");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
    dati_tx[0]=48;dati_tx[1]=49;dati_tx[2]=50;dati_tx[3]=51;//definisco le variabili da trasferire inserendo anche la Len
    TxPacchetto((byte)4, dati_tx);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        //TEST1
        if(PORTA_APERTA==true){
            Test4=0;
            jTextAreaDebugWindow.setText("TX PACCHETTO");
            dati_tx[0]=1;dati_tx[1]=2;dati_tx[2]=3;dati_tx[3]=4;dati_tx[4]=5;dati_tx[5]=6;//definisco le variabili da trasferire inserendo anche la Len
            TxPacchetto((byte)6, dati_tx);
        }
        else if(PORTA_APERTA==false || CountLista==0){
            jTextAreaDebugWindow.setText("SERIALE NON DISPONIBILE");
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        //TEST2
        if(PORTA_APERTA==true){
            jTextAreaDebugWindow.setText("TX PACCHETTO ERRATO");
            Test4=0;
            dati_tx[0]=1;dati_tx[1]=2;dati_tx[2]=3;dati_tx[3]=4;dati_tx[4]=5;dati_tx[5]=6;//definisco le variabili da trasferire inserendo anche la Len
            Test2=1;
            TxPacchetto((byte)6, dati_tx);    
        }
        else if(PORTA_APERTA==false || CountLista==0){
            jTextAreaDebugWindow.setText("SERIALE NON DISPONIBILE");
    }
        
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        //TEST3
        if(PORTA_APERTA==true){
            Test4=0;
            jTextAreaDebugWindow.setText("TX PACCHETTI");
            temp=1000;
            Timer_01.schedule(new Timer_01_Task(), 10, temp);
        }
        else if(PORTA_APERTA==false || CountLista==0){
            jTextAreaDebugWindow.setText("SERIALE NON DISPONIBILE");
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
        //TEST4
        if(PORTA_APERTA==true){
            temp=100;
            jTextAreaDebugWindow.setText("TX CARATTERE RIPETUTO");
            Test4=1;
            if (Test4==1&&Test4_1==0){
                Timer_01.schedule(new Timer_01_Task(), 10, temp);
                Test4_1=1;
            }
            else if(Test3==1&&Test3_1==1){
                Test4=0;
                Test4_1=0;
            }
        }
        else if(PORTA_APERTA==false || CountLista==0){
            jTextAreaDebugWindow.setText("SERIALE NON DISPONIBILE");
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jComboListaComActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboListaComActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboListaComActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox2ActionPerformed

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
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NewJFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    javax.swing.JButton jButton1;
    javax.swing.JButton jButton2;
    javax.swing.JButton jButton3;
    javax.swing.JButton jButton4;
    javax.swing.JButton jButton5;
    javax.swing.JButton jButton6;
    javax.swing.JButton jButton7;
    javax.swing.JButton jButton8;
    javax.swing.JButton jButton9;
    javax.swing.JComboBox<String> jComboBox2;
    javax.swing.JComboBox<String> jComboListaCom;
    javax.swing.JLabel jLabel2;
    javax.swing.JLabel jLabel3;
    javax.swing.JLabel jLabel4;
    javax.swing.JLabel jLabel5;
    javax.swing.JPanel jPan1;
    javax.swing.JPanel jPan2;
    javax.swing.JPanel jPanel0;
    javax.swing.JScrollPane jScrollPane1;
    javax.swing.JScrollPane jScrollPane2;
    javax.swing.JTextArea jTextAreaDebugWindow;
    javax.swing.JTextArea jTextAreaRx;
    // End of variables declaration//GEN-END:variables
}

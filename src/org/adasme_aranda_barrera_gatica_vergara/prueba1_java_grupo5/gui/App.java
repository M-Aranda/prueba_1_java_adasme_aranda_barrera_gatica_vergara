package org.adasme_aranda_barrera_gatica_vergara.prueba1_java_grupo5.gui;

import clasesAusar.VistaLog;
import clasesAusar.VistaVivienda;
import com.jtattoo.plaf.luna.LunaLookAndFeel;
import java.awt.Color;
import java.awt.event.WindowEvent;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import model.Data;
import model.TModelLog;
import model.TModelVivienda;
import model.Usuario;
import model.Vivienda;

/**
 *
 * Autores: Lalo Adasme Marcelo Aranda Franco Barrera Marcelo Gatica Javier
 * Vergara
 */
public class App extends javax.swing.JFrame {

    //clase hilo creada dentro de app, para modificar componetes
    private class HiloDePrueba extends Thread {

        @Override
        public void run() {

        }

    }

    private Data d;
    private TModelVivienda modelVivienda;
    private TModelLog modelLog;
    private List<VistaVivienda> viviendasDisponibles;
    private List<VistaLog> logs;

    //query para los inserts
    static final String WRITE_OBJECT_SQL = "INSERT INTO ejem(nombre, valor_objeto) VALUES (?, ?)";// modificar segun sea necesario

    //query para los select
    static final String READ_OBJECT_SQL = "SELECT valor_objeto FROM ejem WHERE id = ?";//modificar segun sea necesario

    /**
     * Creates new form App
     */
    public App() {

        try {
            d = new Data();
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "ERROR " + ex.getMessage());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "ERROR: " + ex.getMessage());
        }

        initComponents();
        this.setTitle("Inicio de sesión");
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        viviendasDisponibles = new ArrayList();
        try {
            //  Data d= new Data();
            logs = new ArrayList<>();
            viviendasDisponibles = d.leerViviendasDisponibles();
        } catch (SQLException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }

        llenarCbosFrameVendedor();
        cargarTablaJFrameVendedor();

        ocultarOpcionesParaFiltrar();

    }

    private void cargarTablaJFrameVendedor() {

        modelVivienda = new TModelVivienda(viviendasDisponibles);
        tblDatosFrameVend.setModel(modelVivienda);
        tblDatosFrameVend.setGridColor(Color.DARK_GRAY);

    }

    private void llenarCbosFrameVendedor() {
        cboFiltrarPorCasas.removeAllItems();
        cboFiltrarPorDepartamentos.removeAllItems();

        ArrayList<String> estadosCasas = new ArrayList();
        ArrayList<String> estadosDepartamentos = new ArrayList();

        estadosCasas.add("Nuevas");
        estadosCasas.add("Usadas");
        estadosCasas.add("Ambas");

        estadosDepartamentos.add("Nuevos");
        estadosDepartamentos.add("Usados");
        estadosDepartamentos.add("Ambos");

        for (String e : estadosCasas) {
            cboFiltrarPorCasas.addItem(e);
        }

        for (String e : estadosDepartamentos) {
            cboFiltrarPorDepartamentos.addItem(e);
        }
    }

    private void ocultarOpcionesParaFiltrar() {
        cboFiltrarPorCasas.setEnabled(false);
        cboFiltrarPorDepartamentos.setEnabled(false);

        rbtFiltrarAsc.setVisible(false);
        rbtFiltrarDesc.setVisible(false);
        cboFiltrarPorCasas.setVisible(false);
        cboFiltrarPorDepartamentos.setVisible(false);
        chkFiltrarPorCasas.setVisible(false);
        chkFiltrarPorDepartamentos.setVisible(false);
        lblPrecio.setVisible(false);

    }

    private void mostrarOpcionesParaFiltrar() {
        rbtFiltrarAsc.setVisible(true);
        rbtFiltrarDesc.setVisible(true);
        cboFiltrarPorCasas.setVisible(true);
        cboFiltrarPorDepartamentos.setVisible(true);
        chkFiltrarPorCasas.setVisible(true);
        chkFiltrarPorDepartamentos.setVisible(true);
        lblPrecio.setVisible(true);
    }

    private void cargarTblLog() {
        modelLog = new TModelLog(logs);
        tblLog.setModel(modelLog);
    }

    //para escribir el objeto
    public static long writeJavaObject(Connection conn, Object object) throws Exception {
        String className = object.getClass().getName();
        PreparedStatement pstmt = conn.prepareStatement(WRITE_OBJECT_SQL, Statement.RETURN_GENERATED_KEYS);

        // fijar parametros de ingreso
        pstmt.setString(1, className);
        pstmt.setObject(2, object);
        pstmt.executeUpdate();

        // obtener la clave generada para el id
        ResultSet rs = pstmt.getGeneratedKeys();
        int id = -1;
        if (rs.next()) {
            id = rs.getInt(1);
        }

        rs.close();
        pstmt.close();

        return id;
    }

    //para leer el objeto
    public static Object readJavaObject(Connection conn, long id) throws Exception {
        PreparedStatement pstmt = conn.prepareStatement(READ_OBJECT_SQL);
        pstmt.setLong(1, id);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        Object object = rs.getObject(1);
        String className = object.getClass().getName();

        byte[] buf = rs.getBytes(1);
        ObjectInputStream objectIn = null;
        if (buf != null) {
            objectIn = new ObjectInputStream(new ByteArrayInputStream(buf));
        }

        Object deSerializedObject = objectIn.readObject();

        rs.close();
        pstmt.close();

        return deSerializedObject;
    }

    public void msgErrorRutNoEncontrado() {
        String titulo = "Error";
        String msg = "El rut ingresado no se encuentra registrado";
        int tipoMsg = JOptionPane.ERROR_MESSAGE;
        JOptionPane.showMessageDialog(null, msg, titulo, tipoMsg);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        JfVendedor = new javax.swing.JFrame();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDatosFrameVend = new javax.swing.JTable();
        rbtFiltrarAsc = new javax.swing.JRadioButton();
        rbtFiltrarDesc = new javax.swing.JRadioButton();
        cboFiltrarPorCasas = new javax.swing.JComboBox<>();
        cboFiltrarPorDepartamentos = new javax.swing.JComboBox<>();
        chkFiltrarPorCasas = new javax.swing.JCheckBox();
        chkFiltrarPorDepartamentos = new javax.swing.JCheckBox();
        lblPrecio = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMArchivo = new javax.swing.JMenu();
        jMCrearCli = new javax.swing.JMenuItem();
        jMVCamAp = new javax.swing.JMenuItem();
        jMVenderViv = new javax.swing.JMenuItem();
        jMArrendarVivienda = new javax.swing.JMenuItem();
        btnGFiltrarPrecio = new javax.swing.ButtonGroup();
        frmAdmin = new javax.swing.JFrame();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtRunVendedor = new javax.swing.JTextField();
        txtNombreVendedor = new javax.swing.JTextField();
        btnCrearVendedor = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtNRol = new javax.swing.JTextField();
        txtDireccion = new javax.swing.JTextField();
        cboTipo = new javax.swing.JComboBox<>();
        cboDisp = new javax.swing.JComboBox<>();
        cboCondicion = new javax.swing.JComboBox<>();
        spnBanios = new javax.swing.JSpinner();
        spnPiezas = new javax.swing.JSpinner();
        spnArriendo = new javax.swing.JSpinner();
        spnVenta = new javax.swing.JSpinner();
        btnCrearVivienda = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblLog = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        itemAdminSalir = new javax.swing.JMenuItem();
        txtRun = new javax.swing.JTextField();
        lblRUN = new javax.swing.JLabel();
        btnIniciarSesion = new javax.swing.JButton();
        lblicono = new javax.swing.JLabel();

        tblDatosFrameVend.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tblDatosFrameVend);

        btnGFiltrarPrecio.add(rbtFiltrarAsc);
        rbtFiltrarAsc.setText("Filtrar ascendente");

        btnGFiltrarPrecio.add(rbtFiltrarDesc);
        rbtFiltrarDesc.setText("Filtrar descendente");

        cboFiltrarPorCasas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cboFiltrarPorDepartamentos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        chkFiltrarPorCasas.setText("Filtrar por casas");
        chkFiltrarPorCasas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkFiltrarPorCasasActionPerformed(evt);
            }
        });

        chkFiltrarPorDepartamentos.setText("Filtrar por departamentos");
        chkFiltrarPorDepartamentos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkFiltrarPorDepartamentosActionPerformed(evt);
            }
        });

        lblPrecio.setText("Precio:");

        jMArchivo.setText("Archivo");

        jMCrearCli.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        jMCrearCli.setText("Crear cliente");
        jMCrearCli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMCrearCliActionPerformed(evt);
            }
        });
        jMArchivo.add(jMCrearCli);

        jMVCamAp.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.CTRL_MASK));
        jMVCamAp.setText("Cambiar apariencia");
        jMArchivo.add(jMVCamAp);

        jMVenderViv.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        jMVenderViv.setText("Vender vivienda");
        jMVenderViv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMVenderVivActionPerformed(evt);
            }
        });
        jMArchivo.add(jMVenderViv);

        jMArrendarVivienda.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        jMArrendarVivienda.setText("Arrendar vivienda");
        jMArrendarVivienda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMArrendarViviendaActionPerformed(evt);
            }
        });
        jMArchivo.add(jMArrendarVivienda);

        jMenuBar1.add(jMArchivo);

        JfVendedor.setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout JfVendedorLayout = new javax.swing.GroupLayout(JfVendedor.getContentPane());
        JfVendedor.getContentPane().setLayout(JfVendedorLayout);
        JfVendedorLayout.setHorizontalGroup(
            JfVendedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JfVendedorLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(JfVendedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JfVendedorLayout.createSequentialGroup()
                        .addGroup(JfVendedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chkFiltrarPorDepartamentos)
                            .addComponent(chkFiltrarPorCasas))
                        .addGap(18, 18, 18)
                        .addGroup(JfVendedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cboFiltrarPorCasas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboFiltrarPorDepartamentos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(66, 66, 66))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JfVendedorLayout.createSequentialGroup()
                        .addComponent(lblPrecio)
                        .addGap(18, 18, 18)
                        .addComponent(rbtFiltrarDesc)
                        .addGap(18, 18, 18)
                        .addComponent(rbtFiltrarAsc)
                        .addGap(31, 31, 31))))
            .addGroup(JfVendedorLayout.createSequentialGroup()
                .addGroup(JfVendedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 732, Short.MAX_VALUE))
                .addContainerGap())
        );
        JfVendedorLayout.setVerticalGroup(
            JfVendedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JfVendedorLayout.createSequentialGroup()
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addGroup(JfVendedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkFiltrarPorCasas)
                    .addComponent(cboFiltrarPorCasas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(JfVendedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkFiltrarPorDepartamentos)
                    .addComponent(cboFiltrarPorDepartamentos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(JfVendedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbtFiltrarDesc)
                    .addComponent(rbtFiltrarAsc)
                    .addComponent(lblPrecio))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 93, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLabel1.setText("RUN:");

        jLabel2.setText("Nombre:");

        btnCrearVendedor.setText("Crear");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 455, Short.MAX_VALUE)
                        .addComponent(btnCrearVendedor))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtRunVendedor)
                            .addComponent(txtNombreVendedor))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(110, 110, 110)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtRunVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtNombreVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCrearVendedor)
                .addContainerGap(173, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Crear Vendedor", jPanel1);

        jLabel3.setText("N° Rol:");

        jLabel4.setText("Dirección:");

        jLabel5.setText("Tipo:");

        jLabel6.setText("Disponibilidad:");

        jLabel7.setText("Baños:");

        jLabel8.setText("Piezas:");

        jLabel9.setText("Condición:");

        jLabel10.setText("Precio arriendo:");

        jLabel11.setText("Precio venta:");

        cboTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cboDisp.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cboCondicion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnCrearVivienda.setText("Crear");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNRol)
                            .addComponent(txtDireccion)
                            .addComponent(cboTipo, 0, 423, Short.MAX_VALUE)
                            .addComponent(cboDisp, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cboCondicion, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(spnBanios)
                            .addComponent(spnPiezas)
                            .addComponent(spnVenta)
                            .addComponent(spnArriendo)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnCrearVivienda)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(txtNRol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(cboTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(cboDisp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(cboCondicion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(spnBanios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spnPiezas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(spnArriendo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(spnVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCrearVivienda)
                .addContainerGap(67, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Crear Vivienda", jPanel2);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 544, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 366, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Ver Estadísticas", jPanel3);

        tblLog.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(tblLog);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 520, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 353, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Ver Log", jPanel4);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 544, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 366, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Restaurar", jPanel5);

        jMenu1.setText("Archivo");

        itemAdminSalir.setText("Salir");
        itemAdminSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemAdminSalirActionPerformed(evt);
            }
        });
        jMenu1.add(itemAdminSalir);

        jMenuBar2.add(jMenu1);

        frmAdmin.setJMenuBar(jMenuBar2);

        javax.swing.GroupLayout frmAdminLayout = new javax.swing.GroupLayout(frmAdmin.getContentPane());
        frmAdmin.getContentPane().setLayout(frmAdminLayout);
        frmAdminLayout.setHorizontalGroup(
            frmAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(frmAdminLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        frmAdminLayout.setVerticalGroup(
            frmAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(frmAdminLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblRUN.setText("R.U.N");

        btnIniciarSesion.setText("Iniciar sesión");
        btnIniciarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIniciarSesionActionPerformed(evt);
            }
        });

        lblicono.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lookAndFeels/logoCafé.png"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblicono, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblRUN)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnIniciarSesion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtRun, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtRun, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblRUN))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnIniciarSesion))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblicono, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMCrearCliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMCrearCliActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMCrearCliActionPerformed

    private void jMVenderVivActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMVenderVivActionPerformed
        if (chkFiltrarPorCasas.isVisible()) {
            ocultarOpcionesParaFiltrar();
        } else if (!chkFiltrarPorCasas.isVisible()) {
            mostrarOpcionesParaFiltrar();
        }


    }//GEN-LAST:event_jMVenderVivActionPerformed

    private void jMArrendarViviendaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMArrendarViviendaActionPerformed
        if (chkFiltrarPorCasas.isVisible()) {
            ocultarOpcionesParaFiltrar();
        } else if (!chkFiltrarPorCasas.isVisible()) {
            mostrarOpcionesParaFiltrar();
        }

    }//GEN-LAST:event_jMArrendarViviendaActionPerformed

    private void chkFiltrarPorCasasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkFiltrarPorCasasActionPerformed

        if (chkFiltrarPorCasas.isSelected()) {
            cboFiltrarPorCasas.setEnabled(true);
        } else if (!chkFiltrarPorCasas.isSelected()) {
            cboFiltrarPorCasas.setEnabled(false);
        }

    }//GEN-LAST:event_chkFiltrarPorCasasActionPerformed

    private void chkFiltrarPorDepartamentosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkFiltrarPorDepartamentosActionPerformed

        if (chkFiltrarPorDepartamentos.isSelected()) {
            cboFiltrarPorDepartamentos.setEnabled(true);
        } else if (!chkFiltrarPorDepartamentos.isSelected()) {
            cboFiltrarPorDepartamentos.setEnabled(false);
        }


    }//GEN-LAST:event_chkFiltrarPorDepartamentosActionPerformed

    private void itemAdminSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemAdminSalirActionPerformed
        frmAdmin.dispatchEvent(new WindowEvent(frmAdmin, WindowEvent.WINDOW_CLOSING));
    }//GEN-LAST:event_itemAdminSalirActionPerformed

    private void btnIniciarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIniciarSesionActionPerformed
        String run = txtRun.getText();

        try {
            Usuario u = d.buscarUsuario(run);
            if (u != null) {
                if (u.getTipo() == 1) {
                    this.dispose();
                    frmAdmin.setBounds(WIDTH, WIDTH, 389, 256);
                    frmAdmin.setLocationRelativeTo(null);
                    frmAdmin.setVisible(true);
                    JOptionPane.showMessageDialog(null, "          Bienvenido \n Administrador: " + u.getNombre());

                }
                if (u.getTipo() == 2) {
                    this.dispose();
                    JfVendedor.setBounds(WIDTH, WIDTH, 1000, 400);
                    JfVendedor.setLocationRelativeTo(null);
                    JfVendedor.setVisible(true);
                    JOptionPane.showMessageDialog(null, "          Bienvenido \n Vendedor: " + u.getNombre());
                }
            } else {
                JOptionPane.showMessageDialog(null, "Usuario no encontrado");
                txtRun.setText("");
                txtRun.requestFocus();
            }

        } catch (SQLException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }

//        usuarios = new ArrayList<>();
//        try {
//            usuarios = d.leerUsuario();
//        } catch (SQLException ex) {
//            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        try {
//            if (d.existeUsuario(run) == 1) {
//                for (Usuario u : usuarios) {
//
//                    if (run.equals(u.getRun())) {
//                        if (u.getTipo() == 1) {
//                            this.dispose();
//                            frmAdmin.setBounds(WIDTH, WIDTH, 389, 256);
//                            frmAdmin.setLocationRelativeTo(null);
//                            frmAdmin.setVisible(true);
//                            JOptionPane.showMessageDialog(null, "          Bienvenido \n Administrador: " + u.getNombre());
//
//                        }
//                        if (u.getTipo() == 2) {
//                            this.dispose();
//                            JfVendedor.setBounds(WIDTH, WIDTH, 1000, 400);
//                            JfVendedor.setLocationRelativeTo(null);
//                            JfVendedor.setVisible(true);
//                            JOptionPane.showMessageDialog(null, "          Bienvenido \n Vendedor: " + u.getNombre());
//                        }
//                    }
//
//                }
//            }else{
//                JOptionPane.showMessageDialog(null, "Usuario no encontrado");
//                txtRun.setText("");
//                txtRun.requestFocus();
//            }
//            
//        } catch (SQLException ex) {
//            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
//        }

    }//GEN-LAST:event_btnIniciarSesionActionPerformed

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
            UIManager.setLookAndFeel(new LunaLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }

//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(App.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(App.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(App.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(App.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new App().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFrame JfVendedor;
    private javax.swing.JButton btnCrearVendedor;
    private javax.swing.JButton btnCrearVivienda;
    private javax.swing.ButtonGroup btnGFiltrarPrecio;
    private javax.swing.JButton btnIniciarSesion;
    private javax.swing.JComboBox<String> cboCondicion;
    private javax.swing.JComboBox<String> cboDisp;
    private javax.swing.JComboBox<String> cboFiltrarPorCasas;
    private javax.swing.JComboBox<String> cboFiltrarPorDepartamentos;
    private javax.swing.JComboBox<String> cboTipo;
    private javax.swing.JCheckBox chkFiltrarPorCasas;
    private javax.swing.JCheckBox chkFiltrarPorDepartamentos;
    private javax.swing.JFrame frmAdmin;
    private javax.swing.JMenuItem itemAdminSalir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMArchivo;
    private javax.swing.JMenuItem jMArrendarVivienda;
    private javax.swing.JMenuItem jMCrearCli;
    private javax.swing.JMenuItem jMVCamAp;
    private javax.swing.JMenuItem jMVenderViv;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblPrecio;
    private javax.swing.JLabel lblRUN;
    private javax.swing.JLabel lblicono;
    private javax.swing.JRadioButton rbtFiltrarAsc;
    private javax.swing.JRadioButton rbtFiltrarDesc;
    private javax.swing.JSpinner spnArriendo;
    private javax.swing.JSpinner spnBanios;
    private javax.swing.JSpinner spnPiezas;
    private javax.swing.JSpinner spnVenta;
    private javax.swing.JTable tblDatosFrameVend;
    private javax.swing.JTable tblLog;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtNRol;
    private javax.swing.JTextField txtNombreVendedor;
    private javax.swing.JTextField txtRun;
    private javax.swing.JTextField txtRunVendedor;
    // End of variables declaration//GEN-END:variables
}

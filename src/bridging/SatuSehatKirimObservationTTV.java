/*
  By Mas Elkhanza
 */

package bridging;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fungsi.WarnaTable;
import fungsi.batasInput;
import fungsi.koneksiDB;
import java.awt.Dimension;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import fungsi.sekuel;
import fungsi.validasi;
import fungsi.akses;
import java.awt.Cursor;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

/**
 *
 * @author dosen
 */
public final class SatuSehatKirimObservationTTV extends javax.swing.JDialog {
    private final DefaultTableModel tabModeSuhu,tabModeRespirasi,tabModeNadi,tabModeSpO2,tabModeGCS;
    private sekuel Sequel=new sekuel();
    private validasi Valid=new validasi();
    private Connection koneksi=koneksiDB.condb();
    private PreparedStatement ps;
    private ResultSet rs;   
    private int i=0;
    private String link="",json="",iddokter="",idpasien="";
    private ApiSatuSehat api=new ApiSatuSehat();
    private HttpHeaders headers ;
    private HttpEntity requestEntity;
    private ObjectMapper mapper = new ObjectMapper();
    private JsonNode root;
    private JsonNode response;
    private SatuSehatCekNIK cekViaSatuSehat=new SatuSehatCekNIK();    
    
    /** Creates new form DlgKamar
     * @param parent
     * @param modal */
    public SatuSehatKirimObservationTTV(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        this.setLocation(10,2);
        setSize(628,674);

        tabModeSuhu=new DefaultTableModel(null,new String[]{
                "P","Tanggal Registrasi","No.Rawat","No.RM","Nama Pasien","No.KTP Pasien","Stts Rawat","Stts Lanjut",
                "Tanggal Pulang","ID Encounter","Suhu (°C)","Petugas/Dokter/Praktisi","No.KTP Praktisi","Tanggal","Jam",
                "ID Observation Suhu"
            }){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = false;
                if (colIndex==0) {
                    a=true;
                }
                return a;
             }
             Class[] types = new Class[] {
                 java.lang.Boolean.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, 
                 java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, 
                 java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, 
                 java.lang.String.class
             };
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        tbSuhu.setModel(tabModeSuhu);

        //tbKamar.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbKamar.getBackground()));
        tbSuhu.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbSuhu.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 16; i++) {
            TableColumn column = tbSuhu.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(20);
            }else if(i==1){
                column.setPreferredWidth(110);
            }else if(i==2){
                column.setPreferredWidth(105);
            }else if(i==3){
                column.setPreferredWidth(70);
            }else if(i==4){
                column.setPreferredWidth(150);
            }else if(i==5){
                column.setPreferredWidth(110);
            }else if(i==6){
                column.setPreferredWidth(63);
            }else if(i==7){
                column.setPreferredWidth(63);
            }else if(i==8){
                column.setPreferredWidth(110);
            }else if(i==9){
                column.setPreferredWidth(215);
            }else if(i==10){
                column.setPreferredWidth(60);
            }else if(i==11){
                column.setPreferredWidth(150);
            }else if(i==12){
                column.setPreferredWidth(110);
            }else if(i==13){
                column.setPreferredWidth(65);
            }else if(i==14){
                column.setPreferredWidth(55);
            }else if(i==15){
                column.setPreferredWidth(215);
            }
        }
        tbSuhu.setDefaultRenderer(Object.class, new WarnaTable());
        
        tabModeRespirasi=new DefaultTableModel(null,new String[]{
                "P","Tanggal Registrasi","No.Rawat","No.RM","Nama Pasien","No.KTP Pasien","Stts Rawat","Stts Lanjut",
                "Tanggal Pulang","ID Encounter","Resp(/menit)","Petugas/Dokter/Praktisi","No.KTP Praktisi","Tanggal","Jam",
                "ID Observation Respirasi"
            }){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = false;
                if (colIndex==0) {
                    a=true;
                }
                return a;
             }
             Class[] types = new Class[] {
                 java.lang.Boolean.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, 
                 java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, 
                 java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, 
                 java.lang.String.class
             };
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        tbRespirasi.setModel(tabModeRespirasi);

        //tbKamar.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbKamar.getBackground()));
        tbRespirasi.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbRespirasi.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 16; i++) {
            TableColumn column = tbRespirasi.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(20);
            }else if(i==1){
                column.setPreferredWidth(110);
            }else if(i==2){
                column.setPreferredWidth(105);
            }else if(i==3){
                column.setPreferredWidth(70);
            }else if(i==4){
                column.setPreferredWidth(150);
            }else if(i==5){
                column.setPreferredWidth(110);
            }else if(i==6){
                column.setPreferredWidth(63);
            }else if(i==7){
                column.setPreferredWidth(63);
            }else if(i==8){
                column.setPreferredWidth(110);
            }else if(i==9){
                column.setPreferredWidth(215);
            }else if(i==10){
                column.setPreferredWidth(70);
            }else if(i==11){
                column.setPreferredWidth(150);
            }else if(i==12){
                column.setPreferredWidth(110);
            }else if(i==13){
                column.setPreferredWidth(65);
            }else if(i==14){
                column.setPreferredWidth(55);
            }else if(i==15){
                column.setPreferredWidth(215);
            }
        }
        tbRespirasi.setDefaultRenderer(Object.class, new WarnaTable());
        
        tabModeNadi=new DefaultTableModel(null,new String[]{
                "P","Tanggal Registrasi","No.Rawat","No.RM","Nama Pasien","No.KTP Pasien","Stts Rawat","Stts Lanjut",
                "Tanggal Pulang","ID Encounter","Nadi(/menit)","Petugas/Dokter/Praktisi","No.KTP Praktisi","Tanggal","Jam",
                "ID Observation Nadi"
            }){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = false;
                if (colIndex==0) {
                    a=true;
                }
                return a;
             }
             Class[] types = new Class[] {
                 java.lang.Boolean.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, 
                 java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, 
                 java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, 
                 java.lang.String.class
             };
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        tbNadi.setModel(tabModeNadi);

        //tbKamar.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbKamar.getBackground()));
        tbNadi.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbNadi.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 16; i++) {
            TableColumn column = tbNadi.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(20);
            }else if(i==1){
                column.setPreferredWidth(110);
            }else if(i==2){
                column.setPreferredWidth(105);
            }else if(i==3){
                column.setPreferredWidth(70);
            }else if(i==4){
                column.setPreferredWidth(150);
            }else if(i==5){
                column.setPreferredWidth(110);
            }else if(i==6){
                column.setPreferredWidth(63);
            }else if(i==7){
                column.setPreferredWidth(63);
            }else if(i==8){
                column.setPreferredWidth(110);
            }else if(i==9){
                column.setPreferredWidth(215);
            }else if(i==10){
                column.setPreferredWidth(70);
            }else if(i==11){
                column.setPreferredWidth(150);
            }else if(i==12){
                column.setPreferredWidth(110);
            }else if(i==13){
                column.setPreferredWidth(65);
            }else if(i==14){
                column.setPreferredWidth(55);
            }else if(i==15){
                column.setPreferredWidth(215);
            }
        }
        tbNadi.setDefaultRenderer(Object.class, new WarnaTable());
        
        tabModeSpO2=new DefaultTableModel(null,new String[]{
                "P","Tanggal Registrasi","No.Rawat","No.RM","Nama Pasien","No.KTP Pasien","Stts Rawat","Stts Lanjut",
                "Tanggal Pulang","ID Encounter","SpO2(%)","Petugas/Dokter/Praktisi","No.KTP Praktisi","Tanggal","Jam",
                "ID Observation SpO2"
            }){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = false;
                if (colIndex==0) {
                    a=true;
                }
                return a;
             }
             Class[] types = new Class[] {
                 java.lang.Boolean.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, 
                 java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, 
                 java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, 
                 java.lang.String.class
             };
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        tbSpO2.setModel(tabModeSpO2);

        //tbKamar.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbKamar.getBackground()));
        tbSpO2.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbSpO2.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 16; i++) {
            TableColumn column = tbSpO2.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(20);
            }else if(i==1){
                column.setPreferredWidth(110);
            }else if(i==2){
                column.setPreferredWidth(105);
            }else if(i==3){
                column.setPreferredWidth(70);
            }else if(i==4){
                column.setPreferredWidth(150);
            }else if(i==5){
                column.setPreferredWidth(110);
            }else if(i==6){
                column.setPreferredWidth(63);
            }else if(i==7){
                column.setPreferredWidth(63);
            }else if(i==8){
                column.setPreferredWidth(110);
            }else if(i==9){
                column.setPreferredWidth(215);
            }else if(i==10){
                column.setPreferredWidth(55);
            }else if(i==11){
                column.setPreferredWidth(150);
            }else if(i==12){
                column.setPreferredWidth(110);
            }else if(i==13){
                column.setPreferredWidth(65);
            }else if(i==14){
                column.setPreferredWidth(55);
            }else if(i==15){
                column.setPreferredWidth(215);
            }
        }
        tbSpO2.setDefaultRenderer(Object.class, new WarnaTable());
        
        tabModeGCS=new DefaultTableModel(null,new String[]{
                "P","Tanggal Registrasi","No.Rawat","No.RM","Nama Pasien","No.KTP Pasien","Stts Rawat","Stts Lanjut",
                "Tanggal Pulang","ID Encounter","GCS","Petugas/Dokter/Praktisi","No.KTP Praktisi","Tanggal","Jam",
                "ID Observation GCS"
            }){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = false;
                if (colIndex==0) {
                    a=true;
                }
                return a;
             }
             Class[] types = new Class[] {
                 java.lang.Boolean.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, 
                 java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, 
                 java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, 
                 java.lang.String.class
             };
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        tbGCS.setModel(tabModeGCS);

        //tbKamar.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbKamar.getBackground()));
        tbGCS.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbGCS.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 16; i++) {
            TableColumn column = tbGCS.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(20);
            }else if(i==1){
                column.setPreferredWidth(110);
            }else if(i==2){
                column.setPreferredWidth(105);
            }else if(i==3){
                column.setPreferredWidth(70);
            }else if(i==4){
                column.setPreferredWidth(150);
            }else if(i==5){
                column.setPreferredWidth(110);
            }else if(i==6){
                column.setPreferredWidth(63);
            }else if(i==7){
                column.setPreferredWidth(63);
            }else if(i==8){
                column.setPreferredWidth(110);
            }else if(i==9){
                column.setPreferredWidth(215);
            }else if(i==10){
                column.setPreferredWidth(40);
            }else if(i==11){
                column.setPreferredWidth(150);
            }else if(i==12){
                column.setPreferredWidth(110);
            }else if(i==13){
                column.setPreferredWidth(65);
            }else if(i==14){
                column.setPreferredWidth(55);
            }else if(i==15){
                column.setPreferredWidth(215);
            }
        }
        tbGCS.setDefaultRenderer(Object.class, new WarnaTable());
        
        TCari.setDocument(new batasInput((byte)100).getKata(TCari));
        
        if(koneksiDB.CARICEPAT().equals("aktif")){
            TCari.getDocument().addDocumentListener(new javax.swing.event.DocumentListener(){
                @Override
                public void insertUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        tampilsuhu();
                    }
                }
                @Override
                public void removeUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        tampilsuhu();
                    }
                }
                @Override
                public void changedUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        tampilsuhu();
                    }
                }
            });
        } 
        
        try {
            link=koneksiDB.URLFHIRSATUSEHAT();
        } catch (Exception e) {
            System.out.println("Notif : "+e);
        }     
    }
    
    

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        ppPilihSemua = new javax.swing.JMenuItem();
        ppBersihkan = new javax.swing.JMenuItem();
        internalFrame1 = new widget.InternalFrame();
        jPanel3 = new javax.swing.JPanel();
        panelGlass8 = new widget.panelisi();
        jLabel7 = new widget.Label();
        LCount = new widget.Label();
        BtnAll1 = new widget.Button();
        BtnKirim = new widget.Button();
        BtnUpdate = new widget.Button();
        BtnPrint = new widget.Button();
        BtnKeluar = new widget.Button();
        panelGlass9 = new widget.panelisi();
        jLabel15 = new widget.Label();
        DTPCari1 = new widget.Tanggal();
        jLabel17 = new widget.Label();
        DTPCari2 = new widget.Tanggal();
        jLabel16 = new widget.Label();
        TCari = new widget.TextBox();
        BtnCari = new widget.Button();
        TabRawat = new javax.swing.JTabbedPane();
        Scroll = new widget.ScrollPane();
        tbSuhu = new widget.Table();
        Scroll1 = new widget.ScrollPane();
        tbRespirasi = new widget.Table();
        Scroll2 = new widget.ScrollPane();
        tbNadi = new widget.Table();
        Scroll3 = new widget.ScrollPane();
        tbSpO2 = new widget.Table();
        Scroll4 = new widget.ScrollPane();
        tbGCS = new widget.Table();
        Scroll5 = new widget.ScrollPane();
        tbKesadaran = new widget.Table();
        Scroll6 = new widget.ScrollPane();
        tbTensi = new widget.Table();

        jPopupMenu1.setName("jPopupMenu1"); // NOI18N

        ppPilihSemua.setBackground(new java.awt.Color(255, 255, 254));
        ppPilihSemua.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        ppPilihSemua.setForeground(new java.awt.Color(50, 50, 50));
        ppPilihSemua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        ppPilihSemua.setText("Pilih Semua");
        ppPilihSemua.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ppPilihSemua.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ppPilihSemua.setName("ppPilihSemua"); // NOI18N
        ppPilihSemua.setPreferredSize(new java.awt.Dimension(150, 26));
        ppPilihSemua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppPilihSemuaActionPerformed(evt);
            }
        });
        jPopupMenu1.add(ppPilihSemua);

        ppBersihkan.setBackground(new java.awt.Color(255, 255, 254));
        ppBersihkan.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        ppBersihkan.setForeground(new java.awt.Color(50, 50, 50));
        ppBersihkan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        ppBersihkan.setText("Hilangkan Pilihan");
        ppBersihkan.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ppBersihkan.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ppBersihkan.setName("ppBersihkan"); // NOI18N
        ppBersihkan.setPreferredSize(new java.awt.Dimension(150, 26));
        ppBersihkan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppBersihkanActionPerformed(evt);
            }
        });
        jPopupMenu1.add(ppBersihkan);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setIconImage(null);
        setIconImages(null);
        setUndecorated(true);
        setResizable(false);

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Pengiriman Data Observation-TTV Satu Sehat ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        jPanel3.setName("jPanel3"); // NOI18N
        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new java.awt.Dimension(44, 100));
        jPanel3.setLayout(new java.awt.BorderLayout(1, 1));

        panelGlass8.setName("panelGlass8"); // NOI18N
        panelGlass8.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        jLabel7.setText("Record :");
        jLabel7.setName("jLabel7"); // NOI18N
        jLabel7.setPreferredSize(new java.awt.Dimension(53, 23));
        panelGlass8.add(jLabel7);

        LCount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LCount.setText("0");
        LCount.setName("LCount"); // NOI18N
        LCount.setPreferredSize(new java.awt.Dimension(60, 23));
        panelGlass8.add(LCount);

        BtnAll1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAll1.setMnemonic('M');
        BtnAll1.setText("Semua");
        BtnAll1.setToolTipText("Alt+M");
        BtnAll1.setName("BtnAll1"); // NOI18N
        BtnAll1.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnAll1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAll1ActionPerformed(evt);
            }
        });
        BtnAll1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnAll1KeyPressed(evt);
            }
        });
        panelGlass8.add(BtnAll1);

        BtnKirim.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/34.png"))); // NOI18N
        BtnKirim.setMnemonic('K');
        BtnKirim.setText("Kirim");
        BtnKirim.setToolTipText("Alt+K");
        BtnKirim.setName("BtnKirim"); // NOI18N
        BtnKirim.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnKirim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKirimActionPerformed(evt);
            }
        });
        panelGlass8.add(BtnKirim);

        BtnUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/edit_f2.png"))); // NOI18N
        BtnUpdate.setMnemonic('U');
        BtnUpdate.setText("Update");
        BtnUpdate.setToolTipText("Alt+U");
        BtnUpdate.setName("BtnUpdate"); // NOI18N
        BtnUpdate.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnUpdateActionPerformed(evt);
            }
        });
        panelGlass8.add(BtnUpdate);

        BtnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/b_print.png"))); // NOI18N
        BtnPrint.setMnemonic('T');
        BtnPrint.setText("Cetak");
        BtnPrint.setToolTipText("Alt+T");
        BtnPrint.setName("BtnPrint"); // NOI18N
        BtnPrint.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPrintActionPerformed(evt);
            }
        });
        panelGlass8.add(BtnPrint);

        BtnKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        BtnKeluar.setMnemonic('K');
        BtnKeluar.setText("Keluar");
        BtnKeluar.setToolTipText("Alt+K");
        BtnKeluar.setName("BtnKeluar"); // NOI18N
        BtnKeluar.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKeluarActionPerformed(evt);
            }
        });
        BtnKeluar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnKeluarKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnKeluar);

        jPanel3.add(panelGlass8, java.awt.BorderLayout.CENTER);

        panelGlass9.setName("panelGlass9"); // NOI18N
        panelGlass9.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        jLabel15.setText("Tgl.Registrasi :");
        jLabel15.setName("jLabel15"); // NOI18N
        jLabel15.setPreferredSize(new java.awt.Dimension(85, 23));
        panelGlass9.add(jLabel15);

        DTPCari1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "24-12-2022" }));
        DTPCari1.setDisplayFormat("dd-MM-yyyy");
        DTPCari1.setName("DTPCari1"); // NOI18N
        DTPCari1.setOpaque(false);
        DTPCari1.setPreferredSize(new java.awt.Dimension(95, 23));
        panelGlass9.add(DTPCari1);

        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("s.d.");
        jLabel17.setName("jLabel17"); // NOI18N
        jLabel17.setPreferredSize(new java.awt.Dimension(24, 23));
        panelGlass9.add(jLabel17);

        DTPCari2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "24-12-2022" }));
        DTPCari2.setDisplayFormat("dd-MM-yyyy");
        DTPCari2.setName("DTPCari2"); // NOI18N
        DTPCari2.setOpaque(false);
        DTPCari2.setPreferredSize(new java.awt.Dimension(95, 23));
        panelGlass9.add(DTPCari2);

        jLabel16.setText("Key Word :");
        jLabel16.setName("jLabel16"); // NOI18N
        jLabel16.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass9.add(jLabel16);

        TCari.setName("TCari"); // NOI18N
        TCari.setPreferredSize(new java.awt.Dimension(210, 23));
        TCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariKeyPressed(evt);
            }
        });
        panelGlass9.add(TCari);

        BtnCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCari.setMnemonic('6');
        BtnCari.setToolTipText("Alt+6");
        BtnCari.setName("BtnCari"); // NOI18N
        BtnCari.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCariActionPerformed(evt);
            }
        });
        BtnCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCariKeyPressed(evt);
            }
        });
        panelGlass9.add(BtnCari);

        jPanel3.add(panelGlass9, java.awt.BorderLayout.PAGE_START);

        internalFrame1.add(jPanel3, java.awt.BorderLayout.PAGE_END);

        TabRawat.setBackground(new java.awt.Color(255, 255, 254));
        TabRawat.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(241, 246, 236)));
        TabRawat.setForeground(new java.awt.Color(50, 50, 50));
        TabRawat.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        TabRawat.setName("TabRawat"); // NOI18N
        TabRawat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabRawatMouseClicked(evt);
            }
        });

        Scroll.setComponentPopupMenu(jPopupMenu1);
        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);

        tbSuhu.setAutoCreateRowSorter(true);
        tbSuhu.setComponentPopupMenu(jPopupMenu1);
        tbSuhu.setName("tbSuhu"); // NOI18N
        Scroll.setViewportView(tbSuhu);

        TabRawat.addTab("Suhu (°C)", Scroll);

        Scroll1.setComponentPopupMenu(jPopupMenu1);
        Scroll1.setName("Scroll1"); // NOI18N
        Scroll1.setOpaque(true);

        tbRespirasi.setAutoCreateRowSorter(true);
        tbRespirasi.setComponentPopupMenu(jPopupMenu1);
        tbRespirasi.setName("tbRespirasi"); // NOI18N
        Scroll1.setViewportView(tbRespirasi);

        TabRawat.addTab("Respirasi (/menit)", Scroll1);

        Scroll2.setComponentPopupMenu(jPopupMenu1);
        Scroll2.setName("Scroll2"); // NOI18N
        Scroll2.setOpaque(true);

        tbNadi.setAutoCreateRowSorter(true);
        tbNadi.setComponentPopupMenu(jPopupMenu1);
        tbNadi.setName("tbNadi"); // NOI18N
        Scroll2.setViewportView(tbNadi);

        TabRawat.addTab("Nadi (/menit)", Scroll2);

        Scroll3.setComponentPopupMenu(jPopupMenu1);
        Scroll3.setName("Scroll3"); // NOI18N
        Scroll3.setOpaque(true);

        tbSpO2.setAutoCreateRowSorter(true);
        tbSpO2.setComponentPopupMenu(jPopupMenu1);
        tbSpO2.setName("tbSpO2"); // NOI18N
        Scroll3.setViewportView(tbSpO2);

        TabRawat.addTab("SpO2 (%)", Scroll3);

        Scroll4.setComponentPopupMenu(jPopupMenu1);
        Scroll4.setName("Scroll4"); // NOI18N
        Scroll4.setOpaque(true);

        tbGCS.setAutoCreateRowSorter(true);
        tbGCS.setComponentPopupMenu(jPopupMenu1);
        tbGCS.setName("tbGCS"); // NOI18N
        Scroll4.setViewportView(tbGCS);

        TabRawat.addTab("GCS (E,V,M)", Scroll4);

        Scroll5.setComponentPopupMenu(jPopupMenu1);
        Scroll5.setName("Scroll5"); // NOI18N
        Scroll5.setOpaque(true);

        tbKesadaran.setAutoCreateRowSorter(true);
        tbKesadaran.setComponentPopupMenu(jPopupMenu1);
        tbKesadaran.setName("tbKesadaran"); // NOI18N
        Scroll5.setViewportView(tbKesadaran);

        TabRawat.addTab("Kesadaran", Scroll5);

        Scroll6.setComponentPopupMenu(jPopupMenu1);
        Scroll6.setName("Scroll6"); // NOI18N
        Scroll6.setOpaque(true);

        tbTensi.setAutoCreateRowSorter(true);
        tbTensi.setComponentPopupMenu(jPopupMenu1);
        tbTensi.setName("tbTensi"); // NOI18N
        Scroll6.setViewportView(tbTensi);

        TabRawat.addTab("Tensi (mmHg)", Scroll6);

        internalFrame1.add(TabRawat, java.awt.BorderLayout.CENTER);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        dispose();
    }//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            dispose();
        }else{Valid.pindah(evt,BtnPrint,BtnKeluar);}
    }//GEN-LAST:event_BtnKeluarKeyPressed

    private void BtnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPrintActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if(TabRawat.getSelectedIndex()==0){
            if(tabModeSuhu.getRowCount()==0){
                JOptionPane.showMessageDialog(null,"Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
                TCari.requestFocus();
            }else if(tabModeSuhu.getRowCount()!=0){            
                    Map<String, Object> param = new HashMap<>();    
                    param.put("namars",akses.getnamars());
                    param.put("alamatrs",akses.getalamatrs());
                    param.put("kotars",akses.getkabupatenrs());
                    param.put("propinsirs",akses.getpropinsirs());
                    param.put("kontakrs",akses.getkontakrs());
                    param.put("emailrs",akses.getemailrs());   
                    param.put("logo",Sequel.cariGambar("select setting.logo from setting")); 
                    param.put("parameter","%"+TCari.getText().trim()+"%");
                    param.put("tanggal1",Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                    param.put("tanggal2",Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                    Valid.MyReport("rptKirimObservationSatuSehat.jasper","report","::[ Kirim Data Observation Satu Sehat Kemenkes ]::",param);            
            }
        }
        this.setCursor(Cursor.getDefaultCursor());       
    }//GEN-LAST:event_BtnPrintActionPerformed

    private void TCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            BtnCariActionPerformed(null);
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            BtnCariActionPerformed(null);
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            BtnKeluar.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_UP){
            tbSuhu.requestFocus();
        }
    }//GEN-LAST:event_TCariKeyPressed

    private void BtnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        TabRawatMouseClicked(null);
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_BtnCariActionPerformed

    private void BtnCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnCariActionPerformed(null);
        }else{
            Valid.pindah(evt,TCari,BtnPrint);
        }
    }//GEN-LAST:event_BtnCariKeyPressed

    private void BtnKirimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKirimActionPerformed
        if(TabRawat.getSelectedIndex()==0){
            for(i=0;i<tbSuhu.getRowCount();i++){
                if(tbSuhu.getValueAt(i,0).toString().equals("true")&&(!tbSuhu.getValueAt(i,5).toString().equals(""))&&(!tbSuhu.getValueAt(i,12).toString().equals(""))&&tbSuhu.getValueAt(i,15).toString().equals("")){
                    try {
                        iddokter=cekViaSatuSehat.tampilIDParktisi(tbSuhu.getValueAt(i,12).toString());
                        idpasien=cekViaSatuSehat.tampilIDPasien(tbSuhu.getValueAt(i,5).toString());
                        try{
                            headers = new HttpHeaders();
                            headers.setContentType(MediaType.APPLICATION_JSON);
                            headers.add("Authorization", "Bearer "+api.TokenSatuSehat());
                            json = "{" +
                                        "\"resourceType\": \"Observation\"," +
                                        "\"status\": \"final\"," +
                                        "\"category\": [" +
                                            "{" +
                                                "\"coding\": [" +
                                                    "{" +
                                                        "\"system\": \"http://terminology.hl7.org/CodeSystem/observation-category\"," +
                                                        "\"code\": \"vital-signs\"," +
                                                        "\"display\": \"Vital Signs\"" +
                                                    "}" +
                                                "]" +
                                            "}" +
                                        "]," +
                                        "\"code\": {" +
                                            "\"coding\": [" +
                                                "{" +
                                                    "\"system\": \"http://loinc.org\"," +
                                                    "\"code\": \"8310-5\"," +
                                                    "\"display\": \"Body temperature\"" +
                                                "}" +
                                            "]" +
                                        "}," +
                                        "\"subject\": {" +
                                            "\"reference\": \"Patient/"+idpasien+"\"" +
                                        "}," +
                                        "\"performer\": [" +
                                            "{" +
                                                "\"reference\": \"Practitioner/"+iddokter+"\"" +
                                            "}" +
                                        "]," +
                                        "\"encounter\": {" +
                                            "\"reference\": \"Encounter/"+tbSuhu.getValueAt(i,9).toString()+"\"," +
                                            "\"display\": \"Pemeriksaan Fisik Suhu Badan "+tbSuhu.getValueAt(i,4).toString()+" Pada Tanggal "+tbSuhu.getValueAt(i,13).toString()+" Jam "+tbSuhu.getValueAt(i,14).toString()+"\"" +
                                        "}," +
                                        "\"effectiveDateTime\": \""+tbSuhu.getValueAt(i,13).toString()+"T"+tbSuhu.getValueAt(i,14).toString()+"+07:00\"," +
                                        "\"valueQuantity\": {" +
                                            "\"value\": "+tbSuhu.getValueAt(i,10).toString().replaceAll(",",".")+"," +
                                            "\"unit\": \"degree Celsius\"," +
                                            "\"system\": \"http://unitsofmeasure.org\"," +
                                            "\"code\": \"Cel\"" +
                                        "}" +
                                   "}";
                            System.out.println("URL : "+link+"/Observation");
                            System.out.println("Request JSON : "+json);
                            requestEntity = new HttpEntity(json,headers);
                            json=api.getRest().exchange(link+"/Observation", HttpMethod.POST, requestEntity, String.class).getBody();
                            System.out.println("Result JSON : "+json);
                            root = mapper.readTree(json);
                            response = root.path("id");
                            if(!response.asText().equals("")){
                                Sequel.menyimpan("satu_sehat_observationttvsuhu","?,?,?,?,?","Observation Suhu",5,new String[]{
                                    tbSuhu.getValueAt(i,2).toString(),tbSuhu.getValueAt(i,13).toString(),tbSuhu.getValueAt(i,14).toString(),tbSuhu.getValueAt(i,7).toString(),response.asText()
                                });
                            }
                        }catch(Exception e){
                            System.out.println("Notifikasi Bridging : "+e);
                        }
                    } catch (Exception e) {
                        System.out.println("Notifikasi : "+e);
                    }
                }
            }
            tampilsuhu();
        }else if(TabRawat.getSelectedIndex()==1){
            for(i=0;i<tbRespirasi.getRowCount();i++){
                if(tbRespirasi.getValueAt(i,0).toString().equals("true")&&(!tbRespirasi.getValueAt(i,5).toString().equals(""))&&(!tbRespirasi.getValueAt(i,12).toString().equals(""))&&tbRespirasi.getValueAt(i,15).toString().equals("")){
                    try {
                        iddokter=cekViaSatuSehat.tampilIDParktisi(tbRespirasi.getValueAt(i,12).toString());
                        idpasien=cekViaSatuSehat.tampilIDPasien(tbRespirasi.getValueAt(i,5).toString());
                        try{
                            headers = new HttpHeaders();
                            headers.setContentType(MediaType.APPLICATION_JSON);
                            headers.add("Authorization", "Bearer "+api.TokenSatuSehat());
                            json = "{" +
                                        "\"resourceType\": \"Observation\"," +
                                        "\"status\": \"final\"," +
                                        "\"category\": [" +
                                            "{" +
                                                "\"coding\": [" +
                                                    "{" +
                                                        "\"system\": \"http://terminology.hl7.org/CodeSystem/observation-category\"," +
                                                        "\"code\": \"vital-signs\"," +
                                                        "\"display\": \"Vital Signs\"" +
                                                    "}" +
                                                "]" +
                                            "}" +
                                        "]," +
                                        "\"code\": {" +
                                            "\"coding\": [" +
                                                "{" +
                                                    "\"system\": \"http://loinc.org\"," +
                                                    "\"code\": \"9279-1\"," +
                                                    "\"display\": \"Respiratory rate\"" +
                                                "}" +
                                            "]" +
                                        "}," +
                                        "\"subject\": {" +
                                            "\"reference\": \"Patient/"+idpasien+"\"" +
                                        "}," +
                                        "\"performer\": [" +
                                            "{" +
                                                "\"reference\": \"Practitioner/"+iddokter+"\"" +
                                            "}" +
                                        "]," +
                                        "\"encounter\": {" +
                                            "\"reference\": \"Encounter/"+tbRespirasi.getValueAt(i,9).toString()+"\"," +
                                            "\"display\": \"Pemeriksaan Fisik Respirasi "+tbRespirasi.getValueAt(i,4).toString()+" Pada Tanggal "+tbRespirasi.getValueAt(i,13).toString()+" Jam "+tbRespirasi.getValueAt(i,14).toString()+"\"" +
                                        "}," +
                                        "\"effectiveDateTime\": \""+tbRespirasi.getValueAt(i,13).toString()+"T"+tbRespirasi.getValueAt(i,14).toString()+"+07:00\"," +
                                        "\"valueQuantity\": {" +
                                            "\"value\": "+tbRespirasi.getValueAt(i,10).toString().replaceAll(",",".")+"," +
                                            "\"unit\": \"breaths/minute\"," +
                                            "\"system\": \"http://unitsofmeasure.org\"," +
                                            "\"code\": \"/min\"" +
                                        "}" +
                                   "}";
                            System.out.println("URL : "+link+"/Observation");
                            System.out.println("Request JSON : "+json);
                            requestEntity = new HttpEntity(json,headers);
                            json=api.getRest().exchange(link+"/Observation", HttpMethod.POST, requestEntity, String.class).getBody();
                            System.out.println("Result JSON : "+json);
                            root = mapper.readTree(json);
                            response = root.path("id");
                            if(!response.asText().equals("")){
                                Sequel.menyimpan("satu_sehat_observationttvrespirasi","?,?,?,?,?","Observation Respirasi",5,new String[]{
                                    tbRespirasi.getValueAt(i,2).toString(),tbRespirasi.getValueAt(i,13).toString(),tbRespirasi.getValueAt(i,14).toString(),tbRespirasi.getValueAt(i,7).toString(),response.asText()
                                });
                            }
                        }catch(Exception e){
                            System.out.println("Notifikasi Bridging : "+e);
                        }
                    } catch (Exception e) {
                        System.out.println("Notifikasi : "+e);
                    }
                }
            }
            tampilrespirasi();
        }else if(TabRawat.getSelectedIndex()==2){
            for(i=0;i<tbNadi.getRowCount();i++){
                if(tbNadi.getValueAt(i,0).toString().equals("true")&&(!tbNadi.getValueAt(i,5).toString().equals(""))&&(!tbNadi.getValueAt(i,12).toString().equals(""))&&tbNadi.getValueAt(i,15).toString().equals("")){
                    try {
                        iddokter=cekViaSatuSehat.tampilIDParktisi(tbNadi.getValueAt(i,12).toString());
                        idpasien=cekViaSatuSehat.tampilIDPasien(tbNadi.getValueAt(i,5).toString());
                        try{
                            headers = new HttpHeaders();
                            headers.setContentType(MediaType.APPLICATION_JSON);
                            headers.add("Authorization", "Bearer "+api.TokenSatuSehat());
                            json = "{" +
                                        "\"resourceType\": \"Observation\"," +
                                        "\"status\": \"final\"," +
                                        "\"category\": [" +
                                            "{" +
                                                "\"coding\": [" +
                                                    "{" +
                                                        "\"system\": \"http://terminology.hl7.org/CodeSystem/observation-category\"," +
                                                        "\"code\": \"vital-signs\"," +
                                                        "\"display\": \"Vital Signs\"" +
                                                    "}" +
                                                "]" +
                                            "}" +
                                        "]," +
                                        "\"code\": {" +
                                            "\"coding\": [" +
                                                "{" +
                                                    "\"system\": \"http://loinc.org\"," +
                                                    "\"code\": \"8867-4\"," +
                                                    "\"display\": \"Heart rate\"" +
                                                "}" +
                                            "]" +
                                        "}," +
                                        "\"subject\": {" +
                                            "\"reference\": \"Patient/"+idpasien+"\"" +
                                        "}," +
                                        "\"performer\": [" +
                                            "{" +
                                                "\"reference\": \"Practitioner/"+iddokter+"\"" +
                                            "}" +
                                        "]," +
                                        "\"encounter\": {" +
                                            "\"reference\": \"Encounter/"+tbNadi.getValueAt(i,9).toString()+"\"," +
                                            "\"display\": \"Pemeriksaan Fisik Nadi "+tbNadi.getValueAt(i,4).toString()+" Pada Tanggal "+tbNadi.getValueAt(i,13).toString()+" Jam "+tbNadi.getValueAt(i,14).toString()+"\"" +
                                        "}," +
                                        "\"effectiveDateTime\": \""+tbNadi.getValueAt(i,13).toString()+"T"+tbNadi.getValueAt(i,14).toString()+"+07:00\"," +
                                        "\"valueQuantity\": {" +
                                            "\"value\": "+tbNadi.getValueAt(i,10).toString().replaceAll(",",".")+"," +
                                            "\"unit\": \"breaths/minute\"," +
                                            "\"system\": \"http://unitsofmeasure.org\"," +
                                            "\"code\": \"/min\"" +
                                        "}" +
                                   "}";
                            System.out.println("URL : "+link+"/Observation");
                            System.out.println("Request JSON : "+json);
                            requestEntity = new HttpEntity(json,headers);
                            json=api.getRest().exchange(link+"/Observation", HttpMethod.POST, requestEntity, String.class).getBody();
                            System.out.println("Result JSON : "+json);
                            root = mapper.readTree(json);
                            response = root.path("id");
                            if(!response.asText().equals("")){
                                Sequel.menyimpan("satu_sehat_observationttvnadi","?,?,?,?,?","Observation Nadi",5,new String[]{
                                    tbNadi.getValueAt(i,2).toString(),tbNadi.getValueAt(i,13).toString(),tbNadi.getValueAt(i,14).toString(),tbNadi.getValueAt(i,7).toString(),response.asText()
                                });
                            }
                        }catch(Exception e){
                            System.out.println("Notifikasi Bridging : "+e);
                        }
                    } catch (Exception e) {
                        System.out.println("Notifikasi : "+e);
                    }
                }
            }
            tampilnadi();
        }else if(TabRawat.getSelectedIndex()==3){
            for(i=0;i<tbSpO2.getRowCount();i++){
                if(tbSpO2.getValueAt(i,0).toString().equals("true")&&(!tbSpO2.getValueAt(i,5).toString().equals(""))&&(!tbSpO2.getValueAt(i,12).toString().equals(""))&&tbSpO2.getValueAt(i,15).toString().equals("")){
                    try {
                        iddokter=cekViaSatuSehat.tampilIDParktisi(tbSpO2.getValueAt(i,12).toString());
                        idpasien=cekViaSatuSehat.tampilIDPasien(tbSpO2.getValueAt(i,5).toString());
                        try{
                            headers = new HttpHeaders();
                            headers.setContentType(MediaType.APPLICATION_JSON);
                            headers.add("Authorization", "Bearer "+api.TokenSatuSehat());
                            json = "{" +
                                        "\"resourceType\": \"Observation\"," +
                                        "\"status\": \"final\"," +
                                        "\"category\": [" +
                                            "{" +
                                                "\"coding\": [" +
                                                    "{" +
                                                        "\"system\": \"http://terminology.hl7.org/CodeSystem/observation-category\"," +
                                                        "\"code\": \"vital-signs\"," +
                                                        "\"display\": \"Vital Signs\"" +
                                                    "}" +
                                                "]" +
                                            "}" +
                                        "]," +
                                        "\"code\": {" +
                                            "\"coding\": [" +
                                                "{" +
                                                    "\"system\": \"http://loinc.org\"," +
                                                    "\"code\": \"59408-5\"," +
                                                    "\"display\": \"Oxygen saturation\"" +
                                                "}" +
                                            "]" +
                                        "}," +
                                        "\"subject\": {" +
                                            "\"reference\": \"Patient/"+idpasien+"\"" +
                                        "}," +
                                        "\"performer\": [" +
                                            "{" +
                                                "\"reference\": \"Practitioner/"+iddokter+"\"" +
                                            "}" +
                                        "]," +
                                        "\"encounter\": {" +
                                            "\"reference\": \"Encounter/"+tbSpO2.getValueAt(i,9).toString()+"\"," +
                                            "\"display\": \"Pemeriksaan Fisik SpO2 "+tbSpO2.getValueAt(i,4).toString()+" Pada Tanggal "+tbSpO2.getValueAt(i,13).toString()+" Jam "+tbSpO2.getValueAt(i,14).toString()+"\"" +
                                        "}," +
                                        "\"effectiveDateTime\": \""+tbSpO2.getValueAt(i,13).toString()+"T"+tbSpO2.getValueAt(i,14).toString()+"+07:00\"," +
                                        "\"valueQuantity\": {" +
                                            "\"value\": "+tbSpO2.getValueAt(i,10).toString().replaceAll(",",".")+"," +
                                            "\"unit\": \"percent saturation\"," +
                                            "\"system\": \"http://unitsofmeasure.org\"," +
                                            "\"code\": \"%\"" +
                                        "}" +
                                   "}";
                            System.out.println("URL : "+link+"/Observation");
                            System.out.println("Request JSON : "+json);
                            requestEntity = new HttpEntity(json,headers);
                            json=api.getRest().exchange(link+"/Observation", HttpMethod.POST, requestEntity, String.class).getBody();
                            System.out.println("Result JSON : "+json);
                            root = mapper.readTree(json);
                            response = root.path("id");
                            if(!response.asText().equals("")){
                                Sequel.menyimpan("satu_sehat_observationttvspo2","?,?,?,?,?","Observation SpO2",5,new String[]{
                                    tbSpO2.getValueAt(i,2).toString(),tbSpO2.getValueAt(i,13).toString(),tbSpO2.getValueAt(i,14).toString(),tbSpO2.getValueAt(i,7).toString(),response.asText()
                                });
                            }
                        }catch(Exception e){
                            System.out.println("Notifikasi Bridging : "+e);
                        }
                    } catch (Exception e) {
                        System.out.println("Notifikasi : "+e);
                    }
                }
            }
            tampilspo2();
        }else if(TabRawat.getSelectedIndex()==4){
            for(i=0;i<tbGCS.getRowCount();i++){
                if(tbGCS.getValueAt(i,0).toString().equals("true")&&(!tbGCS.getValueAt(i,5).toString().equals(""))&&(!tbGCS.getValueAt(i,12).toString().equals(""))&&tbGCS.getValueAt(i,15).toString().equals("")){
                    try {
                        iddokter=cekViaSatuSehat.tampilIDParktisi(tbGCS.getValueAt(i,12).toString());
                        idpasien=cekViaSatuSehat.tampilIDPasien(tbGCS.getValueAt(i,5).toString());
                        try{
                            headers = new HttpHeaders();
                            headers.setContentType(MediaType.APPLICATION_JSON);
                            headers.add("Authorization", "Bearer "+api.TokenSatuSehat());
                            json = "{" +
                                        "\"resourceType\": \"Observation\"," +
                                        "\"status\": \"final\"," +
                                        "\"category\": [" +
                                            "{" +
                                                "\"coding\": [" +
                                                    "{" +
                                                        "\"system\": \"http://terminology.hl7.org/CodeSystem/observation-category\"," +
                                                        "\"code\": \"vital-signs\"," +
                                                        "\"display\": \"Vital Signs\"" +
                                                    "}" +
                                                "]" +
                                            "}" +
                                        "]," +
                                        "\"code\": {" +
                                            "\"coding\": [" +
                                                "{" +
                                                    "\"system\": \"http://loinc.org\"," +
                                                    "\"code\": \"9269-2\"," +
                                                    "\"display\": \"Glasgow coma score total\"" +
                                                "}" +
                                            "]" +
                                        "}," +
                                        "\"subject\": {" +
                                            "\"reference\": \"Patient/"+idpasien+"\"" +
                                        "}," +
                                        "\"performer\": [" +
                                            "{" +
                                                "\"reference\": \"Practitioner/"+iddokter+"\"" +
                                            "}" +
                                        "]," +
                                        "\"encounter\": {" +
                                            "\"reference\": \"Encounter/"+tbGCS.getValueAt(i,9).toString()+"\"," +
                                            "\"display\": \"Pemeriksaan Fisik GCS "+tbGCS.getValueAt(i,4).toString()+" Pada Tanggal "+tbGCS.getValueAt(i,13).toString()+" Jam "+tbGCS.getValueAt(i,14).toString()+"\"" +
                                        "}," +
                                        "\"effectiveDateTime\": \""+tbGCS.getValueAt(i,13).toString()+"T"+tbGCS.getValueAt(i,14).toString()+"+07:00\"," +
                                        "\"valueQuantity\": {" +
                                            "\"value\": "+tbGCS.getValueAt(i,10).toString().replaceAll(",",".")+"," +
                                            "\"system\": \"http://unitsofmeasure.org\"," +
                                            "\"code\": \"{score}\"" +
                                        "}" +
                                   "}";
                            System.out.println("URL : "+link+"/Observation");
                            System.out.println("Request JSON : "+json);
                            requestEntity = new HttpEntity(json,headers);
                            json=api.getRest().exchange(link+"/Observation", HttpMethod.POST, requestEntity, String.class).getBody();
                            System.out.println("Result JSON : "+json);
                            root = mapper.readTree(json);
                            response = root.path("id");
                            if(!response.asText().equals("")){
                                Sequel.menyimpan("satu_sehat_observationttvgcs","?,?,?,?,?","Observation GCS",5,new String[]{
                                    tbGCS.getValueAt(i,2).toString(),tbGCS.getValueAt(i,13).toString(),tbGCS.getValueAt(i,14).toString(),tbGCS.getValueAt(i,7).toString(),response.asText()
                                });
                            }
                        }catch(Exception e){
                            System.out.println("Notifikasi Bridging : "+e);
                        }
                    } catch (Exception e) {
                        System.out.println("Notifikasi : "+e);
                    }
                }
            }
            tampilgcs();
        }
    }//GEN-LAST:event_BtnKirimActionPerformed

    private void ppPilihSemuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppPilihSemuaActionPerformed
        if(TabRawat.getSelectedIndex()==0){
            for(i=0;i<tbSuhu.getRowCount();i++){
                tbSuhu.setValueAt(true,i,0);
            }
        }else if(TabRawat.getSelectedIndex()==1){
            for(i=0;i<tbRespirasi.getRowCount();i++){
                tbRespirasi.setValueAt(true,i,0);
            }
        }else if(TabRawat.getSelectedIndex()==2){
            for(i=0;i<tbNadi.getRowCount();i++){
                tbNadi.setValueAt(true,i,0);
            }
        }else if(TabRawat.getSelectedIndex()==3){
            for(i=0;i<tbSpO2.getRowCount();i++){
                tbSpO2.setValueAt(true,i,0);
            }
        }else if(TabRawat.getSelectedIndex()==4){
            for(i=0;i<tbGCS.getRowCount();i++){
                tbGCS.setValueAt(true,i,0);
            }
        }
    }//GEN-LAST:event_ppPilihSemuaActionPerformed

    private void ppBersihkanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppBersihkanActionPerformed
        if(TabRawat.getSelectedIndex()==0){
            for(i=0;i<tbSuhu.getRowCount();i++){
                tbSuhu.setValueAt(false,i,0);
            }
        }else if(TabRawat.getSelectedIndex()==1){
            for(i=0;i<tbRespirasi.getRowCount();i++){
                tbRespirasi.setValueAt(false,i,0);
            }
        }else if(TabRawat.getSelectedIndex()==2){
            for(i=0;i<tbNadi.getRowCount();i++){
                tbNadi.setValueAt(false,i,0);
            }
        }else if(TabRawat.getSelectedIndex()==3){
            for(i=0;i<tbSpO2.getRowCount();i++){
                tbSpO2.setValueAt(false,i,0);
            }
        }else if(TabRawat.getSelectedIndex()==4){
            for(i=0;i<tbGCS.getRowCount();i++){
                tbGCS.setValueAt(false,i,0);
            }
        }
    }//GEN-LAST:event_ppBersihkanActionPerformed

    private void BtnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnUpdateActionPerformed
        if(TabRawat.getSelectedIndex()==0){
            for(i=0;i<tbSuhu.getRowCount();i++){
                if(tbSuhu.getValueAt(i,0).toString().equals("true")&&(!tbSuhu.getValueAt(i,5).toString().equals(""))&&(!tbSuhu.getValueAt(i,12).toString().equals(""))&&(!tbSuhu.getValueAt(i,15).toString().equals(""))){
                    try {
                        iddokter=cekViaSatuSehat.tampilIDParktisi(tbSuhu.getValueAt(i,12).toString());
                        idpasien=cekViaSatuSehat.tampilIDPasien(tbSuhu.getValueAt(i,5).toString());
                        try{
                            headers = new HttpHeaders();
                            headers.setContentType(MediaType.APPLICATION_JSON);
                            headers.add("Authorization", "Bearer "+api.TokenSatuSehat());
                            json = "{" +
                                        "\"resourceType\": \"Observation\"," +
                                        "\"id\": \""+tbSuhu.getValueAt(i,15).toString()+"\"," +
                                        "\"status\": \"final\"," +
                                        "\"category\": [" +
                                            "{" +
                                                "\"coding\": [" +
                                                    "{" +
                                                        "\"system\": \"http://terminology.hl7.org/CodeSystem/observation-category\"," +
                                                        "\"code\": \"vital-signs\"," +
                                                        "\"display\": \"Vital Signs\"" +
                                                    "}" +
                                                "]" +
                                            "}" +
                                        "]," +
                                        "\"code\": {" +
                                            "\"coding\": [" +
                                                "{" +
                                                    "\"system\": \"http://loinc.org\"," +
                                                    "\"code\": \"8310-5\"," +
                                                    "\"display\": \"Body temperature\"" +
                                                "}" +
                                            "]" +
                                        "}," +
                                        "\"subject\": {" +
                                            "\"reference\": \"Patient/"+idpasien+"\"" +
                                        "}," +
                                        "\"performer\": [" +
                                            "{" +
                                                "\"reference\": \"Practitioner/"+iddokter+"\"" +
                                            "}" +
                                        "]," +
                                        "\"encounter\": {" +
                                            "\"reference\": \"Encounter/"+tbSuhu.getValueAt(i,9).toString()+"\"," +
                                            "\"display\": \"Pemeriksaan Fisik Suhu Badan "+tbSuhu.getValueAt(i,4).toString()+" Pada Tanggal "+tbSuhu.getValueAt(i,13).toString()+" Jam "+tbSuhu.getValueAt(i,14).toString()+"\"" +
                                        "}," +
                                        "\"effectiveDateTime\": \""+tbSuhu.getValueAt(i,13).toString()+"T"+tbSuhu.getValueAt(i,14).toString()+"+07:00\"," +
                                        "\"valueQuantity\": {" +
                                            "\"value\": "+tbSuhu.getValueAt(i,10).toString().replaceAll(",",".")+"," +
                                            "\"unit\": \"degree Celsius\"," +
                                            "\"system\": \"http://unitsofmeasure.org\"," +
                                            "\"code\": \"Cel\"" +
                                        "}" +
                                   "}";
                            System.out.println("URL : "+link+"/Observation/"+tbSuhu.getValueAt(i,15).toString());
                            System.out.println("Request JSON : "+json);
                            requestEntity = new HttpEntity(json,headers);
                            json=api.getRest().exchange(link+"/Observation/"+tbSuhu.getValueAt(i,15).toString(), HttpMethod.PUT, requestEntity, String.class).getBody();
                            System.out.println("Result JSON : "+json);
                        }catch(Exception e){
                            System.out.println("Notifikasi Bridging : "+e);
                        }
                    } catch (Exception e) {
                        System.out.println("Notifikasi : "+e);
                    }
                }
            }
            tampilsuhu();
        }else if(TabRawat.getSelectedIndex()==1){
            for(i=0;i<tbRespirasi.getRowCount();i++){
                if(tbRespirasi.getValueAt(i,0).toString().equals("true")&&(!tbRespirasi.getValueAt(i,5).toString().equals(""))&&(!tbRespirasi.getValueAt(i,12).toString().equals(""))&&(!tbRespirasi.getValueAt(i,15).toString().equals(""))){
                    try {
                        iddokter=cekViaSatuSehat.tampilIDParktisi(tbRespirasi.getValueAt(i,12).toString());
                        idpasien=cekViaSatuSehat.tampilIDPasien(tbRespirasi.getValueAt(i,5).toString());
                        try{
                            headers = new HttpHeaders();
                            headers.setContentType(MediaType.APPLICATION_JSON);
                            headers.add("Authorization", "Bearer "+api.TokenSatuSehat());
                            json = "{" +
                                        "\"resourceType\": \"Observation\"," +
                                        "\"id\": \""+tbRespirasi.getValueAt(i,15).toString()+"\"," +
                                        "\"status\": \"final\"," +
                                        "\"category\": [" +
                                            "{" +
                                                "\"coding\": [" +
                                                    "{" +
                                                        "\"system\": \"http://terminology.hl7.org/CodeSystem/observation-category\"," +
                                                        "\"code\": \"vital-signs\"," +
                                                        "\"display\": \"Vital Signs\"" +
                                                    "}" +
                                                "]" +
                                            "}" +
                                        "]," +
                                        "\"code\": {" +
                                            "\"coding\": [" +
                                                "{" +
                                                    "\"system\": \"http://loinc.org\"," +
                                                    "\"code\": \"9279-1\"," +
                                                    "\"display\": \"Respiratory rate\"" +
                                                "}" +
                                            "]" +
                                        "}," +
                                        "\"subject\": {" +
                                            "\"reference\": \"Patient/"+idpasien+"\"" +
                                        "}," +
                                        "\"performer\": [" +
                                            "{" +
                                                "\"reference\": \"Practitioner/"+iddokter+"\"" +
                                            "}" +
                                        "]," +
                                        "\"encounter\": {" +
                                            "\"reference\": \"Encounter/"+tbRespirasi.getValueAt(i,9).toString()+"\"," +
                                            "\"display\": \"Pemeriksaan Fisik Respirasi "+tbRespirasi.getValueAt(i,4).toString()+" Pada Tanggal "+tbRespirasi.getValueAt(i,13).toString()+" Jam "+tbRespirasi.getValueAt(i,14).toString()+"\"" +
                                        "}," +
                                        "\"effectiveDateTime\": \""+tbRespirasi.getValueAt(i,13).toString()+"T"+tbRespirasi.getValueAt(i,14).toString()+"+07:00\"," +
                                        "\"valueQuantity\": {" +
                                            "\"value\": "+tbRespirasi.getValueAt(i,10).toString().replaceAll(",",".")+"," +
                                            "\"unit\": \"breaths/minute\"," +
                                            "\"system\": \"http://unitsofmeasure.org\"," +
                                            "\"code\": \"/min\"" +
                                        "}" +
                                   "}";
                            System.out.println("URL : "+link+"/Observation/"+tbRespirasi.getValueAt(i,15).toString());
                            System.out.println("Request JSON : "+json);
                            requestEntity = new HttpEntity(json,headers);
                            json=api.getRest().exchange(link+"/Observation/"+tbRespirasi.getValueAt(i,15).toString(), HttpMethod.PUT, requestEntity, String.class).getBody();
                            System.out.println("Result JSON : "+json);
                        }catch(Exception e){
                            System.out.println("Notifikasi Bridging : "+e);
                        }
                    } catch (Exception e) {
                        System.out.println("Notifikasi : "+e);
                    }
                }
            }
            tampilrespirasi();
        }else if(TabRawat.getSelectedIndex()==2){
            for(i=0;i<tbNadi.getRowCount();i++){
                if(tbNadi.getValueAt(i,0).toString().equals("true")&&(!tbNadi.getValueAt(i,5).toString().equals(""))&&(!tbNadi.getValueAt(i,12).toString().equals(""))&&(!tbNadi.getValueAt(i,15).toString().equals(""))){
                    try {
                        iddokter=cekViaSatuSehat.tampilIDParktisi(tbNadi.getValueAt(i,12).toString());
                        idpasien=cekViaSatuSehat.tampilIDPasien(tbNadi.getValueAt(i,5).toString());
                        try{
                            headers = new HttpHeaders();
                            headers.setContentType(MediaType.APPLICATION_JSON);
                            headers.add("Authorization", "Bearer "+api.TokenSatuSehat());
                            json = "{" +
                                        "\"resourceType\": \"Observation\"," +
                                        "\"id\": \""+tbNadi.getValueAt(i,15).toString()+"\"," +
                                        "\"status\": \"final\"," +
                                        "\"category\": [" +
                                            "{" +
                                                "\"coding\": [" +
                                                    "{" +
                                                        "\"system\": \"http://terminology.hl7.org/CodeSystem/observation-category\"," +
                                                        "\"code\": \"vital-signs\"," +
                                                        "\"display\": \"Vital Signs\"" +
                                                    "}" +
                                                "]" +
                                            "}" +
                                        "]," +
                                        "\"code\": {" +
                                            "\"coding\": [" +
                                                "{" +
                                                    "\"system\": \"http://loinc.org\"," +
                                                    "\"code\": \"8867-4\"," +
                                                    "\"display\": \"Heart rate\"" +
                                                "}" +
                                            "]" +
                                        "}," +
                                        "\"subject\": {" +
                                            "\"reference\": \"Patient/"+idpasien+"\"" +
                                        "}," +
                                        "\"performer\": [" +
                                            "{" +
                                                "\"reference\": \"Practitioner/"+iddokter+"\"" +
                                            "}" +
                                        "]," +
                                        "\"encounter\": {" +
                                            "\"reference\": \"Encounter/"+tbNadi.getValueAt(i,9).toString()+"\"," +
                                            "\"display\": \"Pemeriksaan Fisik Nadi "+tbNadi.getValueAt(i,4).toString()+" Pada Tanggal "+tbNadi.getValueAt(i,13).toString()+" Jam "+tbNadi.getValueAt(i,14).toString()+"\"" +
                                        "}," +
                                        "\"effectiveDateTime\": \""+tbNadi.getValueAt(i,13).toString()+"T"+tbNadi.getValueAt(i,14).toString()+"+07:00\"," +
                                        "\"valueQuantity\": {" +
                                            "\"value\": "+tbNadi.getValueAt(i,10).toString().replaceAll(",",".")+"," +
                                            "\"unit\": \"breaths/minute\"," +
                                            "\"system\": \"http://unitsofmeasure.org\"," +
                                            "\"code\": \"/min\"" +
                                        "}" +
                                   "}";
                            System.out.println("URL : "+link+"/Observation/"+tbNadi.getValueAt(i,15).toString());
                            System.out.println("Request JSON : "+json);
                            requestEntity = new HttpEntity(json,headers);
                            json=api.getRest().exchange(link+"/Observation/"+tbNadi.getValueAt(i,15).toString(), HttpMethod.PUT, requestEntity, String.class).getBody();
                            System.out.println("Result JSON : "+json);
                        }catch(Exception e){
                            System.out.println("Notifikasi Bridging : "+e);
                        }
                    } catch (Exception e) {
                        System.out.println("Notifikasi : "+e);
                    }
                }
            }
            tampilnadi();
        }else if(TabRawat.getSelectedIndex()==3){
            for(i=0;i<tbSpO2.getRowCount();i++){
                if(tbSpO2.getValueAt(i,0).toString().equals("true")&&(!tbSpO2.getValueAt(i,5).toString().equals(""))&&(!tbSpO2.getValueAt(i,12).toString().equals(""))&&(!tbSpO2.getValueAt(i,15).toString().equals(""))){
                    try {
                        iddokter=cekViaSatuSehat.tampilIDParktisi(tbSpO2.getValueAt(i,12).toString());
                        idpasien=cekViaSatuSehat.tampilIDPasien(tbSpO2.getValueAt(i,5).toString());
                        try{
                            headers = new HttpHeaders();
                            headers.setContentType(MediaType.APPLICATION_JSON);
                            headers.add("Authorization", "Bearer "+api.TokenSatuSehat());
                            json = "{" +
                                        "\"resourceType\": \"Observation\"," +
                                        "\"id\": \""+tbSpO2.getValueAt(i,15).toString()+"\"," +
                                        "\"status\": \"final\"," +
                                        "\"category\": [" +
                                            "{" +
                                                "\"coding\": [" +
                                                    "{" +
                                                        "\"system\": \"http://terminology.hl7.org/CodeSystem/observation-category\"," +
                                                        "\"code\": \"vital-signs\"," +
                                                        "\"display\": \"Vital Signs\"" +
                                                    "}" +
                                                "]" +
                                            "}" +
                                        "]," +
                                        "\"code\": {" +
                                            "\"coding\": [" +
                                                "{" +
                                                    "\"system\": \"http://loinc.org\"," +
                                                    "\"code\": \"59408-5\"," +
                                                    "\"display\": \"Oxygen saturation\"" +
                                                "}" +
                                            "]" +
                                        "}," +
                                        "\"subject\": {" +
                                            "\"reference\": \"Patient/"+idpasien+"\"" +
                                        "}," +
                                        "\"performer\": [" +
                                            "{" +
                                                "\"reference\": \"Practitioner/"+iddokter+"\"" +
                                            "}" +
                                        "]," +
                                        "\"encounter\": {" +
                                            "\"reference\": \"Encounter/"+tbSpO2.getValueAt(i,9).toString()+"\"," +
                                            "\"display\": \"Pemeriksaan Fisik SpO2 "+tbSpO2.getValueAt(i,4).toString()+" Pada Tanggal "+tbSpO2.getValueAt(i,13).toString()+" Jam "+tbSpO2.getValueAt(i,14).toString()+"\"" +
                                        "}," +
                                        "\"effectiveDateTime\": \""+tbSpO2.getValueAt(i,13).toString()+"T"+tbSpO2.getValueAt(i,14).toString()+"+07:00\"," +
                                        "\"valueQuantity\": {" +
                                            "\"value\": "+tbSpO2.getValueAt(i,10).toString().replaceAll(",",".")+"," +
                                            "\"unit\": \"percent saturation\"," +
                                            "\"system\": \"http://unitsofmeasure.org\"," +
                                            "\"code\": \"%\"" +
                                        "}" +
                                   "}";
                            System.out.println("URL : "+link+"/Observation/"+tbSpO2.getValueAt(i,15).toString());
                            System.out.println("Request JSON : "+json);
                            requestEntity = new HttpEntity(json,headers);
                            json=api.getRest().exchange(link+"/Observation/"+tbSpO2.getValueAt(i,15).toString(), HttpMethod.PUT, requestEntity, String.class).getBody();
                            System.out.println("Result JSON : "+json);
                        }catch(Exception e){
                            System.out.println("Notifikasi Bridging : "+e);
                        }
                    } catch (Exception e) {
                        System.out.println("Notifikasi : "+e);
                    }
                }
            }
            tampilspo2();
        }else if(TabRawat.getSelectedIndex()==4){
            for(i=0;i<tbGCS.getRowCount();i++){
                if(tbGCS.getValueAt(i,0).toString().equals("true")&&(!tbGCS.getValueAt(i,5).toString().equals(""))&&(!tbGCS.getValueAt(i,12).toString().equals(""))&&(!tbGCS.getValueAt(i,15).toString().equals(""))){
                    try {
                        iddokter=cekViaSatuSehat.tampilIDParktisi(tbGCS.getValueAt(i,12).toString());
                        idpasien=cekViaSatuSehat.tampilIDPasien(tbGCS.getValueAt(i,5).toString());
                        try{
                            headers = new HttpHeaders();
                            headers.setContentType(MediaType.APPLICATION_JSON);
                            headers.add("Authorization", "Bearer "+api.TokenSatuSehat());
                            json = "{" +
                                        "\"resourceType\": \"Observation\"," +
                                        "\"id\": \""+tbGCS.getValueAt(i,15).toString()+"\"," +
                                        "\"status\": \"final\"," +
                                        "\"category\": [" +
                                            "{" +
                                                "\"coding\": [" +
                                                    "{" +
                                                        "\"system\": \"http://terminology.hl7.org/CodeSystem/observation-category\"," +
                                                        "\"code\": \"vital-signs\"," +
                                                        "\"display\": \"Vital Signs\"" +
                                                    "}" +
                                                "]" +
                                            "}" +
                                        "]," +
                                        "\"code\": {" +
                                            "\"coding\": [" +
                                                "{" +
                                                    "\"system\": \"http://loinc.org\"," +
                                                    "\"code\": \"9269-2\"," +
                                                    "\"display\": \"Glasgow coma score total\"" +
                                                "}" +
                                            "]" +
                                        "}," +
                                        "\"subject\": {" +
                                            "\"reference\": \"Patient/"+idpasien+"\"" +
                                        "}," +
                                        "\"performer\": [" +
                                            "{" +
                                                "\"reference\": \"Practitioner/"+iddokter+"\"" +
                                            "}" +
                                        "]," +
                                        "\"encounter\": {" +
                                            "\"reference\": \"Encounter/"+tbGCS.getValueAt(i,9).toString()+"\"," +
                                            "\"display\": \"Pemeriksaan Fisik GCS "+tbGCS.getValueAt(i,4).toString()+" Pada Tanggal "+tbGCS.getValueAt(i,13).toString()+" Jam "+tbGCS.getValueAt(i,14).toString()+"\"" +
                                        "}," +
                                        "\"effectiveDateTime\": \""+tbGCS.getValueAt(i,13).toString()+"T"+tbGCS.getValueAt(i,14).toString()+"+07:00\"," +
                                        "\"valueQuantity\": {" +
                                            "\"value\": "+tbGCS.getValueAt(i,10).toString().replaceAll(",",".")+"," +
                                            "\"system\": \"http://unitsofmeasure.org\"," +
                                            "\"code\": \"{score}\"" +
                                        "}" +
                                   "}";
                            System.out.println("URL : "+link+"/Observation/"+tbGCS.getValueAt(i,15).toString());
                            System.out.println("Request JSON : "+json);
                            requestEntity = new HttpEntity(json,headers);
                            json=api.getRest().exchange(link+"/Observation/"+tbGCS.getValueAt(i,15).toString(), HttpMethod.PUT, requestEntity, String.class).getBody();
                            System.out.println("Result JSON : "+json);
                        }catch(Exception e){
                            System.out.println("Notifikasi Bridging : "+e);
                        }
                    } catch (Exception e) {
                        System.out.println("Notifikasi : "+e);
                    }
                }
            }
            tampilgcs();
        }
    }//GEN-LAST:event_BtnUpdateActionPerformed

    private void TabRawatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabRawatMouseClicked
        if(TabRawat.getSelectedIndex()==0){
            tampilsuhu();
        }else if(TabRawat.getSelectedIndex()==1){
            tampilrespirasi();
        }else if(TabRawat.getSelectedIndex()==2){
            tampilnadi();
        }else if(TabRawat.getSelectedIndex()==3){
            tampilspo2();
        }else if(TabRawat.getSelectedIndex()==4){
            tampilgcs();
        }
    }//GEN-LAST:event_TabRawatMouseClicked

    private void BtnAll1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAll1ActionPerformed
        TCari.setText("");
        TabRawatMouseClicked(null);
    }//GEN-LAST:event_BtnAll1ActionPerformed

    private void BtnAll1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAll1KeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            TCari.setText("");
            tampilsuhu();
        }else{
            Valid.pindah(evt, BtnPrint, BtnKeluar);
        }
    }//GEN-LAST:event_BtnAll1KeyPressed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            SatuSehatKirimObservationTTV dialog = new SatuSehatKirimObservationTTV(new javax.swing.JFrame(), true);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private widget.Button BtnAll;
    private widget.Button BtnAll1;
    private widget.Button BtnCari;
    private widget.Button BtnKeluar;
    private widget.Button BtnKirim;
    private widget.Button BtnPrint;
    private widget.Button BtnUpdate;
    private widget.Tanggal DTPCari1;
    private widget.Tanggal DTPCari2;
    private widget.Label LCount;
    private widget.ScrollPane Scroll;
    private widget.ScrollPane Scroll1;
    private widget.ScrollPane Scroll2;
    private widget.ScrollPane Scroll3;
    private widget.ScrollPane Scroll4;
    private widget.ScrollPane Scroll5;
    private widget.ScrollPane Scroll6;
    private widget.TextBox TCari;
    private javax.swing.JTabbedPane TabRawat;
    private widget.InternalFrame internalFrame1;
    private widget.Label jLabel15;
    private widget.Label jLabel16;
    private widget.Label jLabel17;
    private widget.Label jLabel7;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPopupMenu jPopupMenu1;
    private widget.panelisi panelGlass8;
    private widget.panelisi panelGlass9;
    private javax.swing.JMenuItem ppBersihkan;
    private javax.swing.JMenuItem ppPilihSemua;
    private widget.Table tbGCS;
    private widget.Table tbKesadaran;
    private widget.Table tbNadi;
    private widget.Table tbRespirasi;
    private widget.Table tbSpO2;
    private widget.Table tbSuhu;
    private widget.Table tbTensi;
    // End of variables declaration//GEN-END:variables
    
    private void tampilsuhu() {
        Valid.tabelKosong(tabModeSuhu);
        try{
            ps=koneksi.prepareStatement(
                   "select reg_periksa.tgl_registrasi,reg_periksa.jam_reg,reg_periksa.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,pasien.no_ktp,"+
                   "reg_periksa.stts,DATE_FORMAT(tagihan_sadewa.tgl_bayar,'%Y-%m-%d %H:%i:%s') as pulang,satu_sehat_encounter.id_encounter,"+
                   "pegawai.nama,pegawai.no_ktp as ktppraktisi,pemeriksaan_ralan.tgl_perawatan,pemeriksaan_ralan.jam_rawat,pemeriksaan_ralan.suhu_tubuh, "+
                   "ifnull(satu_sehat_observationttvsuhu.id_observation,'') as satu_sehat_observationttvsuhu from reg_periksa inner join pasien "+
                   "on reg_periksa.no_rkm_medis=pasien.no_rkm_medis inner join tagihan_sadewa on tagihan_sadewa.no_nota=reg_periksa.no_rawat "+
                   "inner join satu_sehat_encounter on satu_sehat_encounter.no_rawat=reg_periksa.no_rawat inner join pemeriksaan_ralan on pemeriksaan_ralan.no_rawat=reg_periksa.no_rawat "+
                   "inner join pegawai on pemeriksaan_ralan.nip=pegawai.nik left join satu_sehat_observationttvsuhu on satu_sehat_observationttvsuhu.no_rawat=pemeriksaan_ralan.no_rawat "+
                   "and satu_sehat_observationttvsuhu.tgl_perawatan=pemeriksaan_ralan.tgl_perawatan and satu_sehat_observationttvsuhu.jam_rawat=pemeriksaan_ralan.jam_rawat "+
                   "and satu_sehat_observationttvsuhu.status='Ralan' where pemeriksaan_ralan.suhu_tubuh<>'' and reg_periksa.tgl_registrasi between ? and ? "+
                   (TCari.getText().equals("")?"":"and (reg_periksa.no_rawat like ? or reg_periksa.no_rkm_medis like ? or "+
                   "pasien.nm_pasien like ? or pasien.no_ktp like ? or pegawai.no_ktp like ? or pegawai.nama like ? or "+
                   "reg_periksa.stts like ?)")+" order by reg_periksa.tgl_registrasi,reg_periksa.jam_reg,reg_periksa.no_rawat,pemeriksaan_ralan.tgl_perawatan,pemeriksaan_ralan.jam_rawat");
            try {
                ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                if(!TCari.getText().equals("")){
                    ps.setString(3,"%"+TCari.getText()+"%");
                    ps.setString(4,"%"+TCari.getText()+"%");
                    ps.setString(5,"%"+TCari.getText()+"%");
                    ps.setString(6,"%"+TCari.getText()+"%");
                    ps.setString(7,"%"+TCari.getText()+"%");
                    ps.setString(8,"%"+TCari.getText()+"%");
                    ps.setString(9,"%"+TCari.getText()+"%");
                }
                rs=ps.executeQuery();
                while(rs.next()){
                    tabModeSuhu.addRow(new Object[]{
                        false,rs.getString("tgl_registrasi")+" "+rs.getString("jam_reg"),rs.getString("no_rawat"),rs.getString("no_rkm_medis"),rs.getString("nm_pasien"),
                        rs.getString("no_ktp"),rs.getString("stts"),"Ralan",rs.getString("pulang"),rs.getString("id_encounter"),rs.getString("suhu_tubuh"),
                        rs.getString("nama"),rs.getString("ktppraktisi"),rs.getString("tgl_perawatan"),rs.getString("jam_rawat"),rs.getString("satu_sehat_observationttvsuhu")
                    });
                }
            } catch (Exception e) {
                System.out.println("Notif : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }
            
            ps=koneksi.prepareStatement(
                   "select reg_periksa.tgl_registrasi,reg_periksa.jam_reg,reg_periksa.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,pasien.no_ktp,"+
                   "reg_periksa.stts,DATE_FORMAT(tagihan_sadewa.tgl_bayar,'%Y-%m-%d %H:%i:%s') as pulang,satu_sehat_encounter.id_encounter,"+
                   "pegawai.nama,pegawai.no_ktp as ktppraktisi,pemeriksaan_ranap.tgl_perawatan,pemeriksaan_ranap.jam_rawat,pemeriksaan_ranap.suhu_tubuh, "+
                   "ifnull(satu_sehat_observationttvsuhu.id_observation,'') as satu_sehat_observationttvsuhu from reg_periksa inner join pasien "+
                   "on reg_periksa.no_rkm_medis=pasien.no_rkm_medis inner join tagihan_sadewa on tagihan_sadewa.no_nota=reg_periksa.no_rawat "+
                   "inner join satu_sehat_encounter on satu_sehat_encounter.no_rawat=reg_periksa.no_rawat inner join pemeriksaan_ranap on pemeriksaan_ranap.no_rawat=reg_periksa.no_rawat "+
                   "inner join pegawai on pemeriksaan_ranap.nip=pegawai.nik left join satu_sehat_observationttvsuhu on satu_sehat_observationttvsuhu.no_rawat=pemeriksaan_ranap.no_rawat "+
                   "and satu_sehat_observationttvsuhu.tgl_perawatan=pemeriksaan_ranap.tgl_perawatan and satu_sehat_observationttvsuhu.jam_rawat=pemeriksaan_ranap.jam_rawat "+
                   "and satu_sehat_observationttvsuhu.status='Ranap' where pemeriksaan_ranap.suhu_tubuh<>'' and reg_periksa.tgl_registrasi between ? and ? "+
                   (TCari.getText().equals("")?"":"and (reg_periksa.no_rawat like ? or reg_periksa.no_rkm_medis like ? or "+
                   "pasien.nm_pasien like ? or pasien.no_ktp like ? or pegawai.no_ktp like ? or pegawai.nama like ? or "+
                   "reg_periksa.stts like ?)")+" order by reg_periksa.tgl_registrasi,reg_periksa.jam_reg,reg_periksa.no_rawat,pemeriksaan_ranap.tgl_perawatan,pemeriksaan_ranap.jam_rawat");
            try {
                ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                if(!TCari.getText().equals("")){
                    ps.setString(3,"%"+TCari.getText()+"%");
                    ps.setString(4,"%"+TCari.getText()+"%");
                    ps.setString(5,"%"+TCari.getText()+"%");
                    ps.setString(6,"%"+TCari.getText()+"%");
                    ps.setString(7,"%"+TCari.getText()+"%");
                    ps.setString(8,"%"+TCari.getText()+"%");
                    ps.setString(9,"%"+TCari.getText()+"%");
                }
                rs=ps.executeQuery();
                while(rs.next()){
                    tabModeSuhu.addRow(new Object[]{
                        false,rs.getString("tgl_registrasi")+" "+rs.getString("jam_reg"),rs.getString("no_rawat"),rs.getString("no_rkm_medis"),rs.getString("nm_pasien"),
                        rs.getString("no_ktp"),rs.getString("stts"),"Ralan",rs.getString("pulang"),rs.getString("id_encounter"),rs.getString("suhu_tubuh"),
                        rs.getString("nama"),rs.getString("ktppraktisi"),rs.getString("tgl_perawatan"),rs.getString("jam_rawat"),rs.getString("satu_sehat_observationttvsuhu")
                    });
                }
            } catch (Exception e) {
                System.out.println("Notif : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
        LCount.setText(""+tabModeSuhu.getRowCount());
    }

    private void tampilrespirasi() {
        Valid.tabelKosong(tabModeRespirasi);
        try{
            ps=koneksi.prepareStatement(
                   "select reg_periksa.tgl_registrasi,reg_periksa.jam_reg,reg_periksa.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,pasien.no_ktp,"+
                   "reg_periksa.stts,DATE_FORMAT(tagihan_sadewa.tgl_bayar,'%Y-%m-%d %H:%i:%s') as pulang,satu_sehat_encounter.id_encounter,"+
                   "pegawai.nama,pegawai.no_ktp as ktppraktisi,pemeriksaan_ralan.tgl_perawatan,pemeriksaan_ralan.jam_rawat,pemeriksaan_ralan.respirasi, "+
                   "ifnull(satu_sehat_observationttvrespirasi.id_observation,'') as satu_sehat_observationttvrespirasi from reg_periksa inner join pasien "+
                   "on reg_periksa.no_rkm_medis=pasien.no_rkm_medis inner join tagihan_sadewa on tagihan_sadewa.no_nota=reg_periksa.no_rawat "+
                   "inner join satu_sehat_encounter on satu_sehat_encounter.no_rawat=reg_periksa.no_rawat inner join pemeriksaan_ralan on pemeriksaan_ralan.no_rawat=reg_periksa.no_rawat "+
                   "inner join pegawai on pemeriksaan_ralan.nip=pegawai.nik left join satu_sehat_observationttvrespirasi on satu_sehat_observationttvrespirasi.no_rawat=pemeriksaan_ralan.no_rawat "+
                   "and satu_sehat_observationttvrespirasi.tgl_perawatan=pemeriksaan_ralan.tgl_perawatan and satu_sehat_observationttvrespirasi.jam_rawat=pemeriksaan_ralan.jam_rawat "+
                   "and satu_sehat_observationttvrespirasi.status='Ralan' where pemeriksaan_ralan.respirasi<>'' and reg_periksa.tgl_registrasi between ? and ? "+
                   (TCari.getText().equals("")?"":"and (reg_periksa.no_rawat like ? or reg_periksa.no_rkm_medis like ? or "+
                   "pasien.nm_pasien like ? or pasien.no_ktp like ? or pegawai.no_ktp like ? or pegawai.nama like ? or "+
                   "reg_periksa.stts like ?)")+" order by reg_periksa.tgl_registrasi,reg_periksa.jam_reg,reg_periksa.no_rawat,pemeriksaan_ralan.tgl_perawatan,pemeriksaan_ralan.jam_rawat");
            try {
                ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                if(!TCari.getText().equals("")){
                    ps.setString(3,"%"+TCari.getText()+"%");
                    ps.setString(4,"%"+TCari.getText()+"%");
                    ps.setString(5,"%"+TCari.getText()+"%");
                    ps.setString(6,"%"+TCari.getText()+"%");
                    ps.setString(7,"%"+TCari.getText()+"%");
                    ps.setString(8,"%"+TCari.getText()+"%");
                    ps.setString(9,"%"+TCari.getText()+"%");
                }
                rs=ps.executeQuery();
                while(rs.next()){
                    tabModeRespirasi.addRow(new Object[]{
                        false,rs.getString("tgl_registrasi")+" "+rs.getString("jam_reg"),rs.getString("no_rawat"),rs.getString("no_rkm_medis"),rs.getString("nm_pasien"),
                        rs.getString("no_ktp"),rs.getString("stts"),"Ralan",rs.getString("pulang"),rs.getString("id_encounter"),rs.getString("respirasi"),
                        rs.getString("nama"),rs.getString("ktppraktisi"),rs.getString("tgl_perawatan"),rs.getString("jam_rawat"),rs.getString("satu_sehat_observationttvrespirasi")
                    });
                }
            } catch (Exception e) {
                System.out.println("Notif : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }
            
            ps=koneksi.prepareStatement(
                   "select reg_periksa.tgl_registrasi,reg_periksa.jam_reg,reg_periksa.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,pasien.no_ktp,"+
                   "reg_periksa.stts,DATE_FORMAT(tagihan_sadewa.tgl_bayar,'%Y-%m-%d %H:%i:%s') as pulang,satu_sehat_encounter.id_encounter,"+
                   "pegawai.nama,pegawai.no_ktp as ktppraktisi,pemeriksaan_ranap.tgl_perawatan,pemeriksaan_ranap.jam_rawat,pemeriksaan_ranap.respirasi, "+
                   "ifnull(satu_sehat_observationttvrespirasi.id_observation,'') as satu_sehat_observationttvrespirasi from reg_periksa inner join pasien "+
                   "on reg_periksa.no_rkm_medis=pasien.no_rkm_medis inner join tagihan_sadewa on tagihan_sadewa.no_nota=reg_periksa.no_rawat "+
                   "inner join satu_sehat_encounter on satu_sehat_encounter.no_rawat=reg_periksa.no_rawat inner join pemeriksaan_ranap on pemeriksaan_ranap.no_rawat=reg_periksa.no_rawat "+
                   "inner join pegawai on pemeriksaan_ranap.nip=pegawai.nik left join satu_sehat_observationttvrespirasi on satu_sehat_observationttvrespirasi.no_rawat=pemeriksaan_ranap.no_rawat "+
                   "and satu_sehat_observationttvrespirasi.tgl_perawatan=pemeriksaan_ranap.tgl_perawatan and satu_sehat_observationttvrespirasi.jam_rawat=pemeriksaan_ranap.jam_rawat "+
                   "and satu_sehat_observationttvrespirasi.status='Ranap' where pemeriksaan_ranap.respirasi<>'' and reg_periksa.tgl_registrasi between ? and ? "+
                   (TCari.getText().equals("")?"":"and (reg_periksa.no_rawat like ? or reg_periksa.no_rkm_medis like ? or "+
                   "pasien.nm_pasien like ? or pasien.no_ktp like ? or pegawai.no_ktp like ? or pegawai.nama like ? or "+
                   "reg_periksa.stts like ?)")+" order by reg_periksa.tgl_registrasi,reg_periksa.jam_reg,reg_periksa.no_rawat,pemeriksaan_ranap.tgl_perawatan,pemeriksaan_ranap.jam_rawat");
            try {
                ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                if(!TCari.getText().equals("")){
                    ps.setString(3,"%"+TCari.getText()+"%");
                    ps.setString(4,"%"+TCari.getText()+"%");
                    ps.setString(5,"%"+TCari.getText()+"%");
                    ps.setString(6,"%"+TCari.getText()+"%");
                    ps.setString(7,"%"+TCari.getText()+"%");
                    ps.setString(8,"%"+TCari.getText()+"%");
                    ps.setString(9,"%"+TCari.getText()+"%");
                }
                rs=ps.executeQuery();
                while(rs.next()){
                    tabModeRespirasi.addRow(new Object[]{
                        false,rs.getString("tgl_registrasi")+" "+rs.getString("jam_reg"),rs.getString("no_rawat"),rs.getString("no_rkm_medis"),rs.getString("nm_pasien"),
                        rs.getString("no_ktp"),rs.getString("stts"),"Ralan",rs.getString("pulang"),rs.getString("id_encounter"),rs.getString("respirasi"),
                        rs.getString("nama"),rs.getString("ktppraktisi"),rs.getString("tgl_perawatan"),rs.getString("jam_rawat"),rs.getString("satu_sehat_observationttvrespirasi")
                    });
                }
            } catch (Exception e) {
                System.out.println("Notif : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
        LCount.setText(""+tabModeRespirasi.getRowCount());
    }
    
    private void tampilnadi() {
        Valid.tabelKosong(tabModeNadi);
        try{
            ps=koneksi.prepareStatement(
                   "select reg_periksa.tgl_registrasi,reg_periksa.jam_reg,reg_periksa.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,pasien.no_ktp,"+
                   "reg_periksa.stts,DATE_FORMAT(tagihan_sadewa.tgl_bayar,'%Y-%m-%d %H:%i:%s') as pulang,satu_sehat_encounter.id_encounter,"+
                   "pegawai.nama,pegawai.no_ktp as ktppraktisi,pemeriksaan_ralan.tgl_perawatan,pemeriksaan_ralan.jam_rawat,pemeriksaan_ralan.nadi, "+
                   "ifnull(satu_sehat_observationttvnadi.id_observation,'') as satu_sehat_observationttvnadi from reg_periksa inner join pasien "+
                   "on reg_periksa.no_rkm_medis=pasien.no_rkm_medis inner join tagihan_sadewa on tagihan_sadewa.no_nota=reg_periksa.no_rawat "+
                   "inner join satu_sehat_encounter on satu_sehat_encounter.no_rawat=reg_periksa.no_rawat inner join pemeriksaan_ralan on pemeriksaan_ralan.no_rawat=reg_periksa.no_rawat "+
                   "inner join pegawai on pemeriksaan_ralan.nip=pegawai.nik left join satu_sehat_observationttvnadi on satu_sehat_observationttvnadi.no_rawat=pemeriksaan_ralan.no_rawat "+
                   "and satu_sehat_observationttvnadi.tgl_perawatan=pemeriksaan_ralan.tgl_perawatan and satu_sehat_observationttvnadi.jam_rawat=pemeriksaan_ralan.jam_rawat "+
                   "and satu_sehat_observationttvnadi.status='Ralan' where pemeriksaan_ralan.nadi<>'' and reg_periksa.tgl_registrasi between ? and ? "+
                   (TCari.getText().equals("")?"":"and (reg_periksa.no_rawat like ? or reg_periksa.no_rkm_medis like ? or "+
                   "pasien.nm_pasien like ? or pasien.no_ktp like ? or pegawai.no_ktp like ? or pegawai.nama like ? or "+
                   "reg_periksa.stts like ?)")+" order by reg_periksa.tgl_registrasi,reg_periksa.jam_reg,reg_periksa.no_rawat,pemeriksaan_ralan.tgl_perawatan,pemeriksaan_ralan.jam_rawat");
            try {
                ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                if(!TCari.getText().equals("")){
                    ps.setString(3,"%"+TCari.getText()+"%");
                    ps.setString(4,"%"+TCari.getText()+"%");
                    ps.setString(5,"%"+TCari.getText()+"%");
                    ps.setString(6,"%"+TCari.getText()+"%");
                    ps.setString(7,"%"+TCari.getText()+"%");
                    ps.setString(8,"%"+TCari.getText()+"%");
                    ps.setString(9,"%"+TCari.getText()+"%");
                }
                rs=ps.executeQuery();
                while(rs.next()){
                    tabModeNadi.addRow(new Object[]{
                        false,rs.getString("tgl_registrasi")+" "+rs.getString("jam_reg"),rs.getString("no_rawat"),rs.getString("no_rkm_medis"),rs.getString("nm_pasien"),
                        rs.getString("no_ktp"),rs.getString("stts"),"Ralan",rs.getString("pulang"),rs.getString("id_encounter"),rs.getString("nadi"),
                        rs.getString("nama"),rs.getString("ktppraktisi"),rs.getString("tgl_perawatan"),rs.getString("jam_rawat"),rs.getString("satu_sehat_observationttvnadi")
                    });
                }
            } catch (Exception e) {
                System.out.println("Notif : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }
            
            ps=koneksi.prepareStatement(
                   "select reg_periksa.tgl_registrasi,reg_periksa.jam_reg,reg_periksa.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,pasien.no_ktp,"+
                   "reg_periksa.stts,DATE_FORMAT(tagihan_sadewa.tgl_bayar,'%Y-%m-%d %H:%i:%s') as pulang,satu_sehat_encounter.id_encounter,"+
                   "pegawai.nama,pegawai.no_ktp as ktppraktisi,pemeriksaan_ranap.tgl_perawatan,pemeriksaan_ranap.jam_rawat,pemeriksaan_ranap.nadi, "+
                   "ifnull(satu_sehat_observationttvnadi.id_observation,'') as satu_sehat_observationttvnadi from reg_periksa inner join pasien "+
                   "on reg_periksa.no_rkm_medis=pasien.no_rkm_medis inner join tagihan_sadewa on tagihan_sadewa.no_nota=reg_periksa.no_rawat "+
                   "inner join satu_sehat_encounter on satu_sehat_encounter.no_rawat=reg_periksa.no_rawat inner join pemeriksaan_ranap on pemeriksaan_ranap.no_rawat=reg_periksa.no_rawat "+
                   "inner join pegawai on pemeriksaan_ranap.nip=pegawai.nik left join satu_sehat_observationttvnadi on satu_sehat_observationttvnadi.no_rawat=pemeriksaan_ranap.no_rawat "+
                   "and satu_sehat_observationttvnadi.tgl_perawatan=pemeriksaan_ranap.tgl_perawatan and satu_sehat_observationttvnadi.jam_rawat=pemeriksaan_ranap.jam_rawat "+
                   "and satu_sehat_observationttvnadi.status='Ranap' where pemeriksaan_ranap.nadi<>'' and reg_periksa.tgl_registrasi between ? and ? "+
                   (TCari.getText().equals("")?"":"and (reg_periksa.no_rawat like ? or reg_periksa.no_rkm_medis like ? or "+
                   "pasien.nm_pasien like ? or pasien.no_ktp like ? or pegawai.no_ktp like ? or pegawai.nama like ? or "+
                   "reg_periksa.stts like ?)")+" order by reg_periksa.tgl_registrasi,reg_periksa.jam_reg,reg_periksa.no_rawat,pemeriksaan_ranap.tgl_perawatan,pemeriksaan_ranap.jam_rawat");
            try {
                ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                if(!TCari.getText().equals("")){
                    ps.setString(3,"%"+TCari.getText()+"%");
                    ps.setString(4,"%"+TCari.getText()+"%");
                    ps.setString(5,"%"+TCari.getText()+"%");
                    ps.setString(6,"%"+TCari.getText()+"%");
                    ps.setString(7,"%"+TCari.getText()+"%");
                    ps.setString(8,"%"+TCari.getText()+"%");
                    ps.setString(9,"%"+TCari.getText()+"%");
                }
                rs=ps.executeQuery();
                while(rs.next()){
                    tabModeNadi.addRow(new Object[]{
                        false,rs.getString("tgl_registrasi")+" "+rs.getString("jam_reg"),rs.getString("no_rawat"),rs.getString("no_rkm_medis"),rs.getString("nm_pasien"),
                        rs.getString("no_ktp"),rs.getString("stts"),"Ralan",rs.getString("pulang"),rs.getString("id_encounter"),rs.getString("nadi"),
                        rs.getString("nama"),rs.getString("ktppraktisi"),rs.getString("tgl_perawatan"),rs.getString("jam_rawat"),rs.getString("satu_sehat_observationttvnadi")
                    });
                }
            } catch (Exception e) {
                System.out.println("Notif : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
        LCount.setText(""+tabModeNadi.getRowCount());
    }
    
    private void tampilspo2() {
        Valid.tabelKosong(tabModeSpO2);
        try{
            ps=koneksi.prepareStatement(
                   "select reg_periksa.tgl_registrasi,reg_periksa.jam_reg,reg_periksa.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,pasien.no_ktp,"+
                   "reg_periksa.stts,DATE_FORMAT(tagihan_sadewa.tgl_bayar,'%Y-%m-%d %H:%i:%s') as pulang,satu_sehat_encounter.id_encounter,"+
                   "pegawai.nama,pegawai.no_ktp as ktppraktisi,pemeriksaan_ralan.tgl_perawatan,pemeriksaan_ralan.jam_rawat,pemeriksaan_ralan.spo2, "+
                   "ifnull(satu_sehat_observationttvspo2.id_observation,'') as satu_sehat_observationttvspo2 from reg_periksa inner join pasien "+
                   "on reg_periksa.no_rkm_medis=pasien.no_rkm_medis inner join tagihan_sadewa on tagihan_sadewa.no_nota=reg_periksa.no_rawat "+
                   "inner join satu_sehat_encounter on satu_sehat_encounter.no_rawat=reg_periksa.no_rawat inner join pemeriksaan_ralan on pemeriksaan_ralan.no_rawat=reg_periksa.no_rawat "+
                   "inner join pegawai on pemeriksaan_ralan.nip=pegawai.nik left join satu_sehat_observationttvspo2 on satu_sehat_observationttvspo2.no_rawat=pemeriksaan_ralan.no_rawat "+
                   "and satu_sehat_observationttvspo2.tgl_perawatan=pemeriksaan_ralan.tgl_perawatan and satu_sehat_observationttvspo2.jam_rawat=pemeriksaan_ralan.jam_rawat "+
                   "and satu_sehat_observationttvspo2.status='Ralan' where pemeriksaan_ralan.spo2<>'' and reg_periksa.tgl_registrasi between ? and ? "+
                   (TCari.getText().equals("")?"":"and (reg_periksa.no_rawat like ? or reg_periksa.no_rkm_medis like ? or "+
                   "pasien.nm_pasien like ? or pasien.no_ktp like ? or pegawai.no_ktp like ? or pegawai.nama like ? or "+
                   "reg_periksa.stts like ?)")+" order by reg_periksa.tgl_registrasi,reg_periksa.jam_reg,reg_periksa.no_rawat,pemeriksaan_ralan.tgl_perawatan,pemeriksaan_ralan.jam_rawat");
            try {
                ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                if(!TCari.getText().equals("")){
                    ps.setString(3,"%"+TCari.getText()+"%");
                    ps.setString(4,"%"+TCari.getText()+"%");
                    ps.setString(5,"%"+TCari.getText()+"%");
                    ps.setString(6,"%"+TCari.getText()+"%");
                    ps.setString(7,"%"+TCari.getText()+"%");
                    ps.setString(8,"%"+TCari.getText()+"%");
                    ps.setString(9,"%"+TCari.getText()+"%");
                }
                rs=ps.executeQuery();
                while(rs.next()){
                    tabModeSpO2.addRow(new Object[]{
                        false,rs.getString("tgl_registrasi")+" "+rs.getString("jam_reg"),rs.getString("no_rawat"),rs.getString("no_rkm_medis"),rs.getString("nm_pasien"),
                        rs.getString("no_ktp"),rs.getString("stts"),"Ralan",rs.getString("pulang"),rs.getString("id_encounter"),rs.getString("spo2"),
                        rs.getString("nama"),rs.getString("ktppraktisi"),rs.getString("tgl_perawatan"),rs.getString("jam_rawat"),rs.getString("satu_sehat_observationttvspo2")
                    });
                }
            } catch (Exception e) {
                System.out.println("Notif : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }
            
            ps=koneksi.prepareStatement(
                   "select reg_periksa.tgl_registrasi,reg_periksa.jam_reg,reg_periksa.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,pasien.no_ktp,"+
                   "reg_periksa.stts,DATE_FORMAT(tagihan_sadewa.tgl_bayar,'%Y-%m-%d %H:%i:%s') as pulang,satu_sehat_encounter.id_encounter,"+
                   "pegawai.nama,pegawai.no_ktp as ktppraktisi,pemeriksaan_ranap.tgl_perawatan,pemeriksaan_ranap.jam_rawat,pemeriksaan_ranap.spo2, "+
                   "ifnull(satu_sehat_observationttvspo2.id_observation,'') as satu_sehat_observationttvspo2 from reg_periksa inner join pasien "+
                   "on reg_periksa.no_rkm_medis=pasien.no_rkm_medis inner join tagihan_sadewa on tagihan_sadewa.no_nota=reg_periksa.no_rawat "+
                   "inner join satu_sehat_encounter on satu_sehat_encounter.no_rawat=reg_periksa.no_rawat inner join pemeriksaan_ranap on pemeriksaan_ranap.no_rawat=reg_periksa.no_rawat "+
                   "inner join pegawai on pemeriksaan_ranap.nip=pegawai.nik left join satu_sehat_observationttvspo2 on satu_sehat_observationttvspo2.no_rawat=pemeriksaan_ranap.no_rawat "+
                   "and satu_sehat_observationttvspo2.tgl_perawatan=pemeriksaan_ranap.tgl_perawatan and satu_sehat_observationttvspo2.jam_rawat=pemeriksaan_ranap.jam_rawat "+
                   "and satu_sehat_observationttvspo2.status='Ranap' where pemeriksaan_ranap.spo2<>'' and reg_periksa.tgl_registrasi between ? and ? "+
                   (TCari.getText().equals("")?"":"and (reg_periksa.no_rawat like ? or reg_periksa.no_rkm_medis like ? or "+
                   "pasien.nm_pasien like ? or pasien.no_ktp like ? or pegawai.no_ktp like ? or pegawai.nama like ? or "+
                   "reg_periksa.stts like ?)")+" order by reg_periksa.tgl_registrasi,reg_periksa.jam_reg,reg_periksa.no_rawat,pemeriksaan_ranap.tgl_perawatan,pemeriksaan_ranap.jam_rawat");
            try {
                ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                if(!TCari.getText().equals("")){
                    ps.setString(3,"%"+TCari.getText()+"%");
                    ps.setString(4,"%"+TCari.getText()+"%");
                    ps.setString(5,"%"+TCari.getText()+"%");
                    ps.setString(6,"%"+TCari.getText()+"%");
                    ps.setString(7,"%"+TCari.getText()+"%");
                    ps.setString(8,"%"+TCari.getText()+"%");
                    ps.setString(9,"%"+TCari.getText()+"%");
                }
                rs=ps.executeQuery();
                while(rs.next()){
                    tabModeSpO2.addRow(new Object[]{
                        false,rs.getString("tgl_registrasi")+" "+rs.getString("jam_reg"),rs.getString("no_rawat"),rs.getString("no_rkm_medis"),rs.getString("nm_pasien"),
                        rs.getString("no_ktp"),rs.getString("stts"),"Ralan",rs.getString("pulang"),rs.getString("id_encounter"),rs.getString("spo2"),
                        rs.getString("nama"),rs.getString("ktppraktisi"),rs.getString("tgl_perawatan"),rs.getString("jam_rawat"),rs.getString("satu_sehat_observationttvspo2")
                    });
                }
            } catch (Exception e) {
                System.out.println("Notif : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
        LCount.setText(""+tabModeSpO2.getRowCount());
    }
    
    private void tampilgcs() {
        Valid.tabelKosong(tabModeGCS);
        try{
            ps=koneksi.prepareStatement(
                   "select reg_periksa.tgl_registrasi,reg_periksa.jam_reg,reg_periksa.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,pasien.no_ktp,"+
                   "reg_periksa.stts,DATE_FORMAT(tagihan_sadewa.tgl_bayar,'%Y-%m-%d %H:%i:%s') as pulang,satu_sehat_encounter.id_encounter,"+
                   "pegawai.nama,pegawai.no_ktp as ktppraktisi,pemeriksaan_ralan.tgl_perawatan,pemeriksaan_ralan.jam_rawat,pemeriksaan_ralan.gcs, "+
                   "ifnull(satu_sehat_observationttvgcs.id_observation,'') as satu_sehat_observationttvgcs from reg_periksa inner join pasien "+
                   "on reg_periksa.no_rkm_medis=pasien.no_rkm_medis inner join tagihan_sadewa on tagihan_sadewa.no_nota=reg_periksa.no_rawat "+
                   "inner join satu_sehat_encounter on satu_sehat_encounter.no_rawat=reg_periksa.no_rawat inner join pemeriksaan_ralan on pemeriksaan_ralan.no_rawat=reg_periksa.no_rawat "+
                   "inner join pegawai on pemeriksaan_ralan.nip=pegawai.nik left join satu_sehat_observationttvgcs on satu_sehat_observationttvgcs.no_rawat=pemeriksaan_ralan.no_rawat "+
                   "and satu_sehat_observationttvgcs.tgl_perawatan=pemeriksaan_ralan.tgl_perawatan and satu_sehat_observationttvgcs.jam_rawat=pemeriksaan_ralan.jam_rawat "+
                   "and satu_sehat_observationttvgcs.status='Ralan' where pemeriksaan_ralan.gcs<>'' and reg_periksa.tgl_registrasi between ? and ? "+
                   (TCari.getText().equals("")?"":"and (reg_periksa.no_rawat like ? or reg_periksa.no_rkm_medis like ? or "+
                   "pasien.nm_pasien like ? or pasien.no_ktp like ? or pegawai.no_ktp like ? or pegawai.nama like ? or "+
                   "reg_periksa.stts like ?)")+" order by reg_periksa.tgl_registrasi,reg_periksa.jam_reg,reg_periksa.no_rawat,pemeriksaan_ralan.tgl_perawatan,pemeriksaan_ralan.jam_rawat");
            try {
                ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                if(!TCari.getText().equals("")){
                    ps.setString(3,"%"+TCari.getText()+"%");
                    ps.setString(4,"%"+TCari.getText()+"%");
                    ps.setString(5,"%"+TCari.getText()+"%");
                    ps.setString(6,"%"+TCari.getText()+"%");
                    ps.setString(7,"%"+TCari.getText()+"%");
                    ps.setString(8,"%"+TCari.getText()+"%");
                    ps.setString(9,"%"+TCari.getText()+"%");
                }
                rs=ps.executeQuery();
                while(rs.next()){
                    tabModeGCS.addRow(new Object[]{
                        false,rs.getString("tgl_registrasi")+" "+rs.getString("jam_reg"),rs.getString("no_rawat"),rs.getString("no_rkm_medis"),rs.getString("nm_pasien"),
                        rs.getString("no_ktp"),rs.getString("stts"),"Ralan",rs.getString("pulang"),rs.getString("id_encounter"),rs.getString("gcs"),
                        rs.getString("nama"),rs.getString("ktppraktisi"),rs.getString("tgl_perawatan"),rs.getString("jam_rawat"),rs.getString("satu_sehat_observationttvgcs")
                    });
                }
            } catch (Exception e) {
                System.out.println("Notif : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }
            
            ps=koneksi.prepareStatement(
                   "select reg_periksa.tgl_registrasi,reg_periksa.jam_reg,reg_periksa.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,pasien.no_ktp,"+
                   "reg_periksa.stts,DATE_FORMAT(tagihan_sadewa.tgl_bayar,'%Y-%m-%d %H:%i:%s') as pulang,satu_sehat_encounter.id_encounter,"+
                   "pegawai.nama,pegawai.no_ktp as ktppraktisi,pemeriksaan_ranap.tgl_perawatan,pemeriksaan_ranap.jam_rawat,pemeriksaan_ranap.gcs, "+
                   "ifnull(satu_sehat_observationttvgcs.id_observation,'') as satu_sehat_observationttvgcs from reg_periksa inner join pasien "+
                   "on reg_periksa.no_rkm_medis=pasien.no_rkm_medis inner join tagihan_sadewa on tagihan_sadewa.no_nota=reg_periksa.no_rawat "+
                   "inner join satu_sehat_encounter on satu_sehat_encounter.no_rawat=reg_periksa.no_rawat inner join pemeriksaan_ranap on pemeriksaan_ranap.no_rawat=reg_periksa.no_rawat "+
                   "inner join pegawai on pemeriksaan_ranap.nip=pegawai.nik left join satu_sehat_observationttvgcs on satu_sehat_observationttvgcs.no_rawat=pemeriksaan_ranap.no_rawat "+
                   "and satu_sehat_observationttvgcs.tgl_perawatan=pemeriksaan_ranap.tgl_perawatan and satu_sehat_observationttvgcs.jam_rawat=pemeriksaan_ranap.jam_rawat "+
                   "and satu_sehat_observationttvgcs.status='Ranap' where pemeriksaan_ranap.gcs<>'' and reg_periksa.tgl_registrasi between ? and ? "+
                   (TCari.getText().equals("")?"":"and (reg_periksa.no_rawat like ? or reg_periksa.no_rkm_medis like ? or "+
                   "pasien.nm_pasien like ? or pasien.no_ktp like ? or pegawai.no_ktp like ? or pegawai.nama like ? or "+
                   "reg_periksa.stts like ?)")+" order by reg_periksa.tgl_registrasi,reg_periksa.jam_reg,reg_periksa.no_rawat,pemeriksaan_ranap.tgl_perawatan,pemeriksaan_ranap.jam_rawat");
            try {
                ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                if(!TCari.getText().equals("")){
                    ps.setString(3,"%"+TCari.getText()+"%");
                    ps.setString(4,"%"+TCari.getText()+"%");
                    ps.setString(5,"%"+TCari.getText()+"%");
                    ps.setString(6,"%"+TCari.getText()+"%");
                    ps.setString(7,"%"+TCari.getText()+"%");
                    ps.setString(8,"%"+TCari.getText()+"%");
                    ps.setString(9,"%"+TCari.getText()+"%");
                }
                rs=ps.executeQuery();
                while(rs.next()){
                    tabModeGCS.addRow(new Object[]{
                        false,rs.getString("tgl_registrasi")+" "+rs.getString("jam_reg"),rs.getString("no_rawat"),rs.getString("no_rkm_medis"),rs.getString("nm_pasien"),
                        rs.getString("no_ktp"),rs.getString("stts"),"Ralan",rs.getString("pulang"),rs.getString("id_encounter"),rs.getString("gcs"),
                        rs.getString("nama"),rs.getString("ktppraktisi"),rs.getString("tgl_perawatan"),rs.getString("jam_rawat"),rs.getString("satu_sehat_observationttvgcs")
                    });
                }
            } catch (Exception e) {
                System.out.println("Notif : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
        LCount.setText(""+tabModeGCS.getRowCount());
    }
    
    public void isCek(){
        BtnKirim.setEnabled(akses.getsatu_sehat_kirim_observationttv());
        BtnPrint.setEnabled(akses.getsatu_sehat_kirim_observationttv());
    }
    
    public JTable getTable(){
        return tbSuhu;
    }
}

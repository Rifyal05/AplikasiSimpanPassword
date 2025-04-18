/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package app;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import java.awt.Desktop;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Base64;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author rifial
 */
@SuppressWarnings("serial")
public class MainApp extends javax.swing.JFrame {

    private File selectedFile = null;
    private ButtonGroup formatGroup;
    private File outputDirectory;
    private final String DEFAULT_PATH = "C:\\moonproject-app\\data\\FOLDER SERIALIASI\\APLIKASI SIMPAN PASSWORD";
    private final transient Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static class PasswordData implements Serializable {
        private static final long serialVersionUID = 1L;

        private String nama;
        private String platform;
        private String password;

        // Konstruktor
        public PasswordData(String nama, String platform, String password) {
            this.nama = nama;
            this.platform = platform;
            this.password = password;
        }

        // Getters
        public String getNama() {
            return nama;
        }

        public String getPlatform() {
            return platform;
        }

        public String getPassword() {
            return password;
        }

        // Setters
        public void setNama(String nama) {
            this.nama = nama;
        }

        public void setPlatform(String platform) {
            this.platform = platform;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        @Override
        public String toString() {
            return "PasswordData{" + "nama=" + nama + ", platform=" + platform + ", password=" + "[PROTECTED]" + '}';
        }
    }

    /**
     * Creates new form MainApp
     */
    public MainApp() {
        initComponents();
        setupComponents();
        textareaoutput.setEditable(false);
        textareaoutput.setColumns(20);
        textareaoutput.setRows(5);
        jScrollPane2.setViewportView(textareaoutput);
        setLocationRelativeTo(null);
    }

    /**
     * Melakukan serialisasi data ke file sesuai format yang dipilih.
     *
     * @param data Objek PasswordData yang akan diserialisasi.
     * @return File objek yang merujuk ke file yang berhasil disimpan, atau null
     * jika gagal.
     */
    private File serializeToFileHelper(PasswordData data) {
        String format = getSelectedFormat();
        String extension;
        String safePlatformName = data.getPlatform().replaceAll("[^a-zA-Z0-9.-]", "_");
        String fileName = safePlatformName + "_password";

        switch (format) {
            case "BASE64" -> extension = ".b64";
            case "JSON" -> extension = ".json";
            case ".SER" -> extension = ".ser";
            default -> {
                showError("Format tidak dikenal untuk disimpan!");
                return null;
            }
        }

        // Pastikan direktori output ada
        if (!outputDirectory.exists()) {
            if (!outputDirectory.mkdirs()) {
                showError("Gagal membuat direktori output:\n" + outputDirectory.getAbsolutePath());
                return null;
            }
        }

        File outputFile = new File(outputDirectory, fileName + extension);

        try {
            switch (format) {
                case "BASE64" -> {
                    String jsonForB64 = gson.toJson(data);
                    String base64String = Base64.getEncoder().encodeToString(jsonForB64.getBytes(StandardCharsets.UTF_8));
                    Files.writeString(outputFile.toPath(), base64String, StandardCharsets.UTF_8);
                }
                case "JSON" -> {
                    String jsonString = gson.toJson(data);
                    Files.writeString(outputFile.toPath(), jsonString, StandardCharsets.UTF_8);
                }
                case ".SER" -> {
                    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(outputFile))) {
                        oos.writeObject(data);
                    }
                }
            }
            return outputFile;

        } catch (IOException e) {
            showError("Gagal menyimpan file '" + outputFile.getName() + "':\n" + e.getMessage());
            return null;
        } catch (Exception e) {
            showError("Terjadi kesalahan tak terduga saat menyimpan file:\n" + e.getMessage());
            return null;
        }
    }

    private void setupComponents() {
        formatGroup = new ButtonGroup();
        formatGroup.add(radiobuttonbase64);
        formatGroup.add(radiobuttonjson);
        formatGroup.add(radiobuttondotser);
        radiobuttonbase64.setSelected(true);

        outputDirectory = new File(DEFAULT_PATH);
        buttonlokasisimpan.setToolTipText("Lokasi saat ini: " + outputDirectory.getAbsolutePath());

        fieldnamafile.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateJsonRadioState();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateJsonRadioState();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateJsonRadioState();
            }
        });
        updateJsonRadioState();
        buttondeserialisasifile.setEnabled(false);
    }

    private void updateJsonRadioState() {
        boolean isFieldEmpty = fieldnamafile.getText().trim().isEmpty();
        if (isFieldEmpty) {
            selectedFile = null;
            if (radiobuttonjson.isSelected()) {
                radiobuttonbase64.setSelected(true);
            }
        } else if (radiobuttonjson.isSelected()) {
            radiobuttonbase64.setSelected(true);
        }

        buttondeserialisasifile.setEnabled(selectedFile != null || !isFieldEmpty);
        buttonserialisasifile.setEnabled(selectedFile != null || !isFieldEmpty);
    }

    private String getSelectedFormat() {
        if (radiobuttonbase64.isSelected()) {
            return "BASE64";
        }
        if (radiobuttonjson.isSelected()) {
            return "JSON";
        }
        if (radiobuttondotser.isSelected()) {
            return ".SER";
        }
        return "NONE";
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void displayResult(String info) {
        textareaoutput.setText(info + "\n=====================================================");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        buttonpilihfile = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        fieldnamafile = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        textareaoutput = new javax.swing.JTextArea();
        buttonserialisasifile = new javax.swing.JButton();
        buttonserialisasiteks = new javax.swing.JButton();
        buttondeserialisasifile = new javax.swing.JButton();
        radiobuttonbase64 = new javax.swing.JRadioButton();
        radiobuttonjson = new javax.swing.JRadioButton();
        radiobuttondotser = new javax.swing.JRadioButton();
        inputnama = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        inputplatform = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        inputpassword = new javax.swing.JTextField();
        buttonlokasisimpan = new javax.swing.JButton();
        buttonbukafolderoutput = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel1.setText("APLIKASI SIMPAN PASSWORD");

        buttonpilihfile.setText("PILIH FILE");
        buttonpilihfile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonpilihfileActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("PILIH FILE :");

        textareaoutput.setColumns(20);
        textareaoutput.setRows(5);
        jScrollPane2.setViewportView(textareaoutput);

        buttonserialisasifile.setText("SERIALISASI FILE");
        buttonserialisasifile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonserialisasifileActionPerformed(evt);
            }
        });

        buttonserialisasiteks.setText("SERIALISASI TEXT");
        buttonserialisasiteks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonserialisasiteksActionPerformed(evt);
            }
        });

        buttondeserialisasifile.setText("DESERIALISASI FILE");
        buttondeserialisasifile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttondeserialisasifileActionPerformed(evt);
            }
        });

        radiobuttonbase64.setText("BASE64");

        radiobuttonjson.setText("JSON");

        radiobuttondotser.setText(".SER");
        radiobuttondotser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radiobuttondotserActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("INPUT NAMA");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("PLATFORM");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("PASSWORD");

        buttonlokasisimpan.setText("PILIH LOKASI SIMPAN");
        buttonlokasisimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonlokasisimpanActionPerformed(evt);
            }
        });

        buttonbukafolderoutput.setText("BUKA FOLDER OUTPUT");
        buttonbukafolderoutput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonbukafolderoutputActionPerformed(evt);
            }
        });

        jLabel6.setText("LOKASI DAN OUTPUT");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setText("PILIH KONVERSI INPUT");

        jLabel8.setText("*note : pastikan ekstensi file yang dipilih dan pilihan konversi input sama untuk deserialiasi");

        jLabel9.setText("<= DESERIALISASI FILE KE TXT");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(buttonpilihfile, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jLabel7))
                                    .addGap(18, 18, 18)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(fieldnamafile, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(radiobuttonbase64, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(radiobuttonjson, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(radiobuttondotser, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGap(14, 14, 14)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(buttondeserialisasifile, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(buttonserialisasifile, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(buttonserialisasiteks, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addComponent(jLabel1)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel3)
                                        .addComponent(jLabel4))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(inputplatform, javax.swing.GroupLayout.PREFERRED_SIZE, 577, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(inputpassword, javax.swing.GroupLayout.PREFERRED_SIZE, 577, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(inputnama, javax.swing.GroupLayout.PREFERRED_SIZE, 577, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGap(61, 61, 61)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(buttonbukafolderoutput, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(buttonlokasisimpan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                        .addGroup(layout.createSequentialGroup()
                                            .addGap(75, 75, 75)
                                            .addComponent(jLabel6))))
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(83, 83, 83)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 628, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel5))
                        .addGap(0, 43, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 522, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(inputnama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(inputplatform, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(buttonlokasisimpan)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(inputpassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(buttonbukafolderoutput)))
                .addGap(51, 51, 51)
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(buttonpilihfile)
                    .addComponent(fieldnamafile, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonserialisasifile)
                    .addComponent(buttonserialisasiteks))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttondeserialisasifile)
                    .addComponent(radiobuttonbase64)
                    .addComponent(radiobuttonjson)
                    .addComponent(radiobuttondotser)
                    .addComponent(jLabel7)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonpilihfileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonpilihfileActionPerformed
        // TODO add your handling code here:
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(outputDirectory);

        FileNameExtensionFilter filterJson = new FileNameExtensionFilter("JSON files (*.json)", "json");
        FileNameExtensionFilter filterSer = new FileNameExtensionFilter("Serialized files (*.ser)", "ser");
        FileNameExtensionFilter filterTxt = new FileNameExtensionFilter("Text/Base64 files (*.txt, *.b64)", "txt", "b64");
        fileChooser.addChoosableFileFilter(filterJson);
        fileChooser.addChoosableFileFilter(filterSer);
        fileChooser.addChoosableFileFilter(filterTxt);

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            fieldnamafile.setText(selectedFile.getAbsolutePath());
            buttondeserialisasifile.setEnabled(true);
        } else {
            if (fieldnamafile.getText().trim().isEmpty()) {
                selectedFile = null;
                buttondeserialisasifile.setEnabled(false);
            }
        }

    }//GEN-LAST:event_buttonpilihfileActionPerformed

    private void radiobuttondotserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radiobuttondotserActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_radiobuttondotserActionPerformed

    private void buttondeserialisasifileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttondeserialisasifileActionPerformed
        File sourceFile = null;
        if (selectedFile != null && selectedFile.exists() && selectedFile.isFile()) {
            sourceFile = selectedFile;
        } else {
            String filePathFromField = fieldnamafile.getText().trim();
            if (!filePathFromField.isEmpty()) {
                File tempFile = new File(filePathFromField);
                if (tempFile.exists() && tempFile.isFile()) {
                    sourceFile = tempFile;
                }
            }
        }

        if (sourceFile == null) {
            showError("Pilih file sumber yang valid terlebih dahulu menggunakan tombol 'PILIH FILE'.");
            return;
        }
        String format = getSelectedFormat();

        String baseSourceName = sourceFile.getName();
        int lastDot = baseSourceName.lastIndexOf('.');
        if (lastDot > 0) {
            baseSourceName = baseSourceName.substring(0, lastDot);
        }
        String outputTxtFileName = baseSourceName + "_deserialized_from_" + format.toLowerCase().replace(".", "") + ".txt";
        File outputTxtFile = new File(outputDirectory, outputTxtFileName);

        StringBuilder outputContent = new StringBuilder();
        outputContent.append("Hasil Deserialisasi dari File: ").append(sourceFile.getAbsolutePath()).append("\n");
        outputContent.append("Format Sumber (diasumsikan): ").append(format).append("\n");
        outputContent.append("--------------------------------------------------\n\n");

        boolean deserializationAttempted = true;
        try {
            Object deserializedObject = null;

            switch (format) {
                case "BASE64" -> {
                    String base64Content = Files.readString(sourceFile.toPath(), StandardCharsets.UTF_8);
                    byte[] decodedBytes = Base64.getDecoder().decode(base64Content);
                    String jsonFromB64 = new String(decodedBytes, StandardCharsets.UTF_8);
                    try {
                        PasswordData pd = gson.fromJson(jsonFromB64, PasswordData.class);
                        if (pd != null && pd.getNama() != null) {
                            deserializedObject = pd;
                        } else {
                            outputContent.append("[INFO] Hasil decode Base64 (bukan struktur PasswordData):\n");
                            outputContent.append(jsonFromB64);
                        }
                    } catch (JsonSyntaxException innerEx) {
                        outputContent.append("[INFO] Hasil decode Base64 (bukan JSON):\n");
                        outputContent.append(jsonFromB64);
                    }
                } 

                case "JSON" -> {
                    String jsonContent = Files.readString(sourceFile.toPath(), StandardCharsets.UTF_8);
                    try {
                        PasswordData pd = gson.fromJson(jsonContent, PasswordData.class);
                        if (pd != null && pd.getNama() != null) {
                            deserializedObject = pd;
                        } else {
                            outputContent.append("[INFO] Struktur JSON tidak dikenali sebagai PasswordData. Isi JSON:\n");
                            outputContent.append(jsonContent);
                        }
                    }catch (JsonSyntaxException innerEx) {
                        throw new JsonSyntaxException("Konten file bukan JSON yang valid.", innerEx);
                    }
                }
                case ".SER" -> {
                    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(sourceFile))) {
                        deserializedObject = ois.readObject();
                    }
                }
                default -> {
                    outputContent.append("[ERROR] Format sumber tidak dipilih atau tidak dikenal.\n");
                    deserializationAttempted = false;
                }
            }

            if (deserializedObject != null) {
                switch (deserializedObject) {
                    case PasswordData pd -> {
                        outputContent.append("Berhasil deserialisasi sebagai PasswordData:\n");
                        outputContent.append("Nama      : ").append(pd.getNama()).append("\n");
                        outputContent.append("Platform  : ").append(pd.getPlatform()).append("\n");
                        outputContent.append("Password  : ").append(pd.getPassword()).append("\n\n");
                    }
                    case byte[] bytes -> {
                        outputContent.append("Berhasil deserialisasi sebagai array byte (data biner):\n");
                        outputContent.append("Ukuran    : ").append(bytes.length).append(" bytes\n");
                        outputContent.append("Data Base64: ").append(Base64.getEncoder().encodeToString(bytes)).append("\n");
                    }
                    default -> {
                        // Tipe objek lain yang tidak dikenal
                        outputContent.append("Berhasil deserialisasi objek tipe: ")
                                .append(deserializedObject.getClass().getName()).append("\n");
                        outputContent.append("Representasi String : ").append(deserializedObject.toString()).append("\n");
                    }
                }
            } else if (deserializationAttempted && !format.equals("BASE64") && !format.equals("JSON")) {
                outputContent.append("[INFO] Tidak ada objek yang berhasil dideserialisasi (mungkin file kosong atau korup).\n");
            }

        } catch (IOException e) {
            outputContent.append("[ERROR] Gagal membaca file sumber: ").append(e.getMessage()).append("\n");
        } catch (ClassNotFoundException e) {
            outputContent.append("[ERROR] Gagal deserialisasi .ser: Class tidak ditemukan. ").append(e.getMessage()).append("\n");
        } catch (JsonSyntaxException e) {
            outputContent.append("[ERROR] Gagal parsing JSON/Base64: Format tidak sesuai atau file korup. ").append(e.getMessage()).append("\n");
        } catch (IllegalArgumentException e) {
            outputContent.append("[ERROR] Gagal decoding Base64: Konten file tidak valid. ").append(e.getMessage()).append("\n");
        } catch (Exception e) {
            outputContent.append("[ERROR] Terjadi kesalahan tak terduga saat deserialisasi: ").append(e.getMessage()).append("\n");
        }

        try {
            if (!outputDirectory.exists() && !outputDirectory.mkdirs()) {
                throw new IOException("Gagal membuat direktori output: " + outputDirectory.getAbsolutePath());
            }
            Files.writeString(outputTxtFile.toPath(), outputContent.toString(), StandardCharsets.UTF_8);

            displayResult("Hasil deserialisasi disimpan ke:\n" + outputTxtFile.getAbsolutePath());

        } catch (IOException e) {
            showError("Gagal menyimpan file output .txt:\n" + outputTxtFile.getAbsolutePath() + "\nError: " + e.getMessage());
            displayResult("Gagal menyimpan file output .txt.\nError: " + e.getMessage() + "\nLihat detail di atas dalam pesan ini:\n\n" + outputContent.toString());
        }
    }//GEN-LAST:event_buttondeserialisasifileActionPerformed

    private void buttonserialisasifileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonserialisasifileActionPerformed
        File sourceFile = null;
        if (selectedFile != null && selectedFile.exists() && selectedFile.isFile()) {
            sourceFile = selectedFile;
        } else {
            String filePathFromField = fieldnamafile.getText().trim();
            if (!filePathFromField.isEmpty()) {
                File tempFile = new File(filePathFromField);
                if (tempFile.exists() && tempFile.isFile()) {
                    sourceFile = tempFile;
                }
            }
        }

        if (sourceFile == null) {
            showError("Pilih file sumber yang valid terlebih dahulu menggunakan tombol 'PILIH FILE'.");
            return;
        }
        byte[] sourceBytes;
        try {
            sourceBytes = Files.readAllBytes(sourceFile.toPath());
        } catch (IOException e) {
            showError("Gagal membaca file sumber:\n" + sourceFile.getAbsolutePath() + "\nError: " + e.getMessage());
            return;
        } catch (OutOfMemoryError e) {
            showError("File sumber terlalu besar untuk dibaca ke memori.");
            return;
        }
        String format = getSelectedFormat();
        String outputExtension;
        String baseOutputName = sourceFile.getName();
        int lastDot = baseOutputName.lastIndexOf('.');
        if (lastDot > 0) {
            baseOutputName = baseOutputName.substring(0, lastDot);
        }

        switch (format) {
            case "BASE64" -> outputExtension = ".b64";
            case "JSON" -> outputExtension = ".json";
            case ".SER" -> outputExtension = ".ser";
            default -> {
                showError("Format target tidak dikenal!");
                return;
            }
        }

        if (!outputDirectory.exists() && !outputDirectory.mkdirs()) {
            showError("Gagal membuat direktori output:\n" + outputDirectory.getAbsolutePath());
            return;
        }

        File outputFile = new File(outputDirectory, baseOutputName + outputExtension);
        try {
            switch (format) {
                case "BASE64" -> {
                    String base64String = Base64.getEncoder().encodeToString(sourceBytes);
                    Files.writeString(outputFile.toPath(), base64String, StandardCharsets.UTF_8);
                }
                case "JSON" -> {
                    String jsonBase64String = Base64.getEncoder().encodeToString(sourceBytes);
                    var jsonMap = new java.util.HashMap<String, String>();
                    jsonMap.put("originalFileName", sourceFile.getName());
                    jsonMap.put("dataBase64", jsonBase64String);
                    String jsonOutputString = gson.toJson(jsonMap);
                    Files.writeString(outputFile.toPath(), jsonOutputString, StandardCharsets.UTF_8);
                }
                case ".SER" -> {
                    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(outputFile))) {
                        oos.writeObject(sourceBytes);
                    }
                }
            }

            displayResult("File sumber '" + sourceFile.getName() + "'\nberhasil diserialisasi/disimpan sebagai:\n" + outputFile.getAbsolutePath());

        } catch (IOException e) {
            showError("Gagal menyimpan file output '" + outputFile.getName() + "':\n" + e.getMessage());
        } catch (Exception e) {
            showError("Terjadi kesalahan tak terduga saat menyimpan file output:\n" + e.getMessage());
        }

    }//GEN-LAST:event_buttonserialisasifileActionPerformed

    private void buttonserialisasiteksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonserialisasiteksActionPerformed
        String nama = inputnama.getText().trim();
        String platform = inputplatform.getText().trim();
        String password = inputpassword.getText();

        if (nama.isEmpty() || platform.isEmpty() || password.isEmpty()) {
            showError("Nama, Platform, dan Password tidak boleh kosong untuk serialisasi teks input!");
            return;
        }
        PasswordData data = new PasswordData(nama, platform, password);

        File savedFile = serializeToFileHelper(data);

        if (savedFile != null) {
            displayResult("Data dari input field berhasil diserialisasi dan disimpan ke:\n" + savedFile.getAbsolutePath());
        }


    }//GEN-LAST:event_buttonserialisasiteksActionPerformed

    private void buttonlokasisimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonlokasisimpanActionPerformed

        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(outputDirectory);
        chooser.setDialogTitle("Pilih Folder Lokasi Simpan");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            outputDirectory = chooser.getSelectedFile();
            buttonlokasisimpan.setToolTipText("Lokasi saat ini: " + outputDirectory.getAbsolutePath());
            JOptionPane.showMessageDialog(this, "Lokasi simpan diubah ke:\n" + outputDirectory.getAbsolutePath());
        }
    }//GEN-LAST:event_buttonlokasisimpanActionPerformed

    private void buttonbukafolderoutputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonbukafolderoutputActionPerformed

        if (!outputDirectory.exists()) {
            int response = JOptionPane.showConfirmDialog(this,
                    "Folder output belum ada:\n" + outputDirectory.getAbsolutePath() + "\nBuat folder ini?",
                    "Folder Tidak Ditemukan", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                if (!outputDirectory.mkdirs()) {
                    showError("Gagal membuat folder output.");
                    return;
                }
            } else {
                return;
            }
        }

        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().open(outputDirectory);
            } catch (IOException e) {
                showError("Tidak dapat membuka folder: " + e.getMessage());
            } catch (UnsupportedOperationException e) {
                showError("Operasi membuka folder tidak didukung pada sistem ini.");
            }
        } else {
            showError("Desktop API tidak didukung pada sistem ini.");
        }
    }//GEN-LAST:event_buttonbukafolderoutputActionPerformed

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
            java.util.logging.Logger.getLogger(MainApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainApp().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonbukafolderoutput;
    private javax.swing.JButton buttondeserialisasifile;
    private javax.swing.JButton buttonlokasisimpan;
    private javax.swing.JButton buttonpilihfile;
    private javax.swing.JButton buttonserialisasifile;
    private javax.swing.JButton buttonserialisasiteks;
    private javax.swing.JTextField fieldnamafile;
    private javax.swing.JTextField inputnama;
    private javax.swing.JTextField inputpassword;
    private javax.swing.JTextField inputplatform;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JRadioButton radiobuttonbase64;
    private javax.swing.JRadioButton radiobuttondotser;
    private javax.swing.JRadioButton radiobuttonjson;
    private javax.swing.JTextArea textareaoutput;
    // End of variables declaration//GEN-END:variables
}

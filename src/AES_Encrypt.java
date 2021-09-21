import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerAdapter;
import java.io.*;

import java.security.spec.KeySpec;

import java.util.Base64;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class AES_Encrypt extends JFrame{
    private JTextArea normalText;
    private JTextArea encryptText;
    private JButton selectFileButton;
    private JButton encryptButton;
    private JPanel JPSelect;
    private JPanel mainPanel;
    private JPanel JPEncrypt;
    private JButton guardar;
    private JButton desencriptar;

    private static final String KeyAES = "klnosWO?43flbro_w?T!_Fifric8akut";
    private static final String Key2AES = "=1ez8+*Mu8L&PU?Ru@i34StEKU=867Ch";

    public AES_Encrypt(String tile) {
        super(tile);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.setMinimumSize(new Dimension(880, 480));
        this.setIconImage(new ImageIcon(getClass().getResource("/candado.jpg/")).getImage());
        this.setLocationRelativeTo(null);
        this.pack();


        guardar.addContainerListener(new ContainerAdapter() {
        });
        guardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GuardarArchivo();
            }
        });
        selectFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AbrirArchivo();
            }
        });
        encryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Encriptar(encryptText.getText());
            }
        });
        desencriptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Desencriptar(encryptText.getText());
            }
        });
    }
    public void GuardarArchivo() {

        String save = encryptText.getText();
        JFileChooser fc = new JFileChooser();
        fc.showOpenDialog(mainPanel);
        File file = new File(String.valueOf(fc.getSelectedFile())+".txt");
        try {

            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(save);
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void AbrirArchivo() {
        JFileChooser fc = new JFileChooser();
        fc.showOpenDialog(mainPanel);
        FileReader file = null;
        try {
            file = new FileReader(fc.getSelectedFile());
            BufferedReader reader = new BufferedReader(file);

            String key = "";
            String line = reader.readLine();

            while (line != null) {
                key += line + "\n";
                line = reader.readLine();
            }
            normalText.setText(key);
            encryptText.setText(key);

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void Encriptar(String data){
        data = data.replace("\n","");
        try {
            byte[] iv = new byte[16];
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec keySpec = new PBEKeySpec(KeyAES.toCharArray(), Key2AES.getBytes(), 65536, 256);
            SecretKey secretKeyTemp = secretKeyFactory.generateSecret(keySpec);
            SecretKeySpec secretKey = new SecretKeySpec(secretKeyTemp.getEncoded(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
            encryptText.setText(Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes("UTF-8"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Desencriptar(String data){
        data = data.replace("\n","");
        byte[] iv = new byte[16];
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec keySpec = new PBEKeySpec(KeyAES.toCharArray(), Key2AES.getBytes(), 65536, 256);
            SecretKey secretKeyTemp = secretKeyFactory.generateSecret(keySpec);
            SecretKeySpec secretKey = new SecretKeySpec(secretKeyTemp.getEncoded(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
            encryptText.setText( new String(cipher.doFinal(Base64.getDecoder().decode(data))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public static void main(String[] args){
        JFrame frame = new AES_Encrypt("AES Encrypt SW");
        frame.setVisible(true);
    }
}

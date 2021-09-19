import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

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

    String clave = "fdsavdfv";

    public AES_Encrypt(String tile) {
        super(tile);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();

        selectFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser fc = new JFileChooser();
                fc.showOpenDialog(JPSelect);

                FileReader file = null;
                try {
                    file = new FileReader(fc.getSelectedFile());
                    BufferedReader reader = new BufferedReader(file);

                    String key = "";
                    String line = reader.readLine();

                    while (line != null){
                        key += line+"\n";
                        line = reader.readLine();
                    }
                    normalText.setText(key);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        encryptButton.addActionListener(new ActionListener() {

            public SecretKeySpec crearClave(String clave) throws NoSuchAlgorithmException {
                byte[] claveEncriptacion = clave.getBytes(StandardCharsets.UTF_8);

                MessageDigest sha = MessageDigest.getInstance("SHA-1");

                claveEncriptacion = sha.digest(claveEncriptacion);
                claveEncriptacion = Arrays.copyOf(claveEncriptacion, 16);

                return new SecretKeySpec(claveEncriptacion, "AES");
            }

            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                String encryptMsg;
                String msg = normalText.getText();
                SecretKeySpec secretKey = null;
                try {
                    secretKey = this.crearClave(clave);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

                Cipher cipher = null;
                try {
                    cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
                    e.printStackTrace();
                }
                try {
                    assert cipher != null;
                    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                }

                byte[] datosEncriptar = msg.getBytes(StandardCharsets.UTF_8);
                byte[] bytesEncriptados = new byte[0];
                try {
                    bytesEncriptados = cipher.doFinal(datosEncriptar);
                } catch (IllegalBlockSizeException | BadPaddingException e) {
                    e.printStackTrace();
                }
                encryptMsg = Base64.getEncoder().encodeToString(bytesEncriptados);
                encryptText.setText(encryptMsg);
            }
        });
        desencriptar.addActionListener(new ActionListener() {

            public SecretKeySpec crearClave(String clave) throws NoSuchAlgorithmException {
                byte[] claveEncriptacion = clave.getBytes(StandardCharsets.UTF_8);

                MessageDigest sha = MessageDigest.getInstance("SHA-1");

                claveEncriptacion = sha.digest(claveEncriptacion);
                claveEncriptacion = Arrays.copyOf(claveEncriptacion, 16);

                return new SecretKeySpec(claveEncriptacion, "AES");
            }

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                SecretKeySpec secretKey = null;
                try {
                    secretKey = this.crearClave(clave);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                String dtsEncry = encryptText.getText();

                Cipher cipher = null;
                try {
                    cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
                } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
                    e.printStackTrace();
                }
                try {
                    assert cipher != null;
                    cipher.init(Cipher.DECRYPT_MODE, secretKey);
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                }

                byte[] bytesEncriptados;
                bytesEncriptados = Base64.getDecoder().decode(dtsEncry);
                byte[] datosDesencriptados = new byte[0];
                try {
                    datosDesencriptados = cipher.doFinal(bytesEncriptados);
                } catch (IllegalBlockSizeException | BadPaddingException e) {
                    e.printStackTrace();
                }

                String datos = new String(datosDesencriptados);
                encryptText.setText(datos);
            }
        });
    }



    public static void main(String[] args){
        JFrame frame = new AES_Encrypt("AES Encrypt SW");
        frame.setVisible(true);
    }
}

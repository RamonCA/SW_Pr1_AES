import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class AES_Encrypt extends JFrame{
    private JTextArea normalText;
    private JTextArea encryptText;
    private JButton selectFileButton;
    private JButton encryptButton;
    private JPanel JPSelect;
    private JPanel mainPanel;
    private JPanel JPEncrypt;

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
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                String encryptMsg;
                
                encryptText.setText(encryptMsg);

            }
        });
    }

    public static void main(String[] args){
        JFrame frame = new AES_Encrypt("AES Encrypt SW");
        frame.setVisible(true);
    }
}

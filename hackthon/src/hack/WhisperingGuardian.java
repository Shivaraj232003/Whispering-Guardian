package hack;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

public class WhisperingGuardian {
    private JFrame frame;
    private JTextField textfield;
    private JTextArea resarea;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            WhisperingGuardian ui = new WhisperingGuardian();
            ui.createGui();
        });
    }

    public void createGui() {
        frame = new JFrame("Whispering Guardian Page");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        textfield = new JTextField(30);
        JButton button = new JButton("Select File");
        button.addActionListener(new ButtonActionListener());

        JPanel input = new JPanel();
        input.setLayout(new FlowLayout());
        input.add(textfield);
        input.add(button);

        resarea = new JTextArea();
        resarea.setEditable(false);
        JScrollPane scroll = new JScrollPane(resarea);

        panel.add(input, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        frame.add(panel);
        frame.setVisible(true);
    }

    private class ButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser filechooser = new JFileChooser();
            filechooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int value = filechooser.showOpenDialog(frame);
            if (value == JFileChooser.APPROVE_OPTION) {
                File selectfile = filechooser.getSelectedFile();
                textfield.setText(selectfile.getAbsolutePath());
                scanMatch(selectfile.getAbsolutePath());
            }
        }
    }

    private void scanMatch(String path) {
        resarea.setText("");
        String[] files = scanFiles(path);
        if (files == null) {
            resarea.append("No files found in the directory.\n");
            return;
        }

        for (String file : files) {
            String filePath = path + File.separator + file;
            try {
                if (match(filePath)) {
                    resarea.append("Threat detected: " + filePath + "\n");
                } else {
                    resarea.append("Safe: " + filePath + "\n");
                }
            } catch (Exception ex) {
                resarea.append("Error scanning file: " + filePath + "\n");
            }
        }
    }

    public static String[] scanFiles(String directoryPath) {
        File directory = new File(directoryPath);
        return directory.list();
    }

    public static boolean match(String filePath) throws Exception {
        File file = new File(filePath);
        FileInputStream fis = new FileInputStream(file);

        MessageDigest hash = MessageDigest.getInstance("SHA-256");
        byte[] byteArray = new byte[1024];
        int bytecount;

        while ((bytecount = fis.read(byteArray)) != -1) {
            hash.update(byteArray, 0, bytecount);
        }

        fis.close();

        byte[] bytes = hash.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }

        return malware.containsKey(sb.toString());
    }

    private static final HashMap<String, String> malware = new HashMap<>();

    static {
        //Malware signatures
        malware.put("d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2", "Malware A");
        malware.put("e3e3e3e3e3e3e3e3e3e3e3e3e3e3e3e3e3e3e3e3e3e3e3e3e3e3e3e3e3e3e3e3", "Malware B");
        malware.put("89e7d31696fad96fc75e36dd695b7a76f573a167f199640b25f810b64ed6eabc", "Malware C");
        malware.put("6346330d14ba4b9863daab27af458544006043c2da13b38f075b633dfcb2901b","Malware D");
        malware.put("e46165d4c990abb1aa458819c3a628fda99f6899600edabaecbdf4b57a6bfc83", "Malware E");
    }
}
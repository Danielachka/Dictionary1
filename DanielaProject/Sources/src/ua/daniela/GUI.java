package ua.daniela;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Set;

/**
 * Created by zidd on 4/12/14.
 */
public class GUI extends JFrame {
    private Dictionary engToRusDictionary;
    private JLabel enterLabel;
    private JTextField wordTextField;
    private JLabel resultLabel;
    private JButton translateButton;
    private JTextField resultTextField;
    private JButton addNewWordButton;

    public GUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        engToRusDictionary = new Dictionary("dictionary.txt");
        enterLabel = new JLabel("Введите слово для перевода (русское или английское):");
        resultLabel = new JLabel("Перевод:");
        wordTextField = new JTextField(20);
        translateButton = new JButton("Перевести");
        resultTextField = new JTextField(20);
        resultTextField.setEditable(false);
        resultTextField.setBackground(Color.WHITE);
        translateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Set<String> resultList = engToRusDictionary.getTranslated(wordTextField.getText());
                if (resultList == null) {
                    resultList = engToRusDictionary.getReverseTranslated(wordTextField.getText());
                }
                Set<String> addedList = engToRusDictionary.getAddedWord(wordTextField.getText());
                if(addedList != null)
                    resultList.addAll(addedList);
                if (resultList == null) {
                    resultTextField.setForeground(Color.RED);
                    resultTextField.setText("Перевода данного слова в словаре не обнаружено.");
                } else {
                    resultTextField.setForeground(Color.BLACK);
                    resultTextField.setText(listToString(resultList));
                }
            }
        });
        addNewWordButton = new JButton("Добавить слово в словарь");
        addNewWordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddNewWordDialog dialog = new AddNewWordDialog();
            }
        });
        panel.add(enterLabel);
        panel.add(wordTextField);
        panel.add(translateButton);
        panel.add(resultLabel);
        panel.add(resultTextField);
        panel.add(addNewWordButton);
        add(panel);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                engToRusDictionary.saveAddedWordInFile();
            }
        });
        setVisible(true);
        setMinimumSize(new Dimension(400,170));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private static String listToString(Set<String> list) {
        StringBuilder sb = new StringBuilder();
        for(String str : list) {
            sb.append(str + ", ");
        }
        sb.delete(sb.length()-2, sb.length()-1);
        return sb.toString();
    }

    private class AddNewWordDialog extends JDialog {
        private JLabel englishWordLabel;
        private JTextField englishWordTextField;
        private JLabel russianWordLabel;
        private JButton addWordButton;
        private JTextField russianWordTextField;


        public AddNewWordDialog() {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            englishWordLabel = new JLabel("Введите английское слово:");
            englishWordTextField = new JTextField(20);
            russianWordLabel = new JLabel("Введите русский перевод:");
            russianWordTextField = new JTextField(20);
            addWordButton = new JButton("Добавить слово в словарь");
            addWordButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    engToRusDictionary.addWordToAddedList(englishWordTextField.getText(),russianWordTextField.getText());
                    JOptionPane.showMessageDialog(null, "Слово успешно добавлено в словарь.");
                    AddNewWordDialog.this.setVisible(false);
                    AddNewWordDialog.this.dispose();

                }
            });
            panel.add(englishWordLabel);
            panel.add(englishWordTextField);
            panel.add(russianWordLabel);
            panel.add(russianWordTextField);
            panel.add(addWordButton);
            add(panel);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setLocationRelativeTo(GUI.this);
            setMinimumSize(new Dimension(400,170));
            setModal(true);
            setVisible(true);
        }

    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        GUI gui = new GUI();
    }
}

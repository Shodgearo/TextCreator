// Class dialog for enter search string in editor

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class QueryDialog extends JDialog implements ActionListener {
    private JTextField textField;
    private JButton ok, button;
    private String searchString;

    // Constructor
    public QueryDialog(JFrame f, String title) {
        super(f, title, true); // SuperClass constructor
        setResizable(false); // Dialog window not change

        setLayout(new BorderLayout());

        // Размещение двух панелей. Одна содержит текстовые поля, другая - кнопки.
        JPanel panelText = new JPanel();
        JLabel label = new JLabel("Введите строку для поиска: ");
        panelText.add(label);
        textField = new JTextField(40);
        panelText.add(textField);
        add("Center", panelText);

        JPanel panelButton = new JPanel();
        ok = new JButton("OK");
        button = new JButton("Отмена");
        panelButton.add(ok);
        panelButton.add(button);
        add("South", panelButton);

        pack(); // Расположение элементов интерфейса, задание предпочтительных размеров

        // Класс сам выполняет обработку от мыши, т. е. определяет себя в качестве приёмника
        ok.addActionListener(this);
        button.addActionListener(this);

        // Closed window = hidden window
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String label;

        label = e.getActionCommand();

        if (label.equals("Отмена")) {
            searchString = null;
            setVisible(false);
            return;
        }

        if (label.equals("OK")) {
            searchString = textField.getText();
            setVisible(false);
            return;
        }
    }

    // This function returned entered string for search
    public String getString() {
        return searchString;
    }
}

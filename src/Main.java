// This program create and edit text files

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.print.PrinterException;
import java.io.*;
import java.text.MessageFormat;
import java.util.Hashtable;

public class Main extends JFrame {
    private final int ROWS = 100;
    private final int COLS = 400;
    private final JComboBox<String> fonts;
    private final JComboBox<String> styles;
    private final JComboBox<String> colors;
    private JTextArea textArea;
    private String dataName;

    public static void main(String[] args) {
        new Main();
    }

    public Main () {
        // For get all fonts in this system
        GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] arr = g.getAvailableFontFamilyNames();

        textArea = new JTextArea(COLS, ROWS); // Создать поле, где мы будем редактировать текст
        textArea.setEditable(true);
        JScrollPane scroll = new JScrollPane(textArea); // Создать прокручиваемый компонент текста
        fonts = new JComboBox<String>(arr);
        styles = new JComboBox<String>();
        colors = new JComboBox<String>();
        JPanel panel = new JPanel();        // Внутренняя панель для размещения списков
        panel.setLayout(new GridLayout(1, 4));
        JPanel textPane = new JPanel();     // Панель для области текста и поля выбора
        textPane.setLayout(new BorderLayout());

        // Приёмник событий от списков
        class MainItemAdapter implements ItemListener {

            @Override
            public void itemStateChanged(ItemEvent e) {
                fontRefresh();
            }
        }

        // Приёмник событий от меню
        class MainActionListener implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {
                String label;

                label = e.getActionCommand();

                if (label.equals("Открыть")) dateLaden();

                if (label.equals("Сохранить")) ;

                if (label.equals("Печать")) printFile();

                if (label.equals("Закрыть")) System.exit(0);

                if (label.equals("Найти")) stringSearch();
            }
        }

        // Выбор начертания шрифта
        styles.addItem("обычный");
        styles.addItem("курсив");
        styles.addItem("полужирный");

        // Выбор цвета шрифта
        colors.addItem("Чёрный");
        colors.addItem("Красный");
        colors.addItem("Зелёный");
        colors.addItem("Синий");
        colors.addItem("Золотой");
        colors.addItem("Небесный");
        colors.addItem("Пурпурный");

        panel.add(fonts);
        panel.add(styles);
        panel.add(colors);

        textPane.setPreferredSize(new Dimension(100, 400));
        textPane.add("Center", scroll);
        textPane.add("North", panel);

        // Установка шрифта, цвета и стиля
        fonts.setSelectedItem("SanSerif");
        styles.setSelectedItem("normal");
        colors.setSelectedIndex(0);

        MainItemAdapter itemListener = new MainItemAdapter();
        fonts.addItemListener(itemListener);
        styles.addItemListener(itemListener);
        colors.addItemListener(itemListener);

        // Определение и установка параметров шрифта
        // Собственный метод для текстового редактора
        fontRefresh();

        // Команды для буфера обмена. Создание Action-таблицы
        Hashtable<String, Action> tab = new Hashtable<String, Action>();
        Action[] actionArr = textArea.getActions();
        for (int i = 0; i < actionArr.length; i++) {
            Action a = actionArr[i];
            tab.put((String) a.getValue(Action.NAME), a);
        }

        // Прослушиватель событий меню
        MainActionListener actionListener = new MainActionListener();

        // Меню
        JMenuBar menu = new JMenuBar();
        setJMenuBar(menu);

        JMenu menu1 = new JMenu("Файл");
        JMenuItem item1_1 = new JMenuItem("Открыть");
        JMenuItem item1_2 = new JMenuItem("Сохранить");
        JMenuItem item1_3 = new JMenuItem("Найти");
        JMenuItem item1_4 = new JMenuItem("Печать");
        JMenuItem item1_5 = new JMenuItem("Закрыть");
        menu1.add(item1_1);
        menu1.add(item1_2);
        menu1.add(item1_3);
        menu1.add(item1_4);
        menu1.add(item1_5);
        item1_1.addActionListener(actionListener);
        item1_2.addActionListener(actionListener);
        item1_3.addActionListener(actionListener);
        item1_4.addActionListener(actionListener);
        item1_5.addActionListener(actionListener);
        menu.add(menu1);

        JMenu menu2 = new JMenu("Правка");
        JMenuItem item2_1 = new JMenuItem("Вырезать");
        JMenuItem item2_2 = new JMenuItem("Копировать");
        JMenuItem item2_3 = new JMenuItem("Вставить");
        menu2.add(item2_1);
        menu2.add(item2_2);
        menu2.add(item2_3);
        item2_1.addActionListener(tab.get(DefaultEditorKit.cutAction));
        item2_2.addActionListener(tab.get(DefaultEditorKit.copyAction));
        item2_3.addActionListener(tab.get(DefaultEditorKit.pasteAction));
        menu.add(menu2);

        add(textPane);

        initFrame();
    }

    // Печать текста
    private void printFile() {
        if(dataName == null) dataName = "Без имени";

        try {
            textArea.print(new MessageFormat(dataName), new MessageFormat("Страница {0}"), true, null, null, false);
        } catch (PrinterException e) {
            System.err.println(" Печать невозможна. \n");
        }
    }

    private void dateLaden() {
        FileDialog dialog = new FileDialog(this, "Открыть...", FileDialog.LOAD);
        dialog.setVisible(true);

        dataName = dialog.getDirectory() + dialog.getFile();

        // Если пользователь ничего не выбрал, то ничего не делаем
        if(dataName == null) return;

        //Открытие потока ввода и загрузка файла
        StringBuffer buffer = new StringBuffer(COLS * ROWS);

        try (FileReader fr = new FileReader(dataName)) {
            // Считываение символов до тех пор, пока не достигнуть конец файла
            char ch;
            int reading;
            boolean bool = true;

            while (bool) {
                reading = fr.read();

                if(reading == -1) {
                    bool = false;
                    continue;
                }

                ch = (char) reading;
                buffer.append(ch);
            }

            String text = new String(buffer);
            textArea.setText(text);
            textArea.setCaretPosition(0);
        } catch (EOFException e) {
            // Ожидаемое прерывание по достижению конца файла
            // Никаких дальнеших действий
        } catch (FileNotFoundException e) {
            System.err.println("Отсутствует файл или не читается!\n");
            dataName = null;
        } catch (IOException e) {
            // В противном случае что-то пошло не так
            System.err.println("Ошибка при чтении файла " + dataName + "\n");
        }
    }

    private void fontRefresh() { // fontAktualisieren() - учебника
        int fontStill, punkt;
        String color, fontName, style;

        // Определение выбранных в списках атрибутов текста
        fontName = (String) fonts.getSelectedItem();
        style = (String) styles.getSelectedItem();
        color = (String) colors.getSelectedItem();

        // Upload color text
        switch (color) {
            case "Чёрный":
                textArea.setForeground(Color.BLACK);
                break;
            case "Красный":
                textArea.setForeground(Color.RED);
                break;
            case "Зелёный":
                textArea.setForeground(Color.GREEN);
                break;
            case "Синий":
                textArea.setForeground(Color.BLUE);
                break;
            case "Пурпурный":
                textArea.setForeground(Color.MAGENTA);
                break;
            case "Небесный":
                textArea.setForeground(Color.CYAN);
                break;
            case "Золотой":
                textArea.setForeground(Color.YELLOW);
                break;
        }

        // Стиль шрифта - это совокупность констант
        // PLAIN (простой), ITALIC (курсив), BOLD (жирный)
        fontStill = Font.PLAIN;

        if(style.equals("курсив")) fontStill += Font.ITALIC;
        if(style.equals("полужирный")) fontStill += Font.BOLD;

        // Установка выбранных атрибутов шрифта
        textArea.setFont(new Font(fontName, fontStill, 14));
    }

    private void initFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Text Editor");
        setSize(800, 600);
//        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // This method search string in text
    private void stringSearch() {
        String searchString;
        QueryDialog query;
        int index;

        // Open dialog for enter searching string
        query = new QueryDialog(this, "Найти");
        query.setLocation(150, 150);
        query.pack();
        query.setVisible(true);

        searchString = query.getString();

        if (searchString == null) return;

        // Теперь ищем
        String text = textArea.getText();
        index = text.indexOf(searchString);

        if(index == -1) {
            JOptionPane.showMessageDialog(null, "Текст не найден", "Message", JOptionPane.INFORMATION_MESSAGE);
        } else
            // Подсветка строки
            textArea.select(index, index + searchString.length());
    }
}

package ru.mephi.vikingdemo.gui;

import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.VikingAnalysisService;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class VikingAnalysisFrame extends JFrame {
    private final VikingAnalysisService service;
    private final JTextArea resultArea = new JTextArea();

    public VikingAnalysisFrame(VikingAnalysisService service) {
        this.service = service;

        setTitle("Анализ викингов");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel buttons = new JPanel(new GridLayout(0, 2, 5, 5));

        buttons.add(btn("Случайный >180", () -> showViking(service.getRandomVikingTallerThan180())));
        buttons.add(btn("Легендарное снаряжение", () -> showList(service.getVikingsWithLegendaryEquipment())));
        buttons.add(btn("Рыжебородые по возрасту", () -> showList(service.getRedBeardedSortedByAge())));

        resultArea.setEditable(false);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
    }

    private JButton btn(String text, Runnable action) {
        JButton b = new JButton(text);
        b.addActionListener(e -> action.run());
        return b;
    }

    private void showViking(Viking v) {
        resultArea.setText(v != null ? v.name() + " , " + v.age() + " лет, " + v.heightCm() + " см" : "Нет данных");
    }

    private void showList(List<Viking> list) {
        resultArea.setText(list.isEmpty() ? "Нет данных" :
                list.stream().map(v -> v.name() + " (" + v.age() + ")").collect(Collectors.joining("\n")));
    }
}

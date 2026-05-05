package ru.mephi.vikingdemo.gui;

import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.VikingAnalysisService;
import ru.mephi.vikingdemo.service.VikingService;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;


public class VikingDesktopFrame extends JFrame {

    private final VikingService vikingService;
    private final VikingTableModel tableModel = new VikingTableModel();
    private VikingAnalysisFrame analysisFrame;       // ← добавить
    private VikingAnalysisService analysisService;  // ← добавить

    public VikingDesktopFrame(VikingService vikingService, VikingAnalysisService analysisService) {
        this.vikingService = vikingService;
        this.analysisService = analysisService;

        setTitle("Viking Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1000, 420));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel header = new JLabel("Viking Demo", SwingConstants.CENTER);
        header.setFont(header.getFont().deriveFont(Font.BOLD, 18f));
        add(header, BorderLayout.NORTH);

        JTable vikingTable = new JTable(tableModel);
        vikingTable.setRowHeight(28);
        add(new JScrollPane(vikingTable), BorderLayout.CENTER);

        JButton createButton = new JButton("Create random viking");
        createButton.addActionListener(event -> onCreateViking());
        JButton analysisButton = new JButton("Анализ викингов");  // ← добавить
        analysisButton.addActionListener(event -> onOpenAnalysis());

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(createButton);
        bottomPanel.add(analysisButton);
        add(bottomPanel, BorderLayout.SOUTH);

        onInit();
    }

    private void onCreateViking() {
        Viking viking = vikingService.createRandomViking();
        tableModel.addViking(viking);
    }

    private void onOpenAnalysis() {                   // ← добавить метод
        if (analysisFrame == null || !analysisFrame.isVisible()) {
            analysisFrame = new VikingAnalysisFrame(analysisService);
            analysisFrame.setLocation(getX() + 20, getY() + getHeight() + 10);
            analysisFrame.setVisible(true);
        } else {
            analysisFrame.toFront();
        }
    }

    public void addNewViking(Viking viking){
        tableModel.addViking(viking);
    }

    private void onInit() {
        List<Viking> all = vikingService.findAll();
        if (!all.isEmpty()){
            for (Viking viking : all) {
                tableModel.addViking(viking);
            }
        }
    }

    public void removeVikingByName(String name) {
        tableModel.removeVikingByName(name);
    }
}
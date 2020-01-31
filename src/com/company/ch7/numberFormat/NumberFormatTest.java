package com.company.ch7.numberFormat;

import javax.swing.*;
import java.awt.*;

public class NumberFormatTest {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame frame = new NumberFormatFrame();
            frame.setTitle("NumberFormatTest");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}

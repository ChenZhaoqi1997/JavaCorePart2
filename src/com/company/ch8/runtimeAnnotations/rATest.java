package com.company.ch8.runtimeAnnotations;

import javax.swing.*;
import java.awt.*;

public class rATest {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame frame = new ButtonFrame();
            frame.setTitle("ButtonFrameTest");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}

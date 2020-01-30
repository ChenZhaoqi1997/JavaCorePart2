package com.company.ch5.view;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;

public class ViewDBFrame extends JFrame {
    private JButton previousButton;
    private JButton nextButton;
    private JButton deleteButton;
    private JButton saveButton;
    private DataPanel dataPanel;
    private Component scrollPane;
    private JComboBox<String> tableNames;
    private Properties props;
    private CachedRowSet crs;
    private Connection conn;

    public ViewDBFrame() {
        tableNames = new JComboBox<String>();
        try {
            readDatabaseProperties();
            conn = getConnection();
            DatabaseMetaData meta = conn.getMetaData();
            try (ResultSet mrs = meta.getTables(
                    null,
                    null,
                    null,
                    new String[] { "TABLE" })) {
                while (mrs.next()) {
                    tableNames.addItem(mrs.getString(3));
                }
            }
        } catch (SQLException e) {
            for (Throwable t : e) {
                t.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        tableNames.addActionListener(
                event -> showTable((String)tableNames.getSelectedItem(), conn));
        add(tableNames, BorderLayout.NORTH);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    for (Throwable t : e) {
                        t.printStackTrace();
                    }
                }
            }
        });
        JPanel buttonPanel = new JPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        previousButton = new JButton("Previous");
        previousButton.addActionListener(event -> showPreviousRow());
        buttonPanel.add(previousButton);
        nextButton = new JButton("Next");
        nextButton.addActionListener(event -> showNextRow());
        buttonPanel.add(nextButton);
        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(event -> deleteRow());
        buttonPanel.add(deleteButton);
        saveButton = new JButton("Save");
        saveButton.addActionListener(event -> saveChanges());
        buttonPanel.add(saveButton);
        if (tableNames.getItemCount() > 0) {
            showTable(tableNames.getItemAt(0), conn);
        }
    }

    public void showTable(String tableName, Connection conn) {
        try (Statement stat = conn.createStatement();
        ResultSet result = stat.executeQuery("select * from " + tableName)) {
            RowSetFactory factory = RowSetProvider.newFactory();
            crs = factory.createCachedRowSet();
            crs.setTableName(tableName);
            crs.populate(result);
            if (scrollPane != null) {
                remove(scrollPane);
            }
            dataPanel = new DataPanel(crs);
            scrollPane = new JScrollPane(dataPanel);
            add(scrollPane, BorderLayout.CENTER);
            pack();
            showNextRow();
        } catch (SQLException e) {
            for (Throwable t : e) {
                t.printStackTrace();
            }
        }
    }

    public void showPreviousRow() {
        try {
            if (crs == null || crs.isFirst()) {
                return;
            }
            crs.previous();
            dataPanel.showRow(crs);
        } catch (SQLException e) {
            for (Throwable t : e) {
                t.printStackTrace();
            }
        }
    }

    public void showNextRow() {
        try {
            if (crs == null || crs.isLast()) {
                return;
            }
            crs.next();
            dataPanel.showRow(crs);
        } catch (SQLException e) {
            for (Throwable t : e) {
                t.printStackTrace();
            }
        }
    }

    public void deleteRow() {
        if (crs == null) {
            return;
        }
        new SwingWorker<Void, Void>() {
            public Void doInBackground() throws SQLException {
                crs.deleteRow();
                crs.acceptChanges(conn);
                if (crs.isAfterLast()) {
                    if (!crs.last()) {
                        crs = null;
                    }
                }
                return null;
            }
            public void done() {
                dataPanel.showRow(crs);
            }
        }.execute();
    }

    public void saveChanges() {
        if (crs == null) {
            return;
        }
        new SwingWorker<Void, Void>() {
            public Void doInBackground() throws SQLException {
                dataPanel.setRow(crs);
                crs.acceptChanges(conn);
                return null;
            }
        }.execute();
    }

    private void readDatabaseProperties() throws IOException {
        props = new Properties();
        try (InputStream in = Files.newInputStream(Paths.get("D:\\005_some_code\\Java\\IdeaProjects\\JavaCorePart2\\src\\com\\company\\ch5\\test\\database.properties"))) {
            props.load(in);
        }
        String drivers = props.getProperty("jdbc.driver");
        if (drivers != null) {
            System.setProperty("jdbc.drivers", drivers);
        }
    }

    private Connection getConnection() throws SQLException {
        String url = props.getProperty("jdbc.url");
        String username = props.getProperty("jdbc.username");
        String password = props.getProperty("jdbc.password");
        return DriverManager.getConnection(url, username, password);
    }
}

package com.mybank.gui;

import com.mybank.domain.Bank;
import com.mybank.domain.CheckingAccount;
import com.mybank.domain.Customer;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 *
 * @author Alexander 'Taurus' Babich
 */
public class SWINGDemo {

    private final JEditorPane log;
    private final JButton show;
    private final JComboBox clients;

    public SWINGDemo() {
        log = new JEditorPane("text/html", "");
        log.setPreferredSize(new Dimension(250, 150));
        show = new JButton("Show");
        clients = new JComboBox();
        readClientData("test.dat");
        for (int i = 0; i < Bank.getNumberOfCustomers(); i++) {
            clients.addItem(Bank.getCustomer(i).getLastName() + ", " + Bank.getCustomer(i).getFirstName());
        }
    }

    private void launchFrame() {
        JFrame frame = new JFrame("MyBank clients");
        frame.setLayout(new BorderLayout());
        JPanel cpane = new JPanel();
        cpane.setLayout(new GridLayout(1, 2));
        cpane.add(clients);
        cpane.add(show);
        frame.add(cpane, BorderLayout.NORTH);
        frame.add(log, BorderLayout.CENTER);

        show.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Customer current = Bank.getCustomer(clients.getSelectedIndex());
                String accType = current.getAccount(0) instanceof CheckingAccount ? "Checking" : "Savings";
                String custInfo = "<br>&nbsp;<b><span style=\"font-size:2em;\">" + current.getLastName() + ", "
                        + current.getFirstName() + "</span><br><hr>"
                        + "&nbsp;<b>Acc Type: </b>" + accType
                        + "<br>&nbsp;<b>Balance: <span style=\"color:red;\">$" + current.getAccount(0).getBalance() + "</span></b>";
                log.setText(custInfo);
            }
        });

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SWINGDemo demo = new SWINGDemo();
        demo.launchFrame();
    }

    private void readClientData(String filename) {
        try ( BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                String fullName = data[0].trim();
                double balance = Double.parseDouble(data[1].trim());
                String[] names = fullName.split(" ");
                String firstName = names[0].trim();
                String lastName = names[1].trim();
                Bank.addCustomer(firstName, lastName);
                Customer customer = Bank.getCustomer(Bank.getNumberOfCustomers() - 1);
                customer.addAccount(new CheckingAccount(balance));
            }
        } catch (IOException e) {
            System.err.println("Error reading client data from file: " + e.getMessage());
        }
    }
}

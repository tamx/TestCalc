package jp.cane.tam.calc;

import java.awt.Button;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

public class mainframe {
    private TextArea console = null;
    private TextField command = null;

    private mainframe() {
        this.console = new TextArea("",
                5, 20,
                TextArea.SCROLLBARS_VERTICAL_ONLY);
        this.console.setEditable(false);
        this.console.setFocusable(false);
        this.command = new TextField("", 20);
        this.command.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    // Enter
                    mainframe.this.execCalc();
                }
            }

            public void keyReleased(KeyEvent e) {
            }

            public void keyTyped(KeyEvent e) {
            }
        });
        // this.command.setEditable(false);
        // this.command.setFocusable(false);
        Frame frame = new Frame("TestCalc", null);
        frame.setResizable(false);
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GridBagLayout layout = new GridBagLayout();
        frame.setLayout(layout);
        {
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = 5;
            frame.add(this.console, gbc);
        }
        {
            int y = 1;
            GridBagConstraints gbc2 = new GridBagConstraints();
            gbc2.gridy = y;
            gbc2.gridwidth = 5;
            frame.add(this.command, gbc2);
            for (String[] row : new String[][] {
                    { "7", "8", "9", "/" },
                    { "4", "5", "6", "*" },
                    { "1", "2", "3", "-" },
                    { "0", "", "=", "+" },
            }) {
                int x = 0;
                y++;
                for (String cell : row) {
                    GridBagConstraints gbc = new GridBagConstraints();
                    gbc.gridx = x++;
                    gbc.gridy = y;
                    if (cell.length() > 0) {
                        Button button = new Button(cell);
                        ActionListener listener = new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                String com = mainframe.this.command.getText();
                                com += cell;
                                mainframe.this.command.setText(com);
                            }
                        };
                        if ("=".equals(cell)) {
                            listener = new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    mainframe.this.execCalc();
                                }
                            };
                        }
                        button.addActionListener(listener);
                        frame.add(button, gbc);
                    }
                }
            }
        }
        frame.pack();
        frame.setVisible(true);
    }

    private void execCalc() {
        String com = this.command.getText();
        String con = this.console.getText();
        con += "\n" + com;
        this.console.setText(con);
        try {
            int result = calc.Exec(com);
            con += "\n= " + String.valueOf(result);
        } catch (Exception e) {
            e.printStackTrace();
            con += "\nError";
        }
        this.console.setText(con);
        this.command.setText("");
    }

    public static void main(String[] argv) {
        new mainframe();
    }
}

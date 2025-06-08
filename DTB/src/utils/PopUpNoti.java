package utils;

import javax.swing.JOptionPane;

public class PopUpNoti {
    public static void showAlert(String message) {
        JOptionPane.showMessageDialog(null, message, "Alert", JOptionPane.ERROR_MESSAGE);
    }
}

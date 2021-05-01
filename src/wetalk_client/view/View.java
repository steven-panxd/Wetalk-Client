package wetalk_client.view;

import wetalk_client.model.UserModel;

import javax.swing.*;
import java.util.ArrayList;

public abstract class View extends JFrame {
    public View() {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);
    }

    public void showAlert(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}

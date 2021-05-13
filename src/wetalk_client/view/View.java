package wetalk_client.view;

import javax.swing.*;

/**
 * Base view for all views
 */
public abstract class View extends JFrame {
    public View() {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);
    }

    public void showAlert(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}

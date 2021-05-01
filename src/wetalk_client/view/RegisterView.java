package wetalk_client.view;

import wetalk_client.controller.message.Message;
import wetalk_client.controller.message.RegisterMessage;
import wetalk_client.controller.message.SwitchViewMessage;
import wetalk_client.utils.Global;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.BlockingQueue;

public class RegisterView extends View {
    private final Panel registerPanel;
    private final JLabel usernameLabel;
    private final JLabel passwordLabel;
    private final JLabel rePasswordLabel;
    private final JTextField usernameTextField;
    private final JTextField passwordTextField;
    private final JTextField rePasswordTextField;
    private final JButton registerButton;
    private final JButton backButton;

    public RegisterView(BlockingQueue<Message> queue) {
        super();

        this.setTitle("Register");

        this.registerPanel = new Panel();
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
        flowLayout.setVgap(50);
        flowLayout.setHgap(60);
        this.registerPanel.setLayout(flowLayout);
        this.registerPanel.setVisible(true);
        this.add(registerPanel);

        this.usernameLabel = new JLabel("username");
        this.passwordLabel = new JLabel("password");
        this.rePasswordLabel = new JLabel("repeat password");
        this.usernameTextField = new JTextField(10);
        this.passwordTextField = new JTextField(10);
        this.rePasswordTextField = new JTextField(10);
        this.registerButton = new JButton("Register");
        this.registerButton.addActionListener(e -> {
            String username = usernameTextField.getText();
            String password = passwordTextField.getText();
            String rePassword = rePasswordTextField.getText();
            RegisterMessage message = new RegisterMessage(username, password, rePassword);
            try {
                queue.put(message);
            } catch (InterruptedException interruptedException) {
                // ignore
            }
        });
        this.backButton = new JButton("Back");
        this.backButton.addActionListener(e -> {
            Message message = new SwitchViewMessage(new LoginView(queue));
            try {
                queue.put(message);
            } catch (InterruptedException interruptedException) {
                // ignore
            }
        });

        this.registerPanel.add(usernameLabel);
        this.registerPanel.add(usernameTextField);
        this.registerPanel.add(passwordLabel);
        this.registerPanel.add(passwordTextField);
        this.registerPanel.add(registerButton);
        this.registerPanel.add(backButton);

        this.setSize(400, 400);
        this.setLocation(700, 280);
        this.setVisible(true);
    }

    public void registerFail(String message) {
        this.showAlert(message);
    }

    public void registerSucceed() {
        this.showAlert("ok");
    }
}
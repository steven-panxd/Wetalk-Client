package wetalk_client.view;

import wetalk_client.controller.requestMessage.RequestMessage;
import wetalk_client.controller.requestMessage.RegisterRequestMessage;
import wetalk_client.controller.requestMessage.SwitchViewRequestMessage;
import wetalk_client.model.MessageModel;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.BlockingQueue;

public class RegisterView extends View {
    private final BlockingQueue<RequestMessage> queue;
    private final Panel registerPanel;
    private final JLabel usernameLabel;
    private final JLabel passwordLabel;
    private final JLabel rePasswordLabel;
    private final JTextField usernameTextField;
    private final JTextField passwordTextField;
    private final JTextField rePasswordTextField;
    private final JButton registerButton;
    private final JButton backButton;

    public RegisterView(BlockingQueue<RequestMessage> queue) {
        super();
        this.queue = queue;

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
            this.onRegister();
        });
        this.backButton = new JButton("Back");
        this.backButton.addActionListener(e -> {
            RequestMessage requestMessage = new SwitchViewRequestMessage(new LoginView(queue));
            try {
                queue.put(requestMessage);
            } catch (InterruptedException interruptedException) {
                // ignore
            }
        });

        this.registerPanel.add(usernameLabel);
        this.registerPanel.add(usernameTextField);
        this.registerPanel.add(passwordLabel);
        this.registerPanel.add(passwordTextField);
        this.registerPanel.add(rePasswordLabel);
        this.registerPanel.add(rePasswordTextField);
        this.registerPanel.add(registerButton);
        this.registerPanel.add(backButton);

        this.setSize(400, 400);
        this.setLocation(700, 280);
        this.setVisible(true);
    }

    private void onRegister() {
        this.registerButton.setEnabled(false);
        this.backButton.setEnabled(false);
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        String rePassword = rePasswordTextField.getText();
        if(!password.equals(rePassword)) {
            this.setRegisterFail("Two passwords are different, please try again.");
            return;
        }
        RegisterRequestMessage message = new RegisterRequestMessage(username, password);
        try {
            this.queue.put(message);
        } catch (InterruptedException interruptedException) {
            // ignore
        }
    }

    public void setRegisterFail(String message) {
        this.showAlert(message);
        this.registerButton.setEnabled(true);
        this.backButton.setEnabled(true);
    }

    public void setRegisterSucceed(String message) {
        this.showAlert(message);
        this.registerButton.setEnabled(true);
        this.backButton.setEnabled(true);
    }
}
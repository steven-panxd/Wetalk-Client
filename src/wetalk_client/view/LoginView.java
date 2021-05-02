package wetalk_client.view;

import com.google.gson.reflect.TypeToken;
import wetalk_client.controller.requestMessage.LoginRequestMessage;
import wetalk_client.controller.requestMessage.RequestMessage;
import wetalk_client.controller.requestMessage.SwitchViewRequestMessage;
import wetalk_client.utils.Global;
import wetalk_client.utils.Json;
import wetalk_client.utils.LocalStorage;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

public class LoginView extends View {
    BlockingQueue<RequestMessage> queue;
    private final Panel loginPanel;
    private final JLabel usernameLabel;
    private final JLabel passwordLabel;
    private final JTextField usernameTextField;
    private final JTextField passwordTextField;
    private final JCheckBox savePasswordCheckbox;
    private final JButton loginButton;
    private final JButton registerButton;

    public LoginView(BlockingQueue<RequestMessage> queue) {
        super();

        this.setTitle("Login");

        // read and load stored username and password from localStorage if exist
        String fileName = Global.getInstance().getProperty("tempFilePrefix") + "login" + Global.getInstance().getProperty("tempFileExt");
        String jsonTempData = null;
        HashMap<String, String> tempData = new HashMap<>();
        try {
            jsonTempData = LocalStorage.getInstance().readDataFromFile(fileName);
            HashMap<String, String> temp = Json.getInstance().fromJson(jsonTempData, new TypeToken<HashMap<String, String>>(){}.getType());
            if(temp != null) {
                tempData.putAll(temp);
            }
        } catch (IOException e) {
            this.showAlert(e.getMessage());
        }


        this.queue = queue;
        this.loginPanel = new Panel();
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
        flowLayout.setVgap(50);
        flowLayout.setHgap(60);
        this.loginPanel.setLayout(flowLayout);
        this.loginPanel.setVisible(true);
        this.add(loginPanel);

        this.usernameLabel = new JLabel("username");
        this.passwordLabel = new JLabel("password");
        this.usernameTextField = new JTextField(10);
        this.usernameTextField.setText(tempData.getOrDefault("username", ""));
        this.passwordTextField = new JTextField(10);
        this.passwordTextField.setText(tempData.getOrDefault("password", ""));
        this.savePasswordCheckbox = new JCheckBox("Save Password");
        this.savePasswordCheckbox.setSelected(Boolean.parseBoolean(tempData.getOrDefault("savePassword", "false")));
        this.savePasswordCheckbox.setPreferredSize(new Dimension(200, 15));
        this.loginButton = new JButton("Login");
        this.loginButton.addActionListener(e -> {
            this.loginButtonAction();
        });
        this.registerButton = new JButton("Register");
        this.registerButton.addActionListener(e -> {
            RequestMessage requestMessage = new SwitchViewRequestMessage(new RegisterView(queue));
            try {
                queue.put(requestMessage);
            } catch (InterruptedException interruptedException) {
                // ignore
            }
        });

        this.loginPanel.add(usernameLabel);
        this.loginPanel.add(usernameTextField);
        this.loginPanel.add(passwordLabel);
        this.loginPanel.add(passwordTextField);
        this.loginPanel.add(savePasswordCheckbox);
        this.loginPanel.add(loginButton);
        this.loginPanel.add(registerButton);

        this.setSize(400, 400);
        this.setLocation(700, 280);
        this.setVisible(true);
    }

    private void loginButtonAction() {
        this.loginButton.setEnabled(false);
        this.registerButton.setEnabled(false);
        String username = this.usernameTextField.getText();
        String password = this.passwordTextField.getText();
        boolean savePassword = this.savePasswordCheckbox.isSelected();
        LoginRequestMessage message = new LoginRequestMessage(username, password, savePassword);
        try {
            this.queue.put(message);
        } catch (InterruptedException interruptedException) {
            // ignore
        }
    }

    public void loginFail(String message) {
        this.showAlert(message);
        this.loginButton.setEnabled(true);
        this.registerButton.setEnabled(true);
    }

    public void loginSucceed() {
        RequestMessage switchToChatViewRequestMessage = new SwitchViewRequestMessage(new LoadingView(queue));
        try {
            queue.put(switchToChatViewRequestMessage);
        } catch (InterruptedException e) {
            // ignore
        }
    }
}

package wetalk_client.view;

import com.google.gson.reflect.TypeToken;
import wetalk_client.controller.requestMessage.*;
import wetalk_client.model.MessageModel;
import wetalk_client.model.UserModel;
import wetalk_client.utils.Global;
import wetalk_client.utils.Json;
import wetalk_client.utils.LocalStorage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.*;

public class ChatView extends View {
    private final BlockingQueue<RequestMessage> queue;
    private final UserModel loginedUser;
    private UserModel currentChattingFriend = null;
    private final JPanel ChatPanel;
    private final JScrollPane chatHistoryScrollPanel;
    private final JTextArea chatHistoryTextArea;  // chat history
    private final JTextField contentTextField;  // ready to send, content
    private final JTextPane currentChattingIndicatorText;  // chatting with user xxx
    private final JButton sendContentButton; // send content button
    private final ArrayList<UserModel> friendList;
    private final JScrollPane friendJListScrollPanel;
    private final JList<String> friendListJList;
    private Timer getLatestDataTimer;

    public ChatView(BlockingQueue<RequestMessage> queue) {
        super();

        this.queue = queue;
        this.loginedUser = (UserModel) Global.getInstance().get("user");

        this.setTitle("WeTalk User: " + loginedUser.getUsername());

        // load friendList
        friendList = (ArrayList<UserModel>) Global.getInstance().get("friendList");

        // load chat histories to MessageMode, then to global variable
        this.initLoadLocalMessages();

        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
        menuBar.add(this.createActionsMenu());


        //done
        ChatPanel = new JPanel();
        ChatPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(ChatPanel);
        ChatPanel.setLayout(null);
        //done
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 322, 217);
        ChatPanel.add(panel);
        panel.setLayout(null);
        //done
        chatHistoryTextArea = new JTextArea();
        chatHistoryTextArea.setEditable(false);
        chatHistoryTextArea.setText("\n"
                + "                     WeTalk \n"
                + "- Tap the friend botton to add or delete friend\n"
                + "- The friend status would show up on your friend list\n"
                + "- Friend list is located on the left of homepage\n"
                + "- To chat with your friends, you need click the name\n"
                + "- The top of the chatbox will shows your friend's name\n"
                + "- The middle part of the chatbox is the chat history\n"
                + "- Enter your message at the bottom of the chatbox\n"
                + "- Press enter to send, if you recive any message\n"
                + "- Message will show on chatbox or remind you in red\n"
        );
        chatHistoryTextArea.setBounds(0, 21, 322, 196);
        chatHistoryTextArea.setBorder(BorderFactory.createLoweredBevelBorder());
        panel.add(chatHistoryTextArea);
        //done
        chatHistoryScrollPanel = new JScrollPane();
        chatHistoryScrollPanel.setBounds(0, 21, 322, 196);
        chatHistoryScrollPanel.setViewportView(chatHistoryTextArea);
        chatHistoryScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(chatHistoryScrollPanel);

        //done
        currentChattingIndicatorText = new JTextPane();
        currentChattingIndicatorText.setEditable(false);
        if (currentChattingFriend != null) {
            currentChattingIndicatorText.setText("Chatting with user " + currentChattingFriend.getUsername());
        } else {
            currentChattingIndicatorText.setText("Please select a friend from the list.");
        }
        currentChattingIndicatorText.setBackground(Color.LIGHT_GRAY);
        currentChattingIndicatorText.setBounds(0, 0, 322, 21);
        currentChattingIndicatorText.setBorder(BorderFactory.createCompoundBorder());
        panel.add(currentChattingIndicatorText);
        //done
        contentTextField = new JTextField();
        contentTextField.setBounds(0, 217, 322, 21);
        contentTextField.setBorder(BorderFactory.createLoweredBevelBorder());
        ChatPanel.add(contentTextField);
        contentTextField.setColumns(10);
        //done
        sendContentButton = new JButton("Send");
        sendContentButton.addActionListener(e -> {
            this.onSendMessage();
        });
        sendContentButton.setBounds(319, 216, 115, 23);
        sendContentButton.setBorder(BorderFactory.createLoweredBevelBorder());
        ChatPanel.add(sendContentButton);
        //done
        friendListJList = new JList<String>();
        friendListJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        friendListJList.setValueIsAdjusting(true);
        friendListJList.setBounds(321, 0, 150, 217);
        String[] friendArray = new String[friendList.size()];
        for (int i = 0; i < friendList.size(); i++) {
            friendArray[i] = "uid: " + friendList.get(i).getID() + ", username: " + friendList.get(i).getUsername();
        }
        friendListJList.setListData(friendArray);
        friendListJList.setBorder(BorderFactory.createLoweredBevelBorder());
        friendListJList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                this.onAFriendSelected(friendListJList.getSelectedIndex());
            }
        });
        ChatPanel.add(friendListJList);
        //done
        friendJListScrollPanel = new JScrollPane();
        friendJListScrollPanel.setViewportView(friendListJList);
        friendJListScrollPanel.setBounds(321, 0, 113, 217);
        friendJListScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        ChatPanel.add(friendJListScrollPanel);

        this.setLocation(700, 350);
        this.setSize(450, 330);
        this.setVisible(true);

        this.initGetRecentDataTimers();
    }

    public void initGetRecentDataTimers() {
        ActionListener getLatestMessageListener = e -> {
            RequestMessage getLatestMessageRequestMessage = new GetLatestDataRequestMessage();
            try {
                this.queue.put(getLatestMessageRequestMessage);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                this.showAlert(ex.getMessage());
            }
        };
        this.getLatestDataTimer = new Timer(1000, getLatestMessageListener);
        this.getLatestDataTimer.start();
    }

    public void onSendMessage() {
        if (this.currentChattingFriend == null) {
            this.showAlert("Please choose a user from the list first!");
            return;
        }

        String content = contentTextField.getText();
        if (content.equals("")) {
            this.showAlert("You can not send an empty message!");
            return;
        }

        this.sendContentButton.setEnabled(false);
        int receiverID = currentChattingFriend.getID();
        Long sendTimeStamp = System.currentTimeMillis();
        MessageModel messageModel = new MessageModel(this.loginedUser.getID(), receiverID, content, sendTimeStamp);
        RequestMessage requestMessage = new SendMessageRequestMessage(messageModel);
        try {
            this.queue.put(requestMessage);
        } catch (InterruptedException e) {
            e.printStackTrace();
            this.showAlert(e.getMessage());
        }
    }

    public void refreshChatHistoryTextField() {
        if(currentChattingFriend == null) {
            return;
        }
        HashMap<Integer, ArrayList<MessageModel>> allHistoryHashMap = (HashMap<Integer, ArrayList<MessageModel>>) Global.getInstance().get("chatHistory");
        ArrayList<MessageModel> allHistoryWithCurrentChattingFriend = allHistoryHashMap.getOrDefault(currentChattingFriend.getID(), new ArrayList<>());
        String allHistoryWithCurrentChattingFriendDisplay = "";
        for (MessageModel messageModel : allHistoryWithCurrentChattingFriend) {
            allHistoryWithCurrentChattingFriendDisplay += this.parseMessageModelToDisplayChatHistory(messageModel);
        }
        this.chatHistoryTextArea.setText(allHistoryWithCurrentChattingFriendDisplay);

        // pull the scroll panel of chat history to bottom
        int maxHeight = this.chatHistoryScrollPanel.getVerticalScrollBar().getMaximum();
        this.chatHistoryScrollPanel.getViewport().setViewPosition(new Point(0, maxHeight));
    }

    public void setSendMessageSucceed(MessageModel messageModel) {
        this.appendChatHistoryToFileAndMemory(messageModel);
        this.refreshChatHistoryTextField();
        this.contentTextField.setText("");
        this.sendContentButton.setEnabled(true);
    }

    private void appendChatHistoryToFileAndMemory(MessageModel messageModel) {
        // read from memory and load to memory
        HashMap<Integer, ArrayList<MessageModel>> allHistoryHashMap = (HashMap<Integer, ArrayList<MessageModel>>) Global.getInstance().get("chatHistory");
        ArrayList<MessageModel> chatHistoryWithThisFriend;
        int tempID;
        if(messageModel.getSenderID() == this.loginedUser.getID()) {
            tempID = messageModel.getReceiverID();
        } else {
            tempID = messageModel.getSenderID();
        }
        chatHistoryWithThisFriend = allHistoryHashMap.getOrDefault(tempID, new ArrayList<>());
        chatHistoryWithThisFriend.add(messageModel);
        allHistoryHashMap.put(tempID, chatHistoryWithThisFriend);

        // save to local file
        String newAllChatHistory = Json.getInstance().toJson(chatHistoryWithThisFriend);
        String chatHistoryPath = Global.getInstance().getProperty("tempFilePrefix") + "chatHistoryFrom_" + this.loginedUser.getID() + "_To_" + tempID + Global.getInstance().getProperty("tempFileExt");
        try {
            LocalStorage.getInstance().saveDataToFile(chatHistoryPath, newAllChatHistory);
        } catch (IOException e) {
            e.printStackTrace();
            this.showAlert(e.getMessage());
        }
    }

    private String parseMessageModelToDisplayChatHistory(MessageModel messageModel) {
        String chatHistoryDisplay = "";
        int senderID = messageModel.getSenderID();
        String content = messageModel.getContent();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sendTime = sdf.format(new Date(Long.parseLong(String.valueOf(messageModel.getSendTimeStamp()))));
        String senderUsername;
        if (senderID == this.loginedUser.getID()) {  // sent from current user
            senderUsername = this.loginedUser.getUsername();
        } else {  // sent from other friend
            senderUsername = this.currentChattingFriend.getUsername();
        }

        chatHistoryDisplay += (senderUsername + "  " + sendTime + "\n" + content + "\n\n");

        return chatHistoryDisplay;
    }

    public void setSendMessageFail(String message) {
        this.getLatestDataTimer.stop();
        this.showAlert(message);
        this.sendContentButton.setEnabled(true);
    }

    public void setGetLatestMessageSucceed(ArrayList<MessageModel> newMessages) {
        if(newMessages.size() == 0) {
            return;
        }
        this.getLatestDataTimer.stop();
        for(MessageModel newMessageModel : newMessages) {
            this.appendChatHistoryToFileAndMemory(newMessageModel);
        }
        this.refreshChatHistoryTextField();
        this.getLatestDataTimer.restart();
    }

    public void setGetLatestDataFail(String message) {
        this.showAlert(message);
    }

    private void onAFriendSelected(int index) {
        if(index < 0) { return; }
        UserModel currentChattingFriend = this.friendList.get(index);
        this.currentChattingFriend = currentChattingFriend;
        this.currentChattingIndicatorText.setText("Chatting with user " + currentChattingFriend.getUsername());
        this.refreshChatHistoryTextField();
    }

    private void initLoadLocalMessages() {
        HashMap<Integer, ArrayList<MessageModel>> chatHistoryHashMap = new HashMap<>();
        for (UserModel friend : friendList) {
            String chatHistoryPath = Global.getInstance().getProperty("tempFilePrefix") + "chatHistoryFrom_" + this.loginedUser.getID() + "_To_" + friend.getID() + Global.getInstance().getProperty("tempFileExt");
            String jsonChatHistoryData = null;
            try {
                jsonChatHistoryData = LocalStorage.getInstance().readDataFromFile(chatHistoryPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (jsonChatHistoryData != null) {
                ArrayList<MessageModel> data = Json.getInstance().fromJson(jsonChatHistoryData, new TypeToken<ArrayList<MessageModel>>() {}.getType());
                chatHistoryHashMap.put(friend.getID(), data);
            }
        }
        Global.getInstance().put("chatHistory", chatHistoryHashMap);
    }

    public void setAddFriendSucceed(String message) {
        this.showAlert(message);
    }

    public void setAddFriendFail(String message) {
        this.showAlert(message);
    }

    private JMenu createActionsMenu() {
        JMenu menu = new JMenu("Actions");
        JMenuItem item = new JMenuItem("Add friend");
        item.addActionListener(e -> {
            String username = JOptionPane.showInputDialog("Please input a username");
            if (username == null) {
                return;
            }
            if (username.equals("")) {
                this.showAlert("Username can not be empty!");
            }
            RequestMessage requestMessage = new AddFriendRequestMessage(username);
            try {
                this.queue.put(requestMessage);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                this.showAlert(ex.getMessage());
            }
        });
        menu.add(item);
        item = new JMenuItem("Exit");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Exit button function
                System.exit(0);
            }
        });
        menu.add(item);
        return menu;
    }

    public void setGetLatestAddFriendRequesterSucceed(ArrayList<UserModel> addFriendRequesters) {
        if(addFriendRequesters.size() == 0) { return; }

        this.getLatestDataTimer.stop();
        ArrayList<UserModel> acceptedUsers = new ArrayList<>();  // for future updateFriendList method use
        for(UserModel addFriendRequester : addFriendRequesters) {
            String requesterUsername = addFriendRequester.getUsername();
            int value = JOptionPane.showConfirmDialog(this, "Would you accept the add friend request from user: " + requesterUsername, "New Friend Request", JOptionPane.YES_NO_OPTION);
            RequestMessage requestMessage;
            if(value == JOptionPane.YES_OPTION) {
                requestMessage = new AcceptFriendRequestMessage(addFriendRequester.getID());
                acceptedUsers.add(addFriendRequester);
            } else {
                requestMessage = new RejectFriendRequestMessage(addFriendRequester.getID());
            }
            try {
                this.queue.put(requestMessage);
            } catch (InterruptedException e) {
                e.printStackTrace();
                this.showAlert(e.getMessage());
            }
        }
        this.updateFriendList(acceptedUsers);
        this.getLatestDataTimer.restart();
    }

    public void setGetLatestAcceptedUsersSucceed(ArrayList<UserModel> acceptedUsers) {
        if(acceptedUsers.size() == 0) { return; }

        this.getLatestDataTimer.stop();
        for(UserModel acceptedUser : acceptedUsers) {
            this.showAlert(acceptedUser.getUsername() + " accepts your request, starting chat now!");
        }
        this.updateFriendList(acceptedUsers);
        this.getLatestDataTimer.restart();
    }

    private void updateFriendList(ArrayList<UserModel> newFriends) {
        for(UserModel newFriend : newFriends) {
            if(!this.friendList.contains(newFriend)) {
                this.friendList.add(newFriend);
            }
        }

        // construct String[] array for friendListJList
        String[] friendArray = new String[this.friendList.size()];
        for (int i=0; i<friendList.size(); i++) {
            friendArray[i] = "uid: " + friendList.get(i).getID() + ", username: " + friendList.get(i).getUsername();
        }
        this.friendListJList.setListData(friendArray);

        // pull the scroll panel of friendListJList to bottom
        int maxHeight = this.friendJListScrollPanel.getVerticalScrollBar().getMaximum();
        this.friendJListScrollPanel.getViewport().setViewPosition(new Point(0, maxHeight));
    }

    public void setGetLatestRejectedUsersSucceed(ArrayList<UserModel> rejectedUsers) {
        if(rejectedUsers.size() == 0) { return; }

        this.getLatestDataTimer.stop();
        for(UserModel rejectedUser : rejectedUsers) {
            this.showAlert(rejectedUser.getUsername() + " rejects your add friend request, you can try again.");
        }
        this.getLatestDataTimer.restart();
    }
}

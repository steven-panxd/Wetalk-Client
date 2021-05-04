package wetalk_client.view;

import com.google.gson.reflect.TypeToken;
import wetalk_client.controller.requestMessage.*;
import wetalk_client.model.MessageModel;
import wetalk_client.model.UserModel;
import wetalk_client.utils.Global;
import wetalk_client.utils.Json;
import wetalk_client.utils.LocalStorage;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

public class ChatView extends View {
	private final BlockingQueue<RequestMessage> queue;
	private final UserModel loginedUser;
	private final ArrayList<UserModel> friendList;
	private UserModel currentChattingFriend = null;
	private Timer getLatestDataTimer;
	private final JPanel contentPane;
	private final JTextArea contentTextField;
	private final JTextArea chatHistoryTextArea;
	private final JTextField currentChattingIndicatorText;
	private final JScrollPane chatHistoryScrollPanel;
	private final JButton sendContentButton;
	private final JList<String> friendListJList;
	private final JScrollPane friendJListScrollPanel;


	public ChatView(BlockingQueue<RequestMessage> queue) {
		super();

		// init data
		this.queue = queue;
		this.loginedUser = (UserModel) Global.getInstance().get("user");
		this.setTitle("WeTalk User: " + loginedUser.getUsername());
		// load friendList
		friendList = (ArrayList<UserModel>) Global.getInstance().get("friendList");

		// load chat histories to MessageMode, then to global variable
		this.initLoadLocalMessages();

		setBounds(100, 100, 1000, 800);
		contentPane = new JPanel();
		contentPane.setBackground(Color.LIGHT_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Actions Menu
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		menuBar.add(createActionsMenu());

		// left part
		JPanel panelLeft = new JPanel();
		panelLeft.setBounds(0, 0, 750, 738);
		contentPane.add(panelLeft);
		panelLeft.setLayout(null);

		// Input content text area
		contentTextField = new JTextArea();
		contentTextField.setBounds(0, 600, 750, 128);
		contentTextField.setFont(new Font("Calibri", Font.BOLD, 15));
		contentTextField.setBorder(BorderFactory.createLoweredBevelBorder());
		contentTextField.setColumns(10);
		JScrollPane contentScrollPanel = new JScrollPane();
		contentScrollPanel.setViewportView(contentTextField);
		contentScrollPanel.setBounds(0, 600, 750, 128);
		contentScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		panelLeft.add(contentScrollPanel);

		// chat history display area
		chatHistoryTextArea = new JTextArea();
		chatHistoryTextArea.setBounds(0, 45, 750, 550);
		chatHistoryTextArea.setEditable(false);
		chatHistoryTextArea.setText("\n"
				+ "                                         WeTalk \n"
				+ "  - Tap the action menu to add or delete friend\n"
				+ "  - The friend list is located on the right side of homepage\n"
				+ "  - To chat with your friends, you need click on one username\n"
				+ "  - The top of the chat box will show the current chatting friend's name\n"
				+ "  - The middle part of the chat box is the chat history\n"
				+ "  - Enter your message at the bottom of the chat box\n"
				+ "  - Press alt+enter on your keyboard or send button below to send a message.\n"
				+ "  - New message notifications will be shown on friend list\n");
		chatHistoryTextArea.setFont(new Font("Calibri", Font.PLAIN, 18));
		chatHistoryTextArea.setBorder(BorderFactory.createLoweredBevelBorder());
		chatHistoryScrollPanel = new JScrollPane();
		chatHistoryScrollPanel.setBounds(0, 45, 750, 550);
		chatHistoryScrollPanel.setViewportView(chatHistoryTextArea);
		chatHistoryScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		panelLeft.add(chatHistoryScrollPanel);

		// Display current chatting friendName
		currentChattingIndicatorText = new JTextField();
		currentChattingIndicatorText.setEditable(false);
		currentChattingIndicatorText.setHorizontalAlignment(SwingConstants.CENTER);
		currentChattingIndicatorText.setText("Please choose a friend to chat");
		currentChattingIndicatorText.setFont(new Font("Calibri", Font.CENTER_BASELINE, 20));
		currentChattingIndicatorText.setBackground(Color.WHITE);
		currentChattingIndicatorText.setBounds(0, 0, 750, 40);
		currentChattingIndicatorText.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelLeft.add(currentChattingIndicatorText);
		currentChattingIndicatorText.setColumns(10);

		// Right
		JPanel panelRight = new JPanel();
		panelRight.setBounds(750, 0, 235, 740);
		contentPane.add(panelRight);
		panelRight.setLayout(null);

		// Send content button
		sendContentButton = new JButton("Send (Alt+Enter)");
		sendContentButton.setBackground(Color.WHITE);
		sendContentButton.setFont(new Font("Calibri", Font.BOLD, 18));
		sendContentButton.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		sendContentButton.setMnemonic(KeyEvent.VK_ENTER);
		sendContentButton.addActionListener(e -> {
			this.onSendMessage();
		});
		sendContentButton.setBounds(10, 700, 220, 30);
		panelRight.add(sendContentButton);

		// display friendList
		friendListJList = new JList<>();
		friendListJList.setBackground(Color.WHITE);
		friendListJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		friendListJList.setValueIsAdjusting(true);
		friendListJList.setBounds(10, 0, 220, 690);
		String[] friendArray = this.generateFriendArray(friendList);
		friendListJList.setListData(friendArray);
		friendListJList.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		friendListJList.addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				this.onAFriendSelected(friendListJList.getSelectedIndex());
			}
		});

		// scroll panel of friendJList
		friendJListScrollPanel = new JScrollPane();
		friendJListScrollPanel.setViewportView(friendListJList);
		friendJListScrollPanel.setBounds(10, 0, 220, 690);
		friendJListScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		panelRight.add(friendJListScrollPanel);

		this.setVisible(true);
		this.initGetRecentDataTimers();
	}

	private void initGetRecentDataTimers() {
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

	private void onSendMessage() {
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

	private void deleteChatHistoryToFileAndMemory(UserModel deletedUser) {
		// delete in memory
		HashMap<Integer, ArrayList<MessageModel>> allHistoryHashMap = (HashMap<Integer, ArrayList<MessageModel>>) Global.getInstance().get("chatHistory");
		allHistoryHashMap.remove(deletedUser.getID());
		// delete chat history file
		String chatHistoryPath = Global.getInstance().getProperty("tempFilePrefix") + "chatHistoryFrom_" + this.loginedUser.getID() + "_To_" + deletedUser.getID() + Global.getInstance().getProperty("tempFileExt");
		LocalStorage.getInstance().deleteFile(chatHistoryPath);
	}

	private String[] generateFriendArray(ArrayList<UserModel> friendList) {
		String[] friendArray = new String[friendList.size()];
		for(int i = 0; i < friendList.size(); i++) {
			friendArray[i] = this.parseFriendListItemToDisplayFriendArrayItem(friendList.get(i));
		}
		return friendArray;
	}

	private String parseFriendListItemToDisplayFriendArrayItem(UserModel friend) {
		String data = friend.getUsername();
		if(friend.getNewMessageNum() > 0) {
			if(friend.getNewMessageNum() == 1) {
				data += " (" + friend.getNewMessageNum() + " new message)";
			} else {
				data += " (" + friend.getNewMessageNum() + " new messages)";
			}
		}
		return data;
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
			for(UserModel friend : friendList) {
				if(friend.getID() == newMessageModel.getSenderID() && (this.currentChattingFriend == null || this.currentChattingFriend.getID() != newMessageModel.getSenderID())) {
					friend.setNewMessageNum(friend.getNewMessageNum() + 1);
				}
			}
			String[] newFriendArray = this.generateFriendArray(friendList);
			this.friendListJList.setListData(newFriendArray);
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
		this.currentChattingFriend.setNewMessageNum(0);
		String[] newFriendArray = this.generateFriendArray(friendList);
		this.friendListJList.setListData(newFriendArray);
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
			String username = JOptionPane.showInputDialog(this, "Please input a username","Add Friend", JOptionPane.QUESTION_MESSAGE);
			if (username == null) {
				return;
			}
			if (username.equals("")) {
				this.showAlert("Username can not be empty!");
				return;
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
		item = new JMenuItem("Delete Friend");
		item.addActionListener(e -> {
			String username = JOptionPane.showInputDialog(this, "Please input a username", "Delete Friend", JOptionPane.QUESTION_MESSAGE);
			if(username == null) {
				return;
			}
			if(username.equals("")) {
				this.showAlert("Username can not be empty!");
			}

			// check if they are friends
			UserModel deletedFriend = null;
			for(UserModel friend : friendList) {
				if(friend.getUsername().equals(username)) {
					deletedFriend = friend;
					break;
				}
			}
			if(deletedFriend == null) {
				this.showAlert("You are not friends.");
				return;
			}

			DeleteFriendRequestMessage message = new DeleteFriendRequestMessage(deletedFriend.getID());
			try {
				this.queue.put(message);
			} catch (InterruptedException ex) {
				// ignore
			}
			// store in global for response callback use
			Global.getInstance().put("deletedFriend", deletedFriend);
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
		String[] friendArray = this.generateFriendArray(friendList);
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

	public void setDeleteFriendSucceed() {
		this.getLatestDataTimer.stop();
		UserModel deletedFriend = (UserModel) Global.getInstance().remove("deletedFriend");
		this.friendList.remove(deletedFriend);
		this.updateFriendList(this.friendList);
		this.deleteChatHistoryToFileAndMemory(deletedFriend);
		if(this.currentChattingFriend != null && this.currentChattingFriend.equals(deletedFriend)) {
			this.currentChattingFriend = null;
			this.chatHistoryTextArea.setText("");
			this.resetChatHistoryTextArea();
			this.resetCurrentChattingIndicator();
		}
		this.showAlert("Delete user " + deletedFriend.getUsername() + " successfully");
		this.getLatestDataTimer.restart();
	};

	public void setDeleteFriendFail(String message) {
		this.showAlert(message);
	}

	public void setGetLatestDeletedUsersSucceed(ArrayList<UserModel> deletedUsers) {
		this.getLatestDataTimer.stop();
		for(UserModel deletedUser : deletedUsers) {
			this.showAlert("User: " + deletedUser.getUsername() + " deleted you, you are not friends now.");
			boolean hasDeletedFriendInFriendList = this.friendList.remove(deletedUser);
			System.out.println(hasDeletedFriendInFriendList);
			if(hasDeletedFriendInFriendList) {
				this.updateFriendList(this.friendList);
			}
			this.deleteChatHistoryToFileAndMemory(deletedUser);
			if(this.currentChattingFriend != null && this.currentChattingFriend.equals(deletedUser)) {
				this.currentChattingFriend = null;
				this.chatHistoryTextArea.setText("");
				this.resetChatHistoryTextArea();
				this.resetCurrentChattingIndicator();
			}
		}
		this.getLatestDataTimer.restart();
	}

	private void resetChatHistoryTextArea() {
		this.chatHistoryTextArea.setText("\n"
				+ "                                         WeTalk \n"
				+ "  - Tap the action menu to add or delete friend\n"
				+ "  - The friend list is located on the right side of homepage\n"
				+ "  - To chat with your friends, you need click on one username\n"
				+ "  - The top of the chat box will show the current chatting friend's name\n"
				+ "  - The middle part of the chat box is the chat history\n"
				+ "  - Enter your message at the bottom of the chat box\n"
				+ "  - Press alt+enter on your keyboard or send button below to send a message.\n"
				+ "  - New message notifications will be shown on friend list\n");
	}

	private void resetCurrentChattingIndicator() {
		this.currentChattingIndicatorText.setText("Please choose a friend to chat");
	}
}

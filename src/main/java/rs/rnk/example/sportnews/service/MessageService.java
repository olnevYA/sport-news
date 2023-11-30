package rs.rnk.example.sportnews.service;

import java.util.ArrayList;
import java.util.List;

import rs.rnk.example.sportnews.model.Message;
import rs.rnk.example.sportnews.model.MessageType;

public class MessageService extends Service{
	private List<Message> messages;
	
	public MessageService() {
		messages = new ArrayList<>();
	}

	
	public void addMessage(Message message) {
		messages.add(message);
	}
	
	public void deleteMessages() {
		messages.clear();
	}
	
	public void addMessage(String content, MessageType type) {
		messages.add(new Message(type, content));
	}
	
	public List<Message> getMessages(){
		return messages;
	}
	
	public int getNumberOfMessages() {
		return messages.size();
	}
	
}

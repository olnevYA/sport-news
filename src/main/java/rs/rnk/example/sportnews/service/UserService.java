package rs.rnk.example.sportnews.service;

import java.util.List;
import javax.servlet.http.HttpSession;

import rs.rnk.example.sportnews.dao.UserDao;
import rs.rnk.example.sportnews.model.Message;
import rs.rnk.example.sportnews.model.MessageType;
import rs.rnk.example.sportnews.model.User;
import rs.rnk.example.sportnews.model.UserRole;
import rs.rnk.example.sportnews.util.Messages;

public class UserService extends Service{
	
	UserDao userDao;
	
	private User currentUser;
	
	public UserService() {
		currentUser = null;
		userDao = new UserDao();
	}
	
	public User getCurrentUser() {
		return currentUser;
	}
	
	public boolean logIn(HttpSession session, MessageService messageService, String username, String password) {
		User user = (User) session.getAttribute("currentUser");
		if (user != null) {
			return true;
		}
		
		user = userDao.get(username, password);

		boolean error = false;
		if(user == null) {
			messageService.addMessage(new Message(MessageType.ERROR, Messages.LOGIN_ERROR));
			error = true;
		} else if(!user.isApproved()){
			messageService.addMessage(new Message(MessageType.ERROR, Messages.USER_NOT_APPROVED));
			error = true;
		}
		
		if(error) {
			return false;
		}
		this.currentUser = user;
		return true;
	}
	
	public boolean signUp(MessageService messageService, User user) {
		boolean error = false;
		
		User dbUser = userDao.get(user.getUsername());
		if(dbUser != null) {
			messageService.addMessage(new Message(MessageType.ERROR, Messages.USERNAME_EXISTS));
			error = true;
		}
		
		dbUser = userDao.getWithEmail(user.getEmail());
		if(dbUser != null) {
			messageService.addMessage(new Message(MessageType.ERROR, Messages.EMAIL_EXISTS));
			error = true;
		}
		
		if(error) {
			return false;
		}
		
		int result = userDao.insert(user);
		
		if(result < 0) {
			messageService.addMessage(Messages.UNKNOWN_ERROR, MessageType.ERROR);
		}
		
		return result > 0;
		
	}
	
	public boolean edit(User newUser, MessageService messageService) {
		boolean error = false;
		
		
		User dbUser = userDao.getWithUsernameDifferentId(newUser.getUsername(), newUser.getId());
		
		if(dbUser != null) {
			messageService.addMessage(new Message(MessageType.ERROR, Messages.USERNAME_EXISTS));
			error = true;
		}
		
		dbUser = userDao.getWithEmailDifferentId(newUser.getEmail(), newUser.getId());
		if(dbUser != null) {
			messageService.addMessage(new Message(MessageType.ERROR, Messages.EMAIL_EXISTS));
			error = true;
		}
		
		if(error) {
			return false;
		}
		int result = userDao.updateUser(newUser);
		
		if(result < 0) {
			messageService.addMessage(Messages.UNKNOWN_ERROR, MessageType.ERROR);
		}
		
		return result > 0;
	}
	
	public boolean delete(int userId) {
		return userDao.delete(userId);
	}
	
	public List<UserRole> getAllUserRoles(){
		return userDao.findAllUserRoles();
	}
	
	public UserRole getUserRole(int userRoleId) {
		return userDao.getUserRole(userRoleId);
	}
	
	public boolean approveUser(int userId) {
		return userDao.updateUser(userId, true);
	}
	
	public List<User> findUsers(boolean approved){
		return userDao.findUsers(approved);
	}
}

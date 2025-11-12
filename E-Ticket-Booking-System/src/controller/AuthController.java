package controller;

import dao.UserDAO;
import model.User;

public class AuthController {
    private UserDAO userDAO = new UserDAO();

    public boolean registerUser(String name, String email, String phone, String password) {
        if (userDAO.userExists(email)) {
            System.out.println("❌ Email already registered!");
            return false;
        }
        User user = new User(name, email, phone, password);
        return userDAO.addUser(user);
    }

    public User login(String email, String password) {
        return userDAO.login(email, password);
    }
}
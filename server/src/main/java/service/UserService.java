package service;

import dataaccess.DataAccessException;
import dataaccess.IAuthDAO;
import dataaccess.IUserDAO;
import model.AuthData;
import model.UserData;

public class UserService {
    private final IUserDAO userDAO;
    private final IAuthDAO authDAO;

    public UserService(IUserDAO userDAO, IAuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public void clear() throws DataAccessException {
        userDAO.clear();
    }

    public RegisterResult register(RegisterRequest request) {
        try {
            if (userDAO.userExists(request.username())) {
                return new RegisterResult(false, "Error: username already taken");
            }
            UserData newUser = new UserData(request.username(), request.password(), request.email());
            userDAO.createUser(newUser);
            String authToken = generateAuthToken();
            AuthData authData = new AuthData(authToken, request.username());
            authDAO.createAuth(authData);
            return new RegisterResult(true, authToken, request.username());
        } catch (Exception e) {
            return new RegisterResult(false, "Error: " + e.getMessage());
        }
    }

    public LoginResult login(LoginRequest request) {
        try {
            UserData user = userDAO.getUser(request.username());
            if (!user.password().equals(request.password())) {
                return new LoginResult(false, "Error: unauthorized");
            }
            String authToken = generateAuthToken();
            AuthData authData = new AuthData(authToken, request.username());
            authDAO.createAuth(authData);
            return new LoginResult(true, authToken, request.username());
        } catch (Exception e) {
            return new LoginResult(false, "Error: " + e.getMessage());
        }
    }

    public LogoutResult logout(String authToken) {
        try {
            authDAO.deleteAuth(authToken);
            return new LogoutResult(true);
        } catch (Exception e) {
            return new LogoutResult(false, "Error: " + e.getMessage());
        }
    }

    private String generateAuthToken() {
        return java.util.UUID.randomUUID().toString();
    }
}

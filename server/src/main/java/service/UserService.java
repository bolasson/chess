package service;

import dataaccess.DataAccessException;
import dataaccess.IAuthDAO;
import dataaccess.IUserDAO;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import requests.LoginRequest;
import requests.RegisterRequest;
import results.LoginResult;
import results.LogoutResult;
import results.RegisterResult;

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
            if (request.username() == null || request.username().isEmpty()) {
                return new RegisterResult(false, "Error: username is required", 400);
            }
            if (request.password() == null || request.password().isEmpty()) {
                return new RegisterResult(false, "Error: password is required", 400);
            }
            if (request.email() == null || request.email().isEmpty()) {
                return new RegisterResult(false, "Error: email is required", 400);
            }
            if (userDAO.userExists(request.username())) {
                return new RegisterResult(false, "Error: username already taken", 403);
            }
            UserData newUser = new UserData(request.username(), request.password(), request.email());
            userDAO.createUser(newUser);
            String authToken = generateAuthToken();
            AuthData authData = new AuthData(authToken, request.username());
            authDAO.createAuth(authData);
            return new RegisterResult(true, authToken, request.username());
        } catch (Exception e) {
            return new RegisterResult(false, "Error: " + e.getMessage(), 403);
        }
    }

    public LoginResult login(LoginRequest request) {
        try {
            if (request.username() == null || request.username().isEmpty()) {
                return new LoginResult(false, "Error: username is required", 400);
            }
            if (request.password() == null || request.password().isEmpty()) {
                return new LoginResult(false, "Error: password is required", 400);
            }
            UserData user = userDAO.getUser(request.username());
            if (!BCrypt.checkpw(request.password(), user.password())) {
                return new LoginResult(false, "Error: credentials are invalid", 401);
            }
            String authToken = generateAuthToken();
            AuthData authData = new AuthData(authToken, request.username());
            authDAO.createAuth(authData);
            return new LoginResult(true, authToken, request.username());
        } catch (DataAccessException e) {
            return new LoginResult(false, "Error: user does not exist", 401);
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

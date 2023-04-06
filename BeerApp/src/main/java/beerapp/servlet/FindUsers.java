package beerapp.servlet;

import beerapp.dal.UsersDao;
import beerapp.model.User;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/findusers")
public class FindUsers extends HttpServlet {

    protected UsersDao usersDao;

    @Override
    public void init() throws ServletException {
        usersDao = UsersDao.getInstance();
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
        Map<String, String> messages = new HashMap<String, String>();
        req.setAttribute("messages", messages);

        List<User> users = new ArrayList<>();

        String userName = req.getParameter("username");
        if (userName == null || userName.trim().isEmpty()) {
            messages.put("success", "Please enter a valid name.");
        } else {
            try {
                users.add(usersDao.getUserFromUserName(userName));
            } catch (SQLException e) {
                e.printStackTrace();
                throw new IOException(e);
            }
            messages.put("success", "Displaying results for " + userName);
        }
        req.setAttribute("users", users);
        req.getRequestDispatcher("FindUsers.jsp").forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
        // Map for storing messages.
        Map<String, String> messages = new HashMap<String, String>();
        req.setAttribute("messages", messages);

        List<User> users = new ArrayList<>();
        String userName = req.getParameter("username");
        if (userName == null || userName.trim().isEmpty()) {
            messages.put("success", "Please enter a valid name.");
        } else {
            // Retrieve BlogUsers, and store as a message.
            try {
                users.add(usersDao.getUserFromUserName(userName));
            } catch (SQLException e) {
                e.printStackTrace();
                throw new IOException(e);
            }
            messages.put("success", "Displaying results for " + userName);
        }
        req.setAttribute("users", users);
        req.getRequestDispatcher("FindUsers.jsp").forward(req, resp);
    }
}
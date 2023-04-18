package beerapp.servlet;

import beerapp.dal.BeersCountDao;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/getTopBeersCountsByLager")
public class GetTopBeersCountsByLager extends HttpServlet {

  protected final BeersCountDao beersCountDao;

  public GetTopBeersCountsByLager() {
    beersCountDao = BeersCountDao.INSTANCE;
  }

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) {
    String topN = req.getParameter("topN");
    request.setAttribute("topBeers", beersCountDao.getTopBeersCountsByLager(topN));
  }

}
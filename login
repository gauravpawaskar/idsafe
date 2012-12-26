/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Gaurav_Pawaskar
 */
public class login extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /*
             * TODO output your page here. You may use following sample code.
             */
            String userName = request.getParameter("userName");
            String password = request.getParameter("password");
            com.org.IDSafe.Util.DBManager DBMan = new com.org.IDSafe.Util.DBManager();
            Connection connect = DBMan.getConnection();
            String sql = "SELECT PasswordHash, Salt FROM login WHERE userName = ?";
            PreparedStatement statement = connect.prepareStatement(sql);
            statement.setString(1, userName);
            ResultSet loginRS = statement.executeQuery();
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet login</title>");            
            out.println("</head>");
            out.println("<body>");
            if (loginRS.next()){
                String passwordHash = loginRS.getString("PasswordHash");
                int salt = loginRS.getInt("Salt");
                StringBuffer calculatedHashValue = com.org.IDSafe.Util.CryptUtil.getSHA256HashWithSalt(password, salt);
                if(passwordHash.equals(calculatedHashValue.toString())){
                    sql = "SELECT secrete FROM secrete WHERE userName = ?";
                    statement = connect.prepareStatement(sql);
                    statement.setString(1, userName);
                    ResultSet secreteRs = statement.executeQuery();
                    if (secreteRs.next()){
                        String secrete = secreteRs.getString("secrete");
                        //out.println(secrete);
                        HttpSession session = request.getSession();
                        session.setAttribute("secrete", secrete);
                        session.setAttribute("user", userName);
                        response.sendRedirect("myData.jsp");
                    }
                    out.println("<h1>Login Success </h1>");
                } else {
                    out.println("<h1>No Such username or password</h1>");
                }
            } else {
                out.println("<h1>No Such username or password</h1>");
            }
            out.println("</body>");
            out.println("</html>");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
        } finally {            
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}

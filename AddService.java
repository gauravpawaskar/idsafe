/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Gaurav_Pawaskar
 */
public class addService extends HttpServlet {

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
            String service = request.getParameter("serviceName");
            String serviceUser = request.getParameter("user");
            String servicePass = request.getParameter("password");
            String userName = request.getSession().getAttribute("user").toString();
            String encryptedServicePass = "";
            
            com.org.IDSafe.Util.DBManager DBMan = new com.org.IDSafe.Util.DBManager();
            Connection connect = DBMan.getConnection();
            String sql = "SELECT secrete FROM secrete WHERE username = ?";
            PreparedStatement statement = connect.prepareStatement(sql);
            statement.setString(1, userName);
            ResultSet secreteRS = statement.executeQuery();
            if(secreteRS.next()){
                String secrete = secreteRS.getString("secrete");
                byte [] rawKey = com.org.IDSafe.Util.CryptUtil.hexStringToByteArray(secrete);
                byte [] encrypted = com.org.IDSafe.Util.CryptUtil.getAESCrypt(rawKey, servicePass);
                encryptedServicePass = com.org.IDSafe.Util.CryptUtil.asHex(encrypted);
                sql = "INSERT INTO servicedata VALUES (?, ?, ?, ?)";
                statement = connect.prepareStatement(sql);
                statement.setString(1, userName);
                statement.setString(2, service);
                statement.setString(3, serviceUser);
                statement.setString(4, encryptedServicePass);
                statement.executeUpdate();
                response.sendRedirect("myData.jsp");
            }
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet addService</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet addService at " + encryptedServicePass + "</h1>");
            out.println("</body>");
            out.println("</html>");
        } catch (SQLException ex) {
            Logger.getLogger(addService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(addService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(addService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(addService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(addService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(addService.class.getName()).log(Level.SEVERE, null, ex);
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

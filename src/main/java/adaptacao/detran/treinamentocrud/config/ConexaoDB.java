package adaptacao.detran.treinamentocrud.config;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class ConexaoDB extends HttpServlet { // <- MUDANÇA: Herdar de HttpServlet

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String dbUrl = "jdbc:postgresql://localhost:5432/treinamento";
        String dbUser = "postgres";
        String dbPass = "postgres";

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();


        try {
            Class.forName("org.postgresql.Driver");

            conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);

            out.println("<html><body>");
            out.println("<h1>Teste de Conexão com Banco de Dados</h1>");
            out.println("<strong>SUCESSO! Conexão estabelecida.</strong><br/><br/>");

            String sql = "SELECT version();";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            if (rs.next()) {
                out.println("Versão do PostgreSQL: " + rs.getString(1));
            }
            out.println("</body></html>");

        } catch (Exception e) {
            out.println("<html><body>");
            out.println("<h1>ERRO AO CONECTAR COM O BANCO DE DADOS</h1>");
            out.println("<p>Ocorreu um erro:</p>");
            out.println("<pre>" + e.getMessage() + "</pre>");
            out.println("</body></html>");
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
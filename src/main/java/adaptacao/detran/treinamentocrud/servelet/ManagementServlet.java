package adaptacao.detran.treinamentocrud.servelet;

import adaptacao.detran.treinamentocrud.config.IbatisConfig;
import adaptacao.detran.treinamentocrud.controller.proprietario.ProprietarioController;
import adaptacao.detran.treinamentocrud.controller.veiculo.VeiculoController;
import adaptacao.detran.treinamentocrud.dto.proprietario.ProprietarioDTO;
import adaptacao.detran.treinamentocrud.dto.proprietario.ProprietarioDTOMapper;
import adaptacao.detran.treinamentocrud.dto.veiculo.VeiculoDTO;
import adaptacao.detran.treinamentocrud.dto.veiculo.VeiculoMapper;
import adaptacao.detran.treinamentocrud.model.proprietario.ProprietarioModel;
import adaptacao.detran.treinamentocrud.repository.proprietario.ProprietarioDAO;
import adaptacao.detran.treinamentocrud.repository.veiculo.VeiculoDAO;
import adaptacao.detran.treinamentocrud.services.proprietario.ProprietarioServices;
import adaptacao.detran.treinamentocrud.services.veiculo.VeiculoServices;
import com.ibatis.sqlmap.client.SqlMapClient;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ManagementServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(ManagementServlet.class);
    private ProprietarioController proprietarioController;
    private VeiculoController veiculoController;
    private ProprietarioDTOMapper proprietarioDTOMapper;

    @Override
    public void init() throws ServletException {
        try {
            logger.info("Iniciando ManagementServlet");
            SqlMapClient sqlMapClient = IbatisConfig.getSqlMapClient();

            VeiculoDAO veiculoDao = new VeiculoDAO(sqlMapClient);
            VeiculoMapper veiculoMapper = new VeiculoMapper();
            VeiculoServices veiculoService = new VeiculoServices(veiculoDao, veiculoMapper);
            this.veiculoController = new VeiculoController(veiculoService);

            ProprietarioDAO propDao = new ProprietarioDAO(sqlMapClient);
            this.proprietarioDTOMapper = new ProprietarioDTOMapper();
            ProprietarioServices propService = new ProprietarioServices(propDao, this.proprietarioDTOMapper);
            this.proprietarioController = new ProprietarioController(propService);
        } catch (ExceptionInInitializerError e) {
            logger.fatal("Falha CRÍTICA ao inicializar o ManagementServlet. A aplicação pode não funcionar.", e);
            throw new ServletException("Falha ao inicializar o IbatisConfig no VeiculoServlet.", e);
        }
    }

    private void gerarRelatorioPDF(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Connection connection = null;
        try {
            String extensao = "pdf";
            String nomeArquivoJRXML = "report1";

            String relPath = "/WEB-INF/relatorios/" + nomeArquivoJRXML + ".jasper";
            String relNmFile = nomeArquivoJRXML + "." + extensao;

            String pathReport = getServletContext().getRealPath(relPath);

            // ✅ 1. Crie a conexão JDBC (substitua pelos dados do seu banco)
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/treinamento",  // ← ajuste para o nome do seu BD
                    "postgres",                                // ← usuário do banco
                    "postgres"                                   // ← senha do banco
            );

            // ✅ 2. Parâmetros do relatório (se tiver parâmetros no iReport)
            Map<String, Object> params = new HashMap<>();

            // ✅ 3. Gera o relatório passando a conexão
            JasperPrint jasperPrint = JasperFillManager.fillReport(pathReport, params, connection);

            // ✅ 4. Exporta para PDF
            byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);

            if (bytes != null && bytes.length > 0) {
                response.setHeader("Cache-Control", "no-store");
                response.setHeader("Expires", "-1");
                response.setHeader("Pragma", "no-cache");
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "inline; filename=\"" + relNmFile + "\"");
                response.setHeader("Content-transfer-encoding", "binary");
                response.setContentLength(bytes.length);

                ServletOutputStream outputStream = response.getOutputStream();
                outputStream.write(bytes);
                outputStream.flush();
                outputStream.close();

            } else {
                response.sendError(HttpServletResponse.SC_NO_CONTENT, "Relatório sem conteúdo.");
            }

        } catch (Exception e) {
            logger.error("Erro ao gerar o relatório", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ignore) {}
            }
        }
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("gerarRelatorio".equals(action)) {
            gerarRelatorioPDF(req, resp);
        } else {
            listarTodosEEncaminhar(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        String veiculoIdParam = request.getParameter("veiculoId");
        String proprietarioIdParam = request.getParameter("proprietarioId");

        try {
            if ("createOrUpdate".equals(action)) {
                ProprietarioDTO propDTO = new ProprietarioDTO();
                propDTO.setCpf_cnpj(request.getParameter("cpf_cnpj"));
                propDTO.setNome(request.getParameter("nome"));
                propDTO.setEndereco(request.getParameter("endereco"));

                if (veiculoIdParam == null || veiculoIdParam.isEmpty()) {
                    ProprietarioDTO propSalvo = proprietarioController.saveProprietario(propDTO);
                    ProprietarioModel propModel = this.proprietarioDTOMapper.map(propSalvo);

                    VeiculoDTO veiculoDTO = new VeiculoDTO(null, request.getParameter("placa"), request.getParameter("renavam"), propModel);
                    veiculoController.saveVeiculo(veiculoDTO);

                    request.getSession().setAttribute("mensagem", "Registro criado com sucesso!");
                } else {
                    long propId = Long.parseLong(proprietarioIdParam);
                    long veiculoId = Long.parseLong(veiculoIdParam);

                    propDTO.setId(propId);
                    proprietarioController.atualizarProprietario(propId, propDTO);

                    ProprietarioModel propModel = this.proprietarioDTOMapper.map(propDTO);

                    VeiculoDTO veiculoDTO = new VeiculoDTO(veiculoId, request.getParameter("placa"), request.getParameter("renavam"), propModel);
                    veiculoController.atualizarVeiculo(veiculoId, veiculoDTO);

                    request.getSession().setAttribute("mensagem", "Registro ID " + veiculoId + " atualizado com sucesso!");
                }
            } else if ("delete".equals(action)) {
                if (veiculoIdParam != null && !veiculoIdParam.isEmpty()) {
                    long veiculoId = Long.parseLong(veiculoIdParam);
                    veiculoController.deleteVeiculo(veiculoId);
                    request.getSession().setAttribute("mensagem", "Veículo deletado com sucesso!");
                } else {
                    throw new IllegalArgumentException("ID do veículo não fornecido para exclusão.");
                }
            }
        } catch (Exception e) {
            logger.error("Erro ao processar a requisição POST no ManagementServlet.", e);
            request.getSession().setAttribute("mensagemErro", "Erro ao executar ação: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/management");
    }

    private void listarTodosEEncaminhar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<ProprietarioDTO> listaProprietarios = proprietarioController.listarTodosProprietarios();
            request.setAttribute("listaProprietarios", listaProprietarios);

            List<VeiculoDTO> listaVeiculos = veiculoController.listarTodosVeiculos();
            request.setAttribute("listaVeiculos", listaVeiculos);

        } catch (SQLException e) {
            logger.error("Erro ao carregar as listas no doGet.", e);
            request.setAttribute("mensagemErro", "Erro ao carregar as listas: " + e.getMessage());
        }

        String destino = "/index.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(destino);
        dispatcher.forward(request, response);
    }
}
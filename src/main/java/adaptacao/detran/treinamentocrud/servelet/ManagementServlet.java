package adaptacao.detran.treinamentocrud.servelet;

import adaptacao.detran.treinamentocrud.config.IbatisConfig;
import adaptacao.detran.treinamentocrud.controller.proprietario.ProprietarioController;
import adaptacao.detran.treinamentocrud.controller.veiculo.VeiculoController;
import adaptacao.detran.treinamentocrud.dto.proprietario.ProprietarioDTO;
import adaptacao.detran.treinamentocrud.dto.proprietario.ProprietarioDTOMapper;
import adaptacao.detran.treinamentocrud.dto.veiculo.VeiculoDTO;
import adaptacao.detran.treinamentocrud.dto.veiculo.VeiculoMapper;
import adaptacao.detran.treinamentocrud.repository.proprietario.ProprietarioDAO;
import adaptacao.detran.treinamentocrud.repository.veiculo.VeiculoDAO;
import adaptacao.detran.treinamentocrud.services.proprietario.ProprietarioServices;
import adaptacao.detran.treinamentocrud.services.veiculo.VeiculoServices;
import com.google.gson.Gson;
import com.ibatis.sqlmap.client.SqlMapClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManagementServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(ManagementServlet.class);
    private ProprietarioController proprietarioController;
    private VeiculoController veiculoController;
    private ProprietarioDTOMapper proprietarioDTOMapper;
    private final Gson gson = new Gson();

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

    // GET: Usado para buscar a lista completa ou um único item para edição
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String veiculoIdParam = req.getParameter("veiculoId");
        try {
            if (veiculoIdParam != null && !veiculoIdParam.isEmpty()) {
                // Busca um único veículo para popular o formulário de edição
                long id = Long.parseLong(veiculoIdParam);
                VeiculoDTO veiculo = veiculoController.listarVeiculoPorId(id);
                sendAsJson(resp, veiculo, HttpServletResponse.SC_OK);
            } else {
                // Busca a lista completa de veículos
                List<VeiculoDTO> veiculos = veiculoController.listarTodosVeiculos();
                sendAsJson(resp, veiculos, HttpServletResponse.SC_OK);
            }
        } catch (Exception e) {
            sendError(resp, e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    // POST: Usado para criar um novo proprietário e veículo
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Lê o corpo da requisição JSON
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = req.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String body = sb.toString();
            Map<String, String> data = gson.fromJson(body, HashMap.class);

            // 1. Cria e salva o Proprietário
            ProprietarioDTO propDTO = new ProprietarioDTO(null, data.get("cpf_cnpj"), data.get("nome"), data.get("endereco"));
            ProprietarioDTO propSalvo = proprietarioController.saveProprietario(propDTO);

            // 2. Cria e salva o Veículo, associando ao proprietário salvo
            VeiculoDTO veiculoDTO = new VeiculoDTO(null, data.get("placa"), data.get("renavam"), new ProprietarioDTOMapper().map(propSalvo));
            veiculoController.saveVeiculo(veiculoDTO);
            logger.info("Novo registro criado com sucesso.");
            sendSuccessMessage(resp, "Registro criado com sucesso!", HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            logger.error("Erro ao processar a requisição POST para criar registro.", e);
            sendError(resp, e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    // PUT: Usado para atualizar um proprietário e veículo existentes
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = req.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String body = sb.toString();
            Map<String, String> data = gson.fromJson(body, HashMap.class);

            long propId = Long.parseLong(data.get("proprietarioId"));
            long veiculoId = Long.parseLong(data.get("veiculoId"));

            // 1. Atualiza o Proprietário
            ProprietarioDTO propDTO = new ProprietarioDTO(propId, data.get("cpf_cnpj"), data.get("nome"), data.get("endereco"));
            proprietarioController.atualizarProprietario(propId, propDTO);

            // 2. Atualiza o Veículo
            VeiculoDTO veiculoDTO = new VeiculoDTO(veiculoId, data.get("placa"), data.get("renavam"), new ProprietarioDTOMapper().map(propDTO));
            veiculoController.atualizarVeiculo(veiculoId, veiculoDTO);

            sendSuccessMessage(resp, "Registro atualizado com sucesso!", HttpServletResponse.SC_OK);

        } catch (Exception e) {
            sendError(resp, e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    // DELETE: Usado para excluir um veículo
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            long veiculoId = Long.parseLong(req.getParameter("veiculoId"));
            veiculoController.deleteVeiculo(veiculoId);
            // NOTA: A lógica para deletar o proprietário se ele não tiver mais veículos pode ser adicionada aqui.
            sendSuccessMessage(resp, "Veículo deletado com sucesso!", HttpServletResponse.SC_OK);
        } catch (Exception e) {
            sendError(resp, e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    // --- Métodos Utilitários ---
    private void sendAsJson(HttpServletResponse resp, Object obj, int statusCode) throws IOException {
        resp.setStatus(statusCode);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        out.print(gson.toJson(obj));
        out.flush();
    }

    private void sendSuccessMessage(HttpServletResponse resp, String message, int statusCode) throws IOException {
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        sendAsJson(resp, response, statusCode);
    }

    private void sendError(HttpServletResponse resp, String message, int statusCode) throws IOException {
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        sendAsJson(resp, response, statusCode);
    }

}

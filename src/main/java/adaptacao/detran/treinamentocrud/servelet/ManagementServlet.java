package adaptacao.detran.treinamentocrud.servelet;

import adaptacao.detran.treinamentocrud.config.IbatisConfig;
import adaptacao.detran.treinamentocrud.controller.proprietario.ProprietarioController;
import adaptacao.detran.treinamentocrud.controller.veiculo.VeiculoController;
import adaptacao.detran.treinamentocrud.dto.proprietario.ProprietarioDTO;
import adaptacao.detran.treinamentocrud.dto.proprietario.ProprietarioDTOMapper;
import adaptacao.detran.treinamentocrud.dto.veiculo.VeiculoDTO;
import adaptacao.detran.treinamentocrud.dto.veiculo.VeiculoMapper;
import adaptacao.detran.treinamentocrud.model.proprietario.ProprietarioModel; // Novo Importe
import adaptacao.detran.treinamentocrud.repository.proprietario.ProprietarioDAO;
import adaptacao.detran.treinamentocrud.repository.veiculo.VeiculoDAO;
import adaptacao.detran.treinamentocrud.services.proprietario.ProprietarioServices;
import adaptacao.detran.treinamentocrud.services.veiculo.VeiculoServices;
import com.ibatis.sqlmap.client.SqlMapClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class ManagementServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(ManagementServlet.class);
    private ProprietarioController proprietarioController;
    private VeiculoController veiculoController;
    private ProprietarioDTOMapper proprietarioDTOMapper;
    // Removendo: private final Gson gson = new Gson();

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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Redireciona a requisição para a função de listagem e encaminhamento
        listarTodosEEncaminhar(req, resp);
    }

    // POST: Consolida as operações de CREATE, UPDATE e DELETE.
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        // O parâmetro 'action' é enviado pelo formulário ou por botões de exclusão.
        String action = request.getParameter("action");
        String veiculoIdParam = request.getParameter("veiculoId");
        String proprietarioIdParam = request.getParameter("proprietarioId");

        try {
            if ("createOrUpdate".equals(action)) {
                // 1. Processa dados do Proprietário
                ProprietarioDTO propDTO = new ProprietarioDTO();
                propDTO.setCpf_cnpj(request.getParameter("cpf_cnpj"));
                propDTO.setNome(request.getParameter("nome"));
                propDTO.setEndereco(request.getParameter("endereco"));

                // 2. Decide se é CREATE (veiculoId vazio) ou UPDATE (veiculoId com valor)
                if (veiculoIdParam == null || veiculoIdParam.isEmpty()) {
                    // --- CREATE ---
                    // Salva o novo Proprietário para obter o ID
                    ProprietarioDTO propSalvo = proprietarioController.saveProprietario(propDTO);
                    ProprietarioModel propModel = this.proprietarioDTOMapper.map(propSalvo);

                    // Cria e salva o Veículo, associando ao proprietário salvo
                    VeiculoDTO veiculoDTO = new VeiculoDTO(null, request.getParameter("placa"), request.getParameter("renavam"), propModel);
                    veiculoController.saveVeiculo(veiculoDTO);

                    request.getSession().setAttribute("mensagem", "Registro criado com sucesso!");
                } else {
                    // --- UPDATE ---
                    long propId = Long.parseLong(proprietarioIdParam);
                    long veiculoId = Long.parseLong(veiculoIdParam);

                    propDTO.setId(propId); // Garante que o ID do proprietário está no DTO
                    proprietarioController.atualizarProprietario(propId, propDTO);

                    ProprietarioModel propModel = this.proprietarioDTOMapper.map(propDTO);

                    VeiculoDTO veiculoDTO = new VeiculoDTO(veiculoId, request.getParameter("placa"), request.getParameter("renavam"), propModel);
                    veiculoController.atualizarVeiculo(veiculoId, veiculoDTO);

                    request.getSession().setAttribute("mensagem", "Registro ID " + veiculoId + " atualizado com sucesso!");
                }
            } else if ("delete".equals(action)) {
                // --- DELETE ---
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

        // Redireciona para o doGet, que carrega a página e a lista atualizada
        listarTodosEEncaminhar(request, response);
    }

    // Método auxiliar para carregar dados e encaminhar para o JSP (retirado do ProprietarioServlet)
    private void listarTodosEEncaminhar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // As listas são necessárias para renderizar a tabela
            List<ProprietarioDTO> listaProprietarios = proprietarioController.listarTodosProprietarios();
            request.setAttribute("listaProprietarios", listaProprietarios);

            List<VeiculoDTO> listaVeiculos = veiculoController.listarTodosVeiculos();
            request.setAttribute("listaVeiculos", listaVeiculos);

        } catch (SQLException e) {
            logger.error("Erro ao carregar as listas no doGet.", e);
            request.setAttribute("mensagemErro", "Erro ao carregar as listas: " + e.getMessage());
        }

        // Encaminha para o JSP (o JSP fará a renderização)
        String destino = "/index.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(destino);
        dispatcher.forward(request, response);
    }
}
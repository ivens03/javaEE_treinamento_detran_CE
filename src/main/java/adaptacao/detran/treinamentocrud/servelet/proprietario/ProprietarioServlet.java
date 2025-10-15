package adaptacao.detran.treinamentocrud.servelet.proprietario;

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
import java.util.ArrayList;
import java.util.List;

public class ProprietarioServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(ProprietarioServlet.class);
    private ProprietarioController proprietarioController;
    private VeiculoController veiculoController;

    @Override
    public void init() throws ServletException {
        try {
            SqlMapClient sqlMapClient = IbatisConfig.getSqlMapClient();

            ProprietarioDAO propDao = new ProprietarioDAO(sqlMapClient);
            ProprietarioDTOMapper propMapper = new ProprietarioDTOMapper();
            ProprietarioServices propService = new ProprietarioServices(propDao, propMapper);
            this.proprietarioController = new ProprietarioController(propService);

            VeiculoDAO veiculoDao = new VeiculoDAO(sqlMapClient);
            VeiculoMapper veiculoMapper = new VeiculoMapper();
            VeiculoServices veiculoService = new VeiculoServices(veiculoDao, veiculoMapper);
            this.veiculoController = new VeiculoController(veiculoService);
        } catch (ExceptionInInitializerError e) {
            logger.fatal("Falha CRÍTICA ao inicializar o ProprietarioServlet. A aplicação não pode continuar.", e);
            throw new ServletException("Falha ao inicializar o IbatisConfig. Verifique a localização e o conteúdo dos arquivos de configuração.", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Busca a lista de veículos para a carga inicial.
            List<VeiculoDTO> listaVeiculos = veiculoController.listarTodosVeiculos();
            request.setAttribute("listaVeiculos", listaVeiculos);

            // Busca a lista de proprietários para os formulários de edição (se aplicável no futuro).
            List<ProprietarioDTO> listaProprietarios = proprietarioController.listarTodosProprietarios();
            request.setAttribute("listaProprietarios", listaProprietarios);

        } catch (SQLException e) {
            logger.fatal("Falha CRÍTICA ao inicializar o ProprietarioServlet. A aplicação não pode continuar.", e);
            e.printStackTrace();
            request.setAttribute("mensagemErro", "Erro de banco de dados ao carregar as listas: " + e.getMessage());
            request.setAttribute("listaVeiculos", new ArrayList<VeiculoDTO>());
            request.setAttribute("listaProprietarios", new ArrayList<ProprietarioDTO>());
        } catch (Exception e) {
            logger.fatal("Falha CRÍTICA ao inicializar o ProprietarioServlet. A aplicação não pode continuar.", e);
            e.printStackTrace();
            request.setAttribute("mensagemErro", "Erro de banco de dados ao carregar as listas: " + e.getMessage());
            request.setAttribute("listaVeiculos", new ArrayList<VeiculoDTO>());
            request.setAttribute("listaProprietarios", new ArrayList<ProprietarioDTO>());
        }

        listarTodosEEncaminhar(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        try {
            if ("create".equals(action)) {
                ProprietarioDTO proprietario = new ProprietarioDTO();
                proprietario.setCpf_cnpj(request.getParameter("cpf_cnpj"));
                proprietario.setNome(request.getParameter("nome"));
                proprietario.setEndereco(request.getParameter("endereco"));
                proprietarioController.saveProprietario(proprietario);
                request.getSession().setAttribute("mensagem", "Proprietário salvo com sucesso!");

            } else if ("update".equals(action)) {
                Long id = Long.parseLong(request.getParameter("id"));
                ProprietarioDTO proprietario = new ProprietarioDTO();
                proprietario.setCpf_cnpj(request.getParameter("cpf_cnpj"));
                proprietario.setNome(request.getParameter("nome"));
                proprietario.setEndereco(request.getParameter("endereco"));
                proprietarioController.atualizarProprietario(id, proprietario);
                request.getSession().setAttribute("mensagem", "Proprietário ID " + id + " atualizado com sucesso!");

            } else if ("delete".equals(action)) {
                Long id = Long.parseLong(request.getParameter("id"));
                proprietarioController.deleteProprietario(id);
                request.getSession().setAttribute("mensagem", "Proprietário ID " + id + " deletado com sucesso!");
            }
        } catch (Exception e) {
            request.getSession().setAttribute("mensagemErro", "Erro ao executar ação no proprietário: " + e.getMessage());
        }

        // Redireciona para a URL raiz
        response.sendRedirect(request.getContextPath() + "/");
    }

    private void listarTodosEEncaminhar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<ProprietarioDTO> listaProprietarios = proprietarioController.listarTodosProprietarios();
            request.setAttribute("listaProprietarios", listaProprietarios);

            List<VeiculoDTO> listaVeiculos = veiculoController.listarTodosVeiculos();
            request.setAttribute("listaVeiculos", listaVeiculos);

        } catch (SQLException e) {
            request.setAttribute("mensagemErro", "Erro ao carregar as listas: " + e.getMessage());
        }

        String destino = "/index.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(destino);
        dispatcher.forward(request, response);
    }
}
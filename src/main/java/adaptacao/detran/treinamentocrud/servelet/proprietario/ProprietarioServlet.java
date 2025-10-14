package adaptacao.detran.treinamentocrud.servelet.proprietario;

import adaptacao.detran.treinamentocrud.config.IbatisConfig;
import adaptacao.detran.treinamentocrud.controller.proprietario.ProprietarioController;
import adaptacao.detran.treinamentocrud.dto.proprietario.ProprietarioDTO;
import adaptacao.detran.treinamentocrud.dto.proprietario.ProprietarioDTOMapper;
import adaptacao.detran.treinamentocrud.repository.proprietario.ProprietarioDAO;
import adaptacao.detran.treinamentocrud.services.proprietario.ProprietarioServices;
import com.ibatis.sqlmap.client.SqlMapClient;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ProprietarioServlet extends HttpServlet {

    private ProprietarioController proprietarioController;

    @Override
    public void init() throws ServletException {
        SqlMapClient sqlMapClient = IbatisConfig.getSqlMapClient();
        ProprietarioDAO dao = new ProprietarioDAO(sqlMapClient);
        ProprietarioDTOMapper mapper = new ProprietarioDTOMapper();
        ProprietarioServices service = new ProprietarioServices(dao, mapper);
        this.proprietarioController = new ProprietarioController(service);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // CORREÇÃO: Pega as mensagens da sessão (se existirem) e as move para a requisição
        // para que o JSP possa exibi-las. Depois, as remove da sessão.
        request.setAttribute("mensagem", request.getSession().getAttribute("mensagem"));
        request.getSession().removeAttribute("mensagem");

        request.setAttribute("mensagemErro", request.getSession().getAttribute("mensagemErro"));
        request.getSession().removeAttribute("mensagemErro");

        String idParam = request.getParameter("id");
        try {
            if (idParam != null && !idParam.isEmpty()) {
                Long id = Long.parseLong(idParam);
                ProprietarioDTO proprietarioEncontrado = proprietarioController.listarProprietarioPorId(id);
                if (proprietarioEncontrado != null) {
                    request.setAttribute("proprietarioEncontrado", proprietarioEncontrado);
                } else {
                    request.getSession().setAttribute("mensagemErro", "Proprietário com ID " + id + " não foi encontrado.");
                }
            }
        } catch (Exception e) {
            request.getSession().setAttribute("mensagemErro", "Ocorreu um erro: " + e.getMessage());
        }

        listarTodosEEncaminhar(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        try {
            if ("create".equals(action)) {
                ProprietarioDTO proprietarioParaSalvar = new ProprietarioDTO();
                proprietarioParaSalvar.setCpf_cnpj(request.getParameter("cpf_cnpj"));
                proprietarioParaSalvar.setNome(request.getParameter("nome"));
                proprietarioParaSalvar.setEndereco(request.getParameter("endereco"));
                proprietarioController.saveProprietario(proprietarioParaSalvar);
                // CORREÇÃO: Salva a mensagem de sucesso na SESSÃO
                request.getSession().setAttribute("mensagem", "Proprietário salvo com sucesso!");

            } else if ("update".equals(action)) {
                Long id = Long.parseLong(request.getParameter("id"));
                ProprietarioDTO proprietarioParaAtualizar = new ProprietarioDTO();
                proprietarioParaAtualizar.setCpf_cnpj(request.getParameter("cpf_cnpj"));
                proprietarioParaAtualizar.setNome(request.getParameter("nome"));
                proprietarioParaAtualizar.setEndereco(request.getParameter("endereco"));
                proprietarioController.atualizarProprietario(id, proprietarioParaAtualizar);
                request.getSession().setAttribute("mensagem", "Proprietário ID " + id + " atualizado com sucesso!");

            } else if ("delete".equals(action)) {
                Long id = Long.parseLong(request.getParameter("id"));
                proprietarioController.deleteProprietario(id);
                request.getSession().setAttribute("mensagem", "Proprietário ID " + id + " deletado com sucesso!");
            }
        } catch (Exception e) {
            // CORREÇÃO: Salva a mensagem de erro na SESSÃO
            request.getSession().setAttribute("mensagemErro", "Erro ao executar a ação: " + e.getMessage());
        }

        // CORREÇÃO: Redireciona para a URL base do servlet. O navegador fará uma nova requisição GET.
        response.sendRedirect(request.getContextPath() + "/proprietario");
    }

    private void listarTodosEEncaminhar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<ProprietarioDTO> listaProprietarios = proprietarioController.listarTodosProprietarios();
            request.setAttribute("listaProprietarios", listaProprietarios);
        } catch (SQLException e) {
            request.setAttribute("mensagemErro", "Erro ao carregar a lista de proprietários: " + e.getMessage());
        }

        // CORREÇÃO: Apontando para o index.jsp que está na raiz do webapp
        String destino = "/index.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(destino);
        dispatcher.forward(request, response);
    }
}
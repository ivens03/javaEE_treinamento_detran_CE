package adaptacao.detran.treinamentocrud.servelet.veiculo;

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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class VeiculoServlet extends HttpServlet {

    private VeiculoController veiculoController;
    private ProprietarioController proprietarioController;
    private ProprietarioDTOMapper proprietarioDTOMapper;

    @Override
    public void init() throws ServletException {
        try {
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
            throw new ServletException("Falha ao inicializar o IbatisConfig no VeiculoServlet.", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        try {
            if ("createVeiculo".equals(action) || "updateVeiculo".equals(action)) {
                VeiculoDTO veiculo = new VeiculoDTO();
                veiculo.setPlaca(request.getParameter("placa"));
                veiculo.setRenavam(request.getParameter("renavam"));

                Long proprietarioId = Long.parseLong(request.getParameter("proprietarioId"));
                ProprietarioDTO proprietarioDTO = proprietarioController.listarProprietarioPorId(proprietarioId);
                ProprietarioModel proprietarioModel = this.proprietarioDTOMapper.map(proprietarioDTO);

                // ==================================================================
                // CORREÇÃO: O método correto é 'setProprietario', não 'setId_proprietario'
                // ==================================================================
                veiculo.setProprietario(proprietarioModel);

                if ("createVeiculo".equals(action)) {
                    veiculoController.saveVeiculo(veiculo);
                    request.getSession().setAttribute("mensagem", "Veículo salvo com sucesso!");
                } else {
                    Long id = Long.parseLong(request.getParameter("id"));
                    veiculo.setId(id); // O DTO precisa do ID para a camada de serviço saber quem atualizar
                    veiculoController.atualizarVeiculo(id, veiculo);
                    request.getSession().setAttribute("mensagem", "Veículo ID " + id + " atualizado com sucesso!");
                }

            } else if ("deleteVeiculo".equals(action)) {
                Long id = Long.parseLong(request.getParameter("id"));
                veiculoController.deleteVeiculo(id);
                request.getSession().setAttribute("mensagem", "Veículo ID " + id + " deletado com sucesso!");
            }
        } catch (Exception e) {
            request.getSession().setAttribute("mensagemErro", "Erro ao executar ação no veículo: " + e.getMessage());
            e.printStackTrace();
        }

        // Redireciona para a URL raiz
        response.sendRedirect(request.getContextPath() + "/");
    }
}
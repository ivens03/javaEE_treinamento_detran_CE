package adaptacao.detran.treinamentocrud.controller.proprietario;

import adaptacao.detran.treinamentocrud.dto.proprietario.ProprietarioDTO;
import adaptacao.detran.treinamentocrud.services.proprietario.ProprietarioServices;

import java.sql.SQLException;
import java.util.List;

public class ProprietarioController {

    private final ProprietarioServices proprietarioServices;

    public ProprietarioController(ProprietarioServices proprietarioServices) {
        this.proprietarioServices = proprietarioServices;
    }

    public ProprietarioDTO saveProprietario(ProprietarioDTO proprietarioDTO) throws SQLException {
        return proprietarioServices.saveProprietario(proprietarioDTO);
    }

    public ProprietarioDTO listarProprietarioPorId (Long id) throws SQLException {
        return proprietarioServices.listarProprietarioPorId(id);
    }

    public List<ProprietarioDTO> listarTodosProprietarios() throws SQLException {
        return proprietarioServices.listarTodosProprietarios();
    }

    public ProprietarioDTO atualizarProprietario(Long id, ProprietarioDTO proprietarioDTO) throws SQLException {
        return proprietarioServices.atualizarProprietario(id, proprietarioDTO);
    }

    public void deleteProprietario(Long id) throws SQLException {
        proprietarioServices.deleteProprietario(id);
    }

}

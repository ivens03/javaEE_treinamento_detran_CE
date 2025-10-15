package adaptacao.detran.treinamentocrud.services.proprietario;

import adaptacao.detran.treinamentocrud.dto.proprietario.ProprietarioDTO;
import adaptacao.detran.treinamentocrud.dto.proprietario.ProprietarioDTOMapper;
import adaptacao.detran.treinamentocrud.model.proprietario.ProprietarioModel;
import adaptacao.detran.treinamentocrud.repository.proprietario.ProprietarioDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProprietarioServices {

    private final ProprietarioDAO proprietarioDAO;
    private final ProprietarioDTOMapper proprietarioDTOMapper;

    public ProprietarioServices(ProprietarioDAO proprietarioDAO, ProprietarioDTOMapper proprietarioDTOMapper) {
        this.proprietarioDAO = proprietarioDAO;
        this.proprietarioDTOMapper = proprietarioDTOMapper;
    }

    public ProprietarioDTO saveProprietario(ProprietarioDTO proprietarioDTO) throws SQLException {
        ProprietarioModel modelParaSalvar = proprietarioDTOMapper.map(proprietarioDTO);
        ProprietarioModel modelSalvo = proprietarioDAO.save(modelParaSalvar);
        return proprietarioDTOMapper.map(modelSalvo);
    }

    public ProprietarioDTO listarProprietarioPorId (Long id) throws SQLException {
        ProprietarioModel proprietarioModel = proprietarioDAO.findById(id);
        if (proprietarioModel == null) {
            return null;
        }
        return proprietarioDTOMapper.map(proprietarioModel);
    }

    public List<ProprietarioDTO> listarTodosProprietarios() throws SQLException {
        List<ProprietarioModel> todosProprietariosModel = proprietarioDAO.findAll();
        List<ProprietarioDTO> todosProprietariosDTO = new ArrayList<ProprietarioDTO>();
        for (ProprietarioModel model : todosProprietariosModel) {
            ProprietarioDTO dto = proprietarioDTOMapper.map(model);
            todosProprietariosDTO.add(dto);
        }
        return todosProprietariosDTO;
    }

    public ProprietarioDTO atualizarProprietario(Long id, ProprietarioDTO proprietarioDTO) throws SQLException {
        ProprietarioModel proprietarioExistente = proprietarioDAO.findById(id);
        if (proprietarioExistente == null) {
            throw new RuntimeException("Proprietário não encontrado com o ID: " + id);
        }
        proprietarioExistente.atualizarProprietarioComDTO(proprietarioDTO);
        proprietarioDAO.update(proprietarioExistente);
        return proprietarioDTOMapper.map(proprietarioExistente);
    }

    public void deleteProprietario(Long id) throws SQLException {
        proprietarioDAO.delete(id);
    }
}
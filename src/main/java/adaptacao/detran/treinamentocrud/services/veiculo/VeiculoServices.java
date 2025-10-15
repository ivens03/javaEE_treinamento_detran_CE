package adaptacao.detran.treinamentocrud.services.veiculo;

import adaptacao.detran.treinamentocrud.dto.veiculo.VeiculoDTO;
import adaptacao.detran.treinamentocrud.dto.veiculo.VeiculoMapper;
import adaptacao.detran.treinamentocrud.model.veiculo.VeiculoModel;
import adaptacao.detran.treinamentocrud.repository.veiculo.VeiculoDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VeiculoServices {

    private final VeiculoDAO veiculoDAO;
    private final VeiculoMapper veiculoMapper;

    public VeiculoServices(VeiculoDAO veiculoDAO, VeiculoMapper veiculoMapper) {
        this.veiculoDAO = veiculoDAO;
        this.veiculoMapper = veiculoMapper;
    }

    public VeiculoDTO saveVeiculo(VeiculoDTO veiculoDTO) throws SQLException {
        VeiculoModel modelParaSalvar = veiculoMapper.map(veiculoDTO);
        VeiculoModel modelSalvo = veiculoDAO.save(modelParaSalvar);
        return veiculoMapper.map(modelSalvo);
    }

    public VeiculoDTO listarVeiculoPorId(Long id) throws SQLException {
        VeiculoModel veiculoModel = veiculoDAO.findById(id);
        if (veiculoModel == null) {
            return null;
        }
        return veiculoMapper.map(veiculoModel);
    }

    public List<VeiculoDTO> listarTodosVeiculos() throws SQLException {
        List<VeiculoModel> todosVeiculosModel = veiculoDAO.findAll();
        List<VeiculoDTO> todosVeiculosDTO = new ArrayList<VeiculoDTO>();
        for (VeiculoModel model : todosVeiculosModel) {
            VeiculoDTO dto = veiculoMapper.map(model);
            todosVeiculosDTO.add(dto);
        }
        return todosVeiculosDTO;
    }

    public VeiculoDTO atualizarVeiculo(Long id, VeiculoDTO veiculoDTO) throws SQLException {
        VeiculoModel veiculoExistente = veiculoDAO.findById(id);
        if (veiculoExistente == null) {
            throw new RuntimeException("Veículo não encontrado com o ID: " + id);
        }
        veiculoExistente.atualizarVeiculoComDTO(veiculoDTO);
        veiculoDAO.update(veiculoExistente);
        return veiculoMapper.map(veiculoExistente);
    }

    public void deleteVeiculo(Long id) throws SQLException {
        veiculoDAO.delete(id);
    }
}
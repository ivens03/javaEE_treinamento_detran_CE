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

    /**
     * Salva um novo veículo no banco de dados.
     * @param veiculoDTO O DTO do veículo a ser salvo.
     * @return O DTO do veículo após ser salvo (com o ID gerado).
     * @throws SQLException Em caso de erro de banco de dados.
     */
    public VeiculoDTO saveVeiculo(VeiculoDTO veiculoDTO) throws SQLException {
        VeiculoModel modelParaSalvar = veiculoMapper.map(veiculoDTO);
        VeiculoModel modelSalvo = veiculoDAO.save(modelParaSalvar);
        return veiculoMapper.map(modelSalvo);
    }

    /**
     * Busca um veículo pelo seu ID.
     * @param id O ID do veículo.
     * @return O DTO do veículo encontrado.
     * @throws SQLException Em caso de erro de banco de dados.
     */
    public VeiculoDTO listarVeiculoPorId(Long id) throws SQLException {
        VeiculoModel veiculoModel = veiculoDAO.findById(id);
        return veiculoMapper.map(veiculoModel);
    }

    /**
     * Lista todos os veículos cadastrados.
     * @return Uma lista de DTOs de todos os veículos.
     * @throws SQLException Em caso de erro de banco de dados.
     */
    public List<VeiculoDTO> listarTodosVeiculos() throws SQLException {
        List<VeiculoModel> todosVeiculosModel = veiculoDAO.findAll();
        List<VeiculoDTO> todosVeiculosDTO = new ArrayList<>();
        for (VeiculoModel model : todosVeiculosModel) {
            VeiculoDTO dto = veiculoMapper.map(model);
            todosVeiculosDTO.add(dto);
        }
        return todosVeiculosDTO;
    }

    /**
     * Atualiza os dados de um veículo existente.
     * @param id O ID do veículo a ser atualizado.
     * @param veiculoDTO O DTO com os novos dados do veículo.
     * @return O DTO do veículo após a atualização.
     * @throws SQLException Em caso de erro de banco de dados.
     */
    public VeiculoDTO atualizarVeiculo(Long id, VeiculoDTO veiculoDTO) throws SQLException {
        VeiculoModel veiculoExistente = veiculoDAO.findById(id);
        if (veiculoExistente == null) {
            throw new RuntimeException("Veículo não encontrado com o ID: " + id);
        }

        // Supõe-se a existência de um método similar ao do ProprietarioModel
        // para atualizar os dados a partir de um DTO.
        // Você precisará criar este método no seu VeiculoModel.
        // veiculoExistente.atualizarVeiculoComDTO(veiculoDTO);

        // Alternativa (se não criar o método acima): Mapear manualmente
        veiculoExistente.setPlaca(veiculoDTO.getPlaca());
        veiculoExistente.setRenavam(veiculoDTO.getRenavam());
        // A lógica para atualizar o proprietário dependerá do seu DTO
        // veiculoExistente.setProprietario(...)

        veiculoDAO.update(veiculoExistente);
        return veiculoMapper.map(veiculoExistente);
    }

    /**
     * Deleta um veículo pelo seu ID.
     * @param id O ID do veículo a ser deletado.
     * @throws SQLException Em caso de erro de banco de dados.
     */
    public void deleteVeiculo(Long id) throws SQLException {
        veiculoDAO.delete(id);
    }
}
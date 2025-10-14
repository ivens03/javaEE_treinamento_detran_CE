package adaptacao.detran.treinamentocrud.controller.veiculo;

import adaptacao.detran.treinamentocrud.dto.veiculo.VeiculoDTO;
import adaptacao.detran.treinamentocrud.services.veiculo.VeiculoServices;

import java.sql.SQLException;
import java.util.List;

public class VeiculoController {

    private final VeiculoServices veiculoServices;

    public VeiculoController(VeiculoServices veiculoServices) {
        this.veiculoServices = veiculoServices;
    }

    public VeiculoDTO saveVeiculo(VeiculoDTO veiculoDTO) throws SQLException {
        return veiculoServices.saveVeiculo(veiculoDTO);
    }

    public VeiculoDTO listarVeiculoPorId(Long id) throws SQLException {
        return veiculoServices.listarVeiculoPorId(id);
    }

    public List<VeiculoDTO> listarTodosVeiculos() throws SQLException {
        return veiculoServices.listarTodosVeiculos();
    }

    public VeiculoDTO atualizarVeiculo(Long id, VeiculoDTO veiculoDTO) throws SQLException {
        return veiculoServices.atualizarVeiculo(id, veiculoDTO);
    }

    public void deleteVeiculo(Long id) throws SQLException {
        veiculoServices.deleteVeiculo(id);
    }
}
package adaptacao.detran.treinamentocrud.repository.veiculo;

import adaptacao.detran.treinamentocrud.model.veiculo.VeiculoModel;
import com.ibatis.sqlmap.client.SqlMapClient;

import java.sql.SQLException;
import java.util.List;

public class VeiculoDAO {

    private final SqlMapClient sqlMapClient;

    public VeiculoDAO(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }

    public VeiculoModel findById(Long id) throws SQLException {
        // O namespace "VeiculoMapper" será definido no arquivo XML
        return (VeiculoModel) sqlMapClient.queryForObject("VeiculoMapper.findById", id);
    }

    @SuppressWarnings("unchecked")
    public List<VeiculoModel> findAll() throws SQLException {
        return sqlMapClient.queryForList("VeiculoMapper.findAll");
    }

    public VeiculoModel save(VeiculoModel veiculo) throws SQLException {
        sqlMapClient.insert("VeiculoMapper.save", veiculo);
        return veiculo;
    }

    public void update(VeiculoModel veiculo) throws SQLException {
        sqlMapClient.update("VeiculoMapper.update", veiculo);
    }

    public void delete(Long id) throws SQLException {
        sqlMapClient.delete("VeiculoMapper.delete", id);
    }

}

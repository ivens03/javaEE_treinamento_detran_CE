package adaptacao.detran.treinamentocrud.repository.proprietario;

import adaptacao.detran.treinamentocrud.model.proprietario.ProprietarioModel;
import com.ibatis.sqlmap.client.SqlMapClient;

import java.sql.SQLException;
import java.util.List;

public class ProprietarioDAO {

    private final SqlMapClient sqlMapClient;

    public ProprietarioDAO(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }

    public ProprietarioModel findById(Long id) throws SQLException {
        return (ProprietarioModel) sqlMapClient.queryForObject("findById", id);
    }

    @SuppressWarnings("unchecked")
    public List<ProprietarioModel> findAll() throws SQLException {
        return sqlMapClient.queryForList("findAll");
    }

    public ProprietarioModel save(ProprietarioModel proprietario) throws SQLException {
        sqlMapClient.insert("save", proprietario);
        return proprietario;
    }

    public void update(ProprietarioModel proprietario) throws SQLException {
        sqlMapClient.update("update", proprietario);
    }

    public void delete(Long id) throws SQLException {
        sqlMapClient.delete("delete", id);
    }
}
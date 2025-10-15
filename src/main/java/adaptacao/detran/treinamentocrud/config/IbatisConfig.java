package adaptacao.detran.treinamentocrud.config;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

import java.io.IOException;
import java.io.Reader;

public class IbatisConfig {

    private static final SqlMapClient sqlMapClient;

    static {
        try {
            String resource = "sql-map-config.xml";
            Reader reader = Resources.getResourceAsReader(resource);
            sqlMapClient = SqlMapClientBuilder.buildSqlMapClient(reader);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao construir o SqlMapClient: " + e, e);
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static SqlMapClient getSqlMapClient() {
        return sqlMapClient;
    }

}

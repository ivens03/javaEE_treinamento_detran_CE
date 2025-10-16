package adaptacao.detran.treinamentocrud.config;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

import java.io.IOException;
import java.io.Reader;

public class IbatisConfig {

    private static final SqlMapClient sqlMapClient;

    static {
        //Reader reader = null;
        try {
            String resource = "/home/ivens/desenvolvimento/projetos/java/treinamentoCRUD/src/main/resources/sql-map-config.xml";
            Reader reader = Resources.getResourceAsReader(resource);
            sqlMapClient = SqlMapClientBuilder.buildSqlMapClient(reader);
        } catch (IOException e) {
            e.printStackTrace();
            // Lança um erro fatal claro
            throw new ExceptionInInitializerError("Erro CRÍTICO ao inicializar o iBATIS: Falha ao carregar XMLs ou Properties: " + e.getMessage());
        } catch (Throwable e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError("Erro CRÍTICO na inicialização do SqlMapClient: " + e.getMessage());
        }
    }

    public static SqlMapClient getSqlMapClient() {
        return sqlMapClient;
    }

}
package adaptacao.detran.treinamentocrud.model.veiculo;

import java.io.Serializable;
import java.util.Date;


public class LogModel implements Serializable {


    private Long id;
    private Date dataCriacao;
    private Date dataAlteracao;
    private VeiculoModel veiculo;

    public LogModel() {
    }

    public LogModel(Long id, Date dataCriacao, Date dataAlteracao, VeiculoModel veiculo) {
        this.id = id;
        this.dataCriacao = dataCriacao;
        this.dataAlteracao = dataAlteracao;
        this.veiculo = veiculo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Date getDataAlteracao() {
        return dataAlteracao;
    }

    public void setDataAlteracao(Date dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }

    public VeiculoModel getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(VeiculoModel veiculo) {
        this.veiculo = veiculo;
    }
}

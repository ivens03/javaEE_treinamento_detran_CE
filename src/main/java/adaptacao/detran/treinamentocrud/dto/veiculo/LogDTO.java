package adaptacao.detran.treinamentocrud.dto.veiculo;

import adaptacao.detran.treinamentocrud.model.veiculo.VeiculoModel;

import java.util.Date;

public class LogDTO {

    private Long id;
    private Date dataCriacao;
    private Date dataAlteracao;
    private VeiculoModel veiculoModel;

    public LogDTO() {
    }

    public LogDTO(Long id, Date dataCriacao, Date dataAlteracao, VeiculoModel veiculoModel) {
        this.id = id;
        this.dataCriacao = dataCriacao;
        this.dataAlteracao = dataAlteracao;
        this.veiculoModel = veiculoModel;
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

    public VeiculoModel getVeiculoModel() {
        return veiculoModel;
    }

    public void setVeiculoModel(VeiculoModel veiculoModel) {
        this.veiculoModel = veiculoModel;
    }

    @Override
    public String toString() {
        return "LogDTO{" +
                "id=" + id +
                ", dataCriacao=" + dataCriacao +
                ", dataAlteracao=" + dataAlteracao +
                ", veiculoModel=" + veiculoModel +
                '}';
    }
}

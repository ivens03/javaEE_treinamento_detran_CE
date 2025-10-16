package adaptacao.detran.treinamentocrud.dto.veiculo;

import adaptacao.detran.treinamentocrud.dto.proprietario.ProprietarioDTO;
import adaptacao.detran.treinamentocrud.model.proprietario.ProprietarioModel;

public class VeiculoDTO {

    private Long id;
    private String placa;
    private String renavam;
    private ProprietarioModel proprietario;

    public VeiculoDTO() {
    }

    public VeiculoDTO(Long id, String placa, String renavam, ProprietarioModel proprietario) {
        this.id = id;
        this.placa = placa;
        this.renavam = renavam;
        this.proprietario = proprietario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getRenavam() {
        return renavam;
    }

    public void setRenavam(String renavam) {
        this.renavam = renavam;
    }

    public ProprietarioModel getProprietario() {
        return proprietario;
    }

    public void setProprietario(ProprietarioModel proprietario) {
        this.proprietario = proprietario;
    }

    @Override
    public String toString() {
        return "VeiculoDTO{" +
                "id=" + id +
                ", placa='" + placa + '\'' +
                ", renavam='" + renavam + '\'' +
                ", proprietario=" + proprietario +
                '}';
    }
}

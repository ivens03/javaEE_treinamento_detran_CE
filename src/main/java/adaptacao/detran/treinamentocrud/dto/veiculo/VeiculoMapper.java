package adaptacao.detran.treinamentocrud.dto.veiculo;

import adaptacao.detran.treinamentocrud.model.veiculo.VeiculoModel;

public class VeiculoMapper {

    public VeiculoModel map(VeiculoDTO veiculoDTO) {

        VeiculoModel veiculoModel = new VeiculoModel();

        veiculoModel.setId(veiculoDTO.getId());
        veiculoModel.setPlaca(veiculoDTO.getPlaca());
        veiculoModel.setRenavam(veiculoDTO.getRenavam());
        veiculoModel.setProprietario(veiculoDTO.getProprietario());

        return veiculoModel;
    }

    public VeiculoDTO map(VeiculoModel veiculoModel) {

        VeiculoDTO veiculoDTO = new VeiculoDTO();

        veiculoDTO.setId(veiculoModel.getId());
        veiculoDTO.setPlaca(veiculoModel.getPlaca());
        veiculoDTO.setRenavam(veiculoModel.getRenavam());
        veiculoDTO.setProprietario(veiculoModel.getProprietario());

        return veiculoDTO;
    }

}

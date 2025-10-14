package adaptacao.detran.treinamentocrud.dto.veiculo;

import adaptacao.detran.treinamentocrud.model.veiculo.LogModel;

public class LogMapper {

    private final VeiculoMapper veiculoMapper;

    public LogMapper(VeiculoMapper veiculoMapper) {
        this.veiculoMapper = veiculoMapper;
    }

    public LogModel map(LogDTO logDTO) {

        LogModel logModel = new LogModel();

        logModel.setId(logDTO.getId());
        logModel.setDataCriacao(logDTO.getDataCriacao());
        logModel.setDataAlteracao(logDTO.getDataAlteracao());
        logModel.setVeiculo(logDTO.getVeiculoModel());
        return  logModel;
    }

    public LogDTO map(LogModel logModel) {

        LogDTO logDTO = new LogDTO();

        logDTO.setId(logModel.getId());
        logDTO.setDataCriacao(logModel.getDataCriacao());
        logDTO.setDataAlteracao(logModel.getDataAlteracao());
        logDTO.setVeiculoModel(logModel.getVeiculo());
        return  logDTO;
    }
}

package adaptacao.detran.treinamentocrud.dto.proprietario;

import adaptacao.detran.treinamentocrud.model.proprietario.ProprietarioModel;

public class ProprietarioDTOMapper {

    public ProprietarioModel map(ProprietarioDTO proprietarioDTO) {

        ProprietarioModel proprietarioModel = new ProprietarioModel();

        proprietarioModel.setId(proprietarioDTO.getId());
        proprietarioModel.setCpf_cnpj(proprietarioDTO.getCpf_cnpj());
        proprietarioModel.setNome(proprietarioDTO.getNome());
        proprietarioModel.setEndereco(proprietarioDTO.getEndereco());

        return proprietarioModel;
    }

    public ProprietarioDTO map(ProprietarioModel proprietarioModel) {

        ProprietarioDTO proprietarioDTO = new ProprietarioDTO();

        proprietarioDTO.setId(proprietarioModel.getId());
        proprietarioDTO.setCpf_cnpj(proprietarioModel.getCpf_cnpj());
        proprietarioDTO.setNome(proprietarioModel.getNome());
        proprietarioDTO.setEndereco(proprietarioModel.getEndereco());

        return proprietarioDTO;
    }

}

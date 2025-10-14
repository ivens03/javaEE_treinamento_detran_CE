package adaptacao.detran.treinamentocrud.model.proprietario;

import adaptacao.detran.treinamentocrud.dto.proprietario.ProprietarioDTO;

import java.io.Serializable;

public class ProprietarioModel implements Serializable {


    private Long id;
    private String cpf_cnpj;
    private String nome;
    private String endereco;

    public ProprietarioModel() {
    }

    public ProprietarioModel(Long id, String cpf_cnpj, String nome, String endereco) {
        this.id = id;
        this.cpf_cnpj = cpf_cnpj;
        this.nome = nome;
        this.endereco = endereco;
    }

    /// <summary>
    /// Atualiza o proprietário com os dados do DTO caso o valor seja diferente de null não atualiza.
    /// </summary>
    /// <param name="proprietarioDTO"></param>
    public void atualizarProprietarioComDTO(ProprietarioDTO proprietarioDTO) {
        if (proprietarioDTO.getCpf_cnpj() != null) {
            setCpf_cnpj(proprietarioDTO.getCpf_cnpj());
        }
        if (proprietarioDTO.getNome() != null) {
            setNome(proprietarioDTO.getNome());
        }
        if (proprietarioDTO.getEndereco() != null) {
            setEndereco(proprietarioDTO.getEndereco());
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCpf_cnpj() {
        return cpf_cnpj;
    }

    public void setCpf_cnpj(String cpf_cnpj) {
        this.cpf_cnpj = cpf_cnpj;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
}

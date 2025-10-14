package adaptacao.detran.treinamentocrud.dto.proprietario;

public class ProprietarioDTO {

    private Long id;
    private String cpf_cnpj;
    private String nome;
    private String endereco;

    public ProprietarioDTO() {
    }

    public ProprietarioDTO(Long id, String cpf_cnpj, String nome, String endereco) {
        this.id = id;
        this.cpf_cnpj = cpf_cnpj;
        this.nome = nome;
        this.endereco = endereco;
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

    @Override
    public String toString() {
        return "ProprietarioDTO{" +
                "id=" + id +
                ", cpf_cnpj='" + cpf_cnpj + '\'' +
                ", nome='" + nome + '\'' +
                ", endereco='" + endereco + '\'' +
                '}';
    }
}

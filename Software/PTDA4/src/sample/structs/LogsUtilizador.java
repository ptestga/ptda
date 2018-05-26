package sample.structs;

public class LogsUtilizador {
    private String id, tipo, data, utilizador, id_utilizador, password, nome, morada,
    cod_postal, localidade, nacionalidade, nif, cc, sexo, data_nascimento, contacto, mail, funcao;

    public LogsUtilizador(String id, String tipo, String data, String utilizador, String id_utilizador, String password,
                          String nome, String morada, String cod_postal, String localidade, String nacionalidade,
                          String nif, String cc, String sexo, String data_nascimento, String contacto, String mail, String funcao) {
        this.id = id;
        this.tipo = tipo;
        this.data = data;
        this.utilizador = utilizador;
        this.id_utilizador = id_utilizador;
        this.password = password;
        this.nome = nome;
        this.morada = morada;
        this.cod_postal = cod_postal;
        this.localidade = localidade;
        this.nacionalidade = nacionalidade;
        this.nif = nif;
        this.cc = cc;
        this.sexo = sexo;
        this.data_nascimento = data_nascimento;
        this.contacto = contacto;
        this.mail = mail;
        this.funcao = funcao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getUtilizador() {
        return utilizador;
    }

    public void setUtilizador(String utilizador) {
        this.utilizador = utilizador;
    }

    public String getId_utilizador() {
        return id_utilizador;
    }

    public void setId_utilizador(String id_utilizador) {
        this.id_utilizador = id_utilizador;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMorada() {
        return morada;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public String getCod_postal() {
        return cod_postal;
    }

    public void setCod_postal(String cod_postal) {
        this.cod_postal = cod_postal;
    }

    public String getLocalidade() {
        return localidade;
    }

    public void setLocalidade(String localidade) {
        this.localidade = localidade;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getData_nascimento() {
        return data_nascimento;
    }

    public void setData_nascimento(String data_nascimento) {
        this.data_nascimento = data_nascimento;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }
}

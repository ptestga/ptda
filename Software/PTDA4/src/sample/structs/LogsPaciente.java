package sample.structs;

public class LogsPaciente {
    private String id, tipo, data, utilizador, id_paciente, id_utilizador, em_tratamento, em_treino;

    public LogsPaciente(String id, String tipo, String data, String utilizador, String id_paciente, String id_utilizador, String em_tratamento, String em_treino) {
        this.id = id;
        this.tipo = tipo;
        this.data = data;
        this.utilizador = utilizador;
        this.id_paciente = id_paciente;
        this.id_utilizador = id_utilizador;
        this.em_tratamento = em_tratamento;
        this.em_treino = em_treino;
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

    public String getId_paciente() {
        return id_paciente;
    }

    public void setId_paciente(String id_paciente) {
        this.id_paciente = id_paciente;
    }

    public String getId_utilizador() {
        return id_utilizador;
    }

    public void setId_utilizador(String id_utilizador) {
        this.id_utilizador = id_utilizador;
    }

    public String getEm_tratamento() {
        return em_tratamento;
    }

    public void setEm_tratamento(String em_tratamento) {
        this.em_tratamento = em_tratamento;
    }

    public String getEm_treino() {
        return em_treino;
    }

    public void setEm_treino(String em_treino) {
        this.em_treino = em_treino;
    }
}

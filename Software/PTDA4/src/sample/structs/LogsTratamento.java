package sample.structs;

public class LogsTratamento {
    private String id, tipo, data, utilizador, id_tratamento, desc_tratamento, notas_tratamento, data_inicio,
    data_fim, paciente, medico;

    public LogsTratamento(String id, String tipo, String data, String utilizador, String id_tratamento,
                          String desc_tratamento, String notas_tratamento, String data_inicio, String data_fim,
                          String paciente, String medico) {
        this.id = id;
        this.tipo = tipo;
        this.data = data;
        this.utilizador = utilizador;
        this.id_tratamento = id_tratamento;
        this.desc_tratamento = desc_tratamento;
        this.notas_tratamento = notas_tratamento;
        this.data_inicio = data_inicio;
        this.data_fim = data_fim;
        this.paciente = paciente;
        this.medico = medico;
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

    public String getId_tratamento() {
        return id_tratamento;
    }

    public void setId_tratamento(String id_tratamento) {
        this.id_tratamento = id_tratamento;
    }

    public String getDesc_tratamento() {
        return desc_tratamento;
    }

    public void setDesc_tratamento(String desc_tratamento) {
        this.desc_tratamento = desc_tratamento;
    }

    public String getNotas_tratamento() {
        return notas_tratamento;
    }

    public void setNotas_tratamento(String notas_tratamento) {
        this.notas_tratamento = notas_tratamento;
    }

    public String getData_inicio() {
        return data_inicio;
    }

    public void setData_inicio(String data_inicio) {
        this.data_inicio = data_inicio;
    }

    public String getData_fim() {
        return data_fim;
    }

    public void setData_fim(String data_fim) {
        this.data_fim = data_fim;
    }

    public String getPaciente() {
        return paciente;
    }

    public void setPaciente(String paciente) {
        this.paciente = paciente;
    }

    public String getMedico() {
        return medico;
    }

    public void setMedico(String medico) {
        this.medico = medico;
    }
}

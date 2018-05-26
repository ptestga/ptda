package sample.structs;

public class LogsTreino {
    private String id, tipo, data, utilizador, id_treino, desc_treino, notas_treino, data_inicio,
            data_fim, fisioterapeuta, tratamento;

    public LogsTreino(String id, String tipo, String data, String utilizador, String id_treino, String desc_treino,
                      String notas_treino, String data_inicio, String data_fim, String fisioterapeuta, String tratamento) {
        this.id = id;
        this.tipo = tipo;
        this.data = data;
        this.utilizador = utilizador;
        this.id_treino = id_treino;
        this.desc_treino = desc_treino;
        this.notas_treino = notas_treino;
        this.data_inicio = data_inicio;
        this.data_fim = data_fim;
        this.fisioterapeuta = fisioterapeuta;
        this.tratamento = tratamento;
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

    public String getId_treino() {
        return id_treino;
    }

    public void setId_treino(String id_treino) {
        this.id_treino = id_treino;
    }

    public String getDesc_treino() {
        return desc_treino;
    }

    public void setDesc_treino(String desc_treino) {
        this.desc_treino = desc_treino;
    }

    public String getNotas_treino() {
        return notas_treino;
    }

    public void setNotas_treino(String notas_treino) {
        this.notas_treino = notas_treino;
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

    public String getFisioterapeuta() {
        return fisioterapeuta;
    }

    public void setFisioterapeuta(String fisioterapeuta) {
        this.fisioterapeuta = fisioterapeuta;
    }

    public String getTratamento() {
        return tratamento;
    }

    public void setTratamento(String tratamento) {
        this.tratamento = tratamento;
    }
}

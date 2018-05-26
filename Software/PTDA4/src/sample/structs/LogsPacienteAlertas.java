package sample.structs;

public class LogsPacienteAlertas {
    private String id, tipo, data, utilizador, id_paciente, min_press_min, max_press_min, min_press_max, max_press_max,
    freq_min, freq_max;

    public LogsPacienteAlertas(String id, String tipo, String data, String utilizador, String id_paciente,
                               String min_press_min, String max_press_min, String min_press_max, String max_press_max,
                               String freq_min, String freq_max) {
        this.id = id;
        this.tipo = tipo;
        this.data = data;
        this.utilizador = utilizador;
        this.id_paciente = id_paciente;
        this.min_press_min = min_press_min;
        this.max_press_min = max_press_min;
        this.min_press_max = min_press_max;
        this.max_press_max = max_press_max;
        this.freq_min = freq_min;
        this.freq_max = freq_max;
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

    public String getMin_press_min() {
        return min_press_min;
    }

    public void setMin_press_min(String min_press_min) {
        this.min_press_min = min_press_min;
    }

    public String getMax_press_min() {
        return max_press_min;
    }

    public void setMax_press_min(String max_press_min) {
        this.max_press_min = max_press_min;
    }

    public String getMin_press_max() {
        return min_press_max;
    }

    public void setMin_press_max(String min_press_max) {
        this.min_press_max = min_press_max;
    }

    public String getMax_press_max() {
        return max_press_max;
    }

    public void setMax_press_max(String max_press_max) {
        this.max_press_max = max_press_max;
    }

    public String getFreq_min() {
        return freq_min;
    }

    public void setFreq_min(String freq_min) {
        this.freq_min = freq_min;
    }

    public String getFreq_max() {
        return freq_max;
    }

    public void setFreq_max(String freq_max) {
        this.freq_max = freq_max;
    }
}

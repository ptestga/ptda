package sample.connectivity;

import java.lang.Thread;
import java.time.ZonedDateTime;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Esta classe é responsável pela comunicação entre as interfaces e a base de dados.
 * É também nesta classe que estão os métodos responsáveis pela manipulação da base de dados bem como todas as queries.
 */
public class Driver
{
    private Connection connection;

    /**
     * O construtor desta classe tenta fazer login com os dados fornecidos pelo utilizador.
     *
     * @param username NIF do utilizador
     * @param pwd Password
     */
    public Driver(String username, String pwd)
    {
        connection = login(username, pwd);
    }

    /**
     * Método responsável pelo login na base de dados.
     *
     * @param username NIF do utilizador
     * @param pwd Password
     * @return Este método devolve um objecto da classe connection que contém a ligação com a base de dados.
     */
    private Connection login(String username, String pwd)
    {
        try
        {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://127.0.0.1:5432/ptda4";

            return DriverManager.getConnection(url, username, pwd);
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Método responsável pelo logout dos users na base de dados.
     *
     * @return Devolve true se o logout for bem feito ou false caso haja um erro no processo.
     */
    public boolean logout()
    {
        try
        {
            connection.close();
            connection = null;
            return true;
        } catch (Exception e)
        {
            return false;
        }
    }

    /**
     * Método que verifica se o user está ligado à base de dados.
     *
     * @return Devolve true se o utilizador estiver ligado ou false caso não o esteja.
     */
    public boolean isConnected()
    {
        return connection != null;
    }

    /**
     * Método responsável pela a adição de pacientes à base de dados.
     *
     * @param pwd Password
     * @param nome Nome Completo
     * @param rua Morada
     * @param codpos Código Postal
     * @param localidade Localidade
     * @param nacionalidade Nacionalidade
     * @param nif Número de Identificação Fiscal
     * @param cc Cartão de Cidadão
     * @param sexo Género
     * @param dataNascimento Data de Nascimento
     * @param tlf Telefone
     * @param email Email
     * @return Devolve true se adicionar com sucesso ou false se falhar na adição.
     */
    public boolean addPac(String pwd, String nome, String rua, String codpos, String localidade, String nacionalidade,
                          String nif, String cc, String sexo, String dataNascimento, String tlf, String email)
    {
        String sql = "INSERT INTO vw_paciente VALUES(DEFAULT,'n','n',DEFAULT,'" + pwd + "','" + nome + "','" + rua + "','" +
                codpos + "','" + localidade + "','" + nacionalidade + "','" + nif + "','" + cc + "','" + sexo + "','" +
                dataNascimento + "','" + tlf + "','" + email + "','paciente'); " +
                "CREATE ROLE \"" + nif + "\" ENCRYPTED PASSWORD '" + pwd + "' LOGIN INHERIT; " +
                "GRANT paciente to \"" + nif + "\";";

        try
        {
            Statement statement = connection.createStatement();
            statement.execute(sql);

            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }

    /**
     * Método responsável pela a adição de funcionários à base de dados.
     *
     * @param data Data de admissão
     * @param activo Estado do funcionário
     * @param pwd Password
     * @param nome Nome Completo
     * @param rua Morada
     * @param codpos Código Postal
     * @param localidade Localidade
     * @param nacionalidade Nacionalidade
     * @param nif Número de Identificação Fiscal
     * @param cc Cartão de Cidadão
     * @param sexo Género
     * @param dataNascimento Data de Nascimento
     * @param tlf Telefone
     * @param email Email
     * @param funcao Cargo do funcionário
     * @return Devolve true se adicionar com sucesso ou false se falhar na adição.
     */
    public boolean addFunc(String data, String activo, String pwd, String nome, String rua, String codpos,
                           String localidade, String nacionalidade, String nif, String cc, String sexo, String dataNascimento,
                           String tlf, String email, String funcao)
    {
        String sql = "INSERT INTO vw_func VALUES(DEFAULT,'" + data + "','" + activo + "',DEFAULT,'" + pwd + "','" + nome + "','" + rua + "','" +
                codpos + "','" + localidade + "','" + nacionalidade + "','" + nif + "','" + cc + "','" + sexo + "','" +
                dataNascimento + "','" + tlf + "','" + email + "','" + funcao + "'); " +
                "CREATE ROLE \"" + nif + "\" ENCRYPTED PASSWORD '" + pwd;

        if(funcao.equals("administrador"))
            sql = sql + "' LOGIN INHERIT CREATEROLE; " +
                    "GRANT " + funcao + " to \"" + nif + "\";";
        else
            sql = sql + "' LOGIN INHERIT; " +
                    "GRANT " + funcao + " to \"" + nif + "\";";

        try
        {
            Statement statement = connection.createStatement();
            statement.execute(sql);

            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }

    /**
     * Método responsável pela alteração dos dados dos pacientes.
     *
     * @param pacienteID Identificação do paciente
     * @param pwd Password
     * @param nome Nome completo
     * @param rua Morada
     * @param codpos Código
     * @param localidade Localidade
     * @param nacionalidade Nacionalidade
     * @param sexo Género
     * @param dataNascimento Data de nascimento
     * @param tlf Telefone
     * @param email Email
     * @return Devolve true se alterar os dados com sucesso ou false se falhar na alteração.
     */
    public boolean editPaciente(String pacienteID, String nome, String rua, String codpos, String localidade, String nacionalidade,
                                String sexo, String dataNascimento, String tlf, String email)
    {
        String sql = "UPDATE vw_paciente SET nome = '" + nome + "', morada = '" + rua +
                "', cod_postal = '" + codpos + "', localidade = '" + localidade + "', nacionalidade = '" + nacionalidade +
                "', sexo = '" + sexo + "', data_nascimento = '" + dataNascimento + "', contacto = '" +
                tlf + "', mail = '" + email + "' WHERE id_paciente = '" + pacienteID + "';";
        try
        {
            Statement statement = connection.createStatement();
            statement.execute(sql);

            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }

    /**
     * Método responsável pela alteração dos dados dos funcionários.
     *
     * @param funcID ID do funcionário
     * @param pwd Password
     * @param nome Nome completo
     * @param rua Morada
     * @param codpos Código
     * @param localidade Localidade
     * @param nacionalidade Nacionalidade
     * @param sexo Género
     * @param dataNascimento Data de nascimento
     * @param tlf Telefone
     * @param email Email
     * @return Devolve true se alterar os dados com sucesso ou false se falhar na alteração.
     */
    public boolean editFunc(String funcID, String nome, String rua, String codpos, String localidade, String nacionalidade,
                                String sexo, String dataNascimento, String tlf, String email)
    {
        String sql = "UPDATE vw_func SET nome = '" + nome + "', morada = '" + rua +
                "', cod_postal = '" + codpos + "', localidade = '" + localidade + "', nacionalidade = '" + nacionalidade +
                "', sexo = '" + sexo + "', data_nascimento = '" + dataNascimento + "', contacto = '" +
                tlf + "', mail = '" + email + "' WHERE id_funcionario = '" + funcID + "';";
        try
        {
            Statement statement = connection.createStatement();
            statement.execute(sql);

            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }

    /**
     * Método responsável pela a execução de queries à base de dados. Criado para poupar código e facilitar as queries.
     *
     * @param sql String que contém a querie a ser executada.
     * @return Devolve o ResultSet com os dados da querie.
     */
    private ResultSet queryDB(String sql)
    {
        try
        {
            Statement statement = connection.createStatement();
            return statement.executeQuery(sql);
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Método que devolve o tipo de utilizador logado (paciente, fisioterapeuta, médico, administrador).
     *
     * @param nif NIF do utilizador
     * @return Devolve o tipo de utilizador logado (paciente, fisioterapeuta, médico, administrador)
     */
    public String userType(String nif)
    {
        try
        {
            String sql = "SELECT funcao FROM utilizador WHERE utilizador.nif = '" + nif + "';";
            String result = null;
            ResultSet rs = queryDB(sql);

            if(rs.next())
            {
                result = rs.getString("funcao");
            }

            return result;
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Método que devolve a view vw_paciente em formato de ResultSet.
     *
     * @return Devolve a view vw_paciente em formato de ResultSet.
     */
    public ResultSet getAllPacientes()
    {
        String sql = "SELECT * FROM vw_paciente;";
        return queryDB(sql);
    }

    /**
     * Método que devolve a view vw_func em formato de ResultSet.
     *
     * @return Devolve a view vw_func em formato de ResultSet.
     */
    public ResultSet getAllFuncionarios()
    {
        String sql = "SELECT * FROM vw_func;";
        return queryDB(sql);
    }

    /**
     * Método que verifica se um paciente tem um tratamento activo associado.
     *
     * @param idPac ID do paciente
     * @return String com a resposta "s" ou "n"
     */
    public String getTratamentoEstado(String idPac)
    {
        String sql = "SELECT em_tratamento FROM paciente WHERE id_paciente = '" + idPac + "';";

        try
        {
            ResultSet rs = queryDB(sql);

            if(rs.next())
            {
                return rs.getString("em_tratamento");
            }

            return null;
        }catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Método que verifica se um paciente tem um treino activo associado.
     *
     * @param idPac ID do paciente
     * @return String com a resposta "s" ou "n"
     */
    public String getTreinoEstado(String idPac)
    {
        String sql = "SELECT em_treino FROM paciente WHERE id_paciente = '" + idPac + "';";

        try
        {
            ResultSet rs = queryDB(sql);

            if(rs.next())
            {
                return rs.getString("em_treino");
            }

            return null;
        }catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Método que devolve todos os pacientes com um tratamento activo.
     *
     * @return ResultSet com informações dos pacientes.
     */
    public ResultSet pacientesComTratamento()
    {
        String sql = "SELECT * FROM vw_paciente WHERE em_tratamento = 's';";
        return queryDB(sql);
    }

    /**
     * Método que devolve todos os pacientes sem um tratamento activo.
     *
     * @return ResultSet com informações dos pacientes.
     */
    public ResultSet pacientesSemTratamento()
    {
        String sql = "SELECT * FROM vw_paciente WHERE em_tratamento = 'n';";
        return queryDB(sql);
    }

    /**
     * Método que devolve todos os dados de um paciente.
     *
     * @param idPaciente ID do paciente
     * @return ResultSet com todas as informações de um paciente
     */
    public ResultSet dadosPaciente(String idPaciente)
    {
        String sql = "SELECT * FROM vw_paciente WHERE id_paciente = '" + idPaciente + "';";
        return queryDB(sql);
    }

    /**
     * Método que devolve todos os dados de um funcionário.
     *
     * @param nifFuncionario NIF do funcionário
     * @return ResultSet com todas as informações de um funcionário
     */
    public ResultSet dadosFuncionario(String nifFuncionario)
    {
        String idFunc = getFuncID(nifFuncionario);

        String sql = "SELECT * FROM vw_func WHERE id_funcionario = '" + idFunc + "';";
        return queryDB(sql);
    }

    /**
     * Método que através do NIF devolve o ID de utilizador.
     *
     * @param nifDoUser NIF do utilizador
     * @return String com o ID do utilizador
     */
    public String getUserID(String nifDoUser)
    {
        String sql = "SELECT id_utilizador FROM utilizador WHERE nif = '" + nifDoUser +"';";

        try
        {
            ResultSet rs = queryDB(sql);

            if(rs.next())
            {
                return rs.getString("id_utilizador");
            }

            return null;
        }catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Método que através do NIF devolve o ID de paciente de um utilizador
     *
     * @param nifDoPaciente NIF de um paciente
     * @return String com o ID de paciente
     */
    public String getPacienteID(String nifDoPaciente)
    {
        String sql = "SELECT id_paciente FROM vw_paciente WHERE nif = '" + nifDoPaciente +"';";

        try
        {
            ResultSet rs = queryDB(sql);

            if(rs.next())
            {
                return rs.getString("id_paciente");
            }

            return null;
        }catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Método que através do NIF devolve o ID de funcionário de um utilizador
     *
     * @param nifDoFunc NIF de um funcionário
     * @return String com o ID de funcionário
     */
    public String getFuncID(String nifDoFunc)
    {
        String sql = "SELECT id_funcionario FROM vw_func WHERE nif = '" + nifDoFunc +"';";

        try
        {
            ResultSet rs = queryDB(sql);

            if(rs.next())
            {
                return rs.getString("id_funcionario");
            }

            return null;
        }catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Método que adiciona um tratamento à base de dados
     *
     * @param descricao Descrição do tratamento
     * @param notas Notas do tratamento
     * @param dataInicio Data de Inicio
     * @param dataFim Data de Fim
     * @param idPaciente ID do paciente
     * @param idMedico ID do médico
     * @return True se for bem sucessida a adição ou false se ocorrer um erro.
     */
    public boolean addTratamento(String descricao, String notas, String dataInicio, String dataFim, String idPaciente, String idMedico)
    {
        String sql = "INSERT INTO tratamento VALUES(DEFAULT,'" + descricao + "','" + notas + "','" + dataInicio + "','" +
                dataFim + "','" + idPaciente + "','" + idMedico + "');";

        try
        {
            Statement statement = connection.createStatement();
            statement.execute(sql);

            return true;
        }catch(Exception e)
        {
            return false;
        }
    }

    /**
     * Método que adiciona um treino à base de dados
     *
     * @param descricao Descrição do treino
     * @param notas Notas do treino
     * @param dataInicio Data de Inicio
     * @param dataFim Data de Fim
     * @param idFisio ID do fisioterapeuta
     * @param idTratamento ID do tratamento a que o treino está associado
     * @return True se for bem sucessida a adição ou false se ocorrer um erro.
     */
    public boolean addTreino(String descricao, String notas, String dataInicio, String dataFim, String idFisio, String idTratamento)
    {
        String sql = "INSERT INTO treino VALUES(DEFAULT,'" + descricao + "','" + notas + "','" + dataInicio + "','" +
                dataFim + "','" + idFisio + "','" + idTratamento + "');";

        try
        {
            Statement statement = connection.createStatement();
            statement.execute(sql);

            return true;
        }catch(Exception e)
        {
            return false;
        }
    }

    /**
     * Método que edita um tratamento existente na base de dados.
     *
     * @param idTrat ID do tratamento a ser editado
     * @param descricao Nova descrição
     * @param notas Novas notas
     * @param dataInicio Nova data de Inicio
     * @param dataFim Nova data de fim
     * @return True se for bem sucessida a alteração ou false se ocorrer um erro.
     */
    public boolean editTratamento(String idTrat, String descricao, String notas, String dataInicio, String dataFim)
    {
        String sql = "UPDATE tratamento SET desc_tratamento = '" + descricao + "', notas_tratamento = '" + notas + "', data_inicio ='" + dataInicio + "', data_fim = '" +
                dataFim + "' WHERE id_tratamento = '" + idTrat + "';";

        try
        {
            Statement statement = connection.createStatement();
            statement.execute(sql);

            return true;
        }catch(Exception e)
        {
            return false;
        }
    }

    /**
     * Método que edita um treino existente na base de dados.
     *
     * @param idTreino ID do treino a ser editado
     * @param descricao Nova descrição
     * @param notas Novas notas
     * @param dataInicio Nova data de Inicio
     * @param dataFim Nova data de fim
     * @return True se for bem sucessida a alteração ou false se ocorrer um erro.
     */
    public boolean editTreino(String idTreino, String descricao, String notas, String dataInicio, String dataFim)
    {
        String sql = "UPDATE treino SET desc_treino = '" + descricao + "', notas_treino = '" + notas + "', data_inicio ='" + dataInicio + "', data_fim = '" +
                dataFim + "' WHERE id_treino = '" + idTreino + "';";

        try
        {
            Statement statement = connection.createStatement();
            statement.execute(sql);

            return true;
        }catch(Exception e)
        {
            return false;
        }
    }

    /**
     * Método que devolve todos os tratamentos de um paciente.
     *
     * @param pacienteID ID do paciente
     * @return ResultSet com todas as informações dos tratamentos associados a um paciente
     */
    public ResultSet getTratamentos(String pacienteID)
    {
        String sql = "SELECT * FROM tratamento WHERE paciente = '" + pacienteID + "';";
        return queryDB(sql);
    }

    /**
     * Método que devolve todas as informações de um tratamento.
     *
     * @param idTrat ID do tratamento
     * @return ResultSet com todas as informações de um tratamento
     */
    public ResultSet getDadosTratamento(String idTrat)
    {
        String sql = "SELECT * FROM tratamento WHERE id_tratamento = '" + idTrat + "';";
        return queryDB(sql);
    }

    /**
     * Método que devolve todos os treinos associados a um tratamento.
     *
     * @param idTratamento ID do tratamento
     * @return ResultSet com todas as informações dos treinos associados a um tratamento
     */
    public ResultSet getTreinos(String idTratamento)
    {
        String sql = "SELECT * FROM treino WHERE tratamento = '" + idTratamento + "';";
        return queryDB(sql);
    }

    /**
     * Método que actualiza um estado de um funcionário (caso este fique activo/desactivo).
     *
     * @param nifFuncionario NIF do funcionário
     * @param estado Estado de empregabilidade
     * @return True se for bem sucessida a alteração ou false se ocorrer um erro.
     */
    public boolean updateEstadoFuncionario(String nifFuncionario, String estado)
    {
        String funcID = getFuncID(nifFuncionario);

        try
        {
            String sql = "UPDATE vw_func SET activo = '" + estado + "' WHERE id_funcionario = '" + funcID + "';";
            Statement statement = connection.createStatement();
            statement.execute(sql);
            return true;
        }catch (Exception e)
        {
            return false;
        }
    }

    /**
     * Método que finaliza o tratamento actual de um paciente.
     *
     * @param pacienteID ID do paciente
     * @return True se for bem sucessida a alteração ou false se ocorrer um erro.
     */
    public boolean finalizarTratamento(String pacienteID)
    {
        try
        {
            String result = null;
            String sql = "SELECT em_tratamento FROM vw_paciente WHERE id_paciente = '" + pacienteID + "';";
            ResultSet rs = queryDB(sql);

            if(rs.next())
            {
                result = rs.getString("em_tratamento");
            }

            if(result.equals("s"))
            {
                sql = "UPDATE vw_paciente SET em_tratamento = 'n' WHERE id_paciente = '" + pacienteID + "';";
                Statement statement = connection.createStatement();
                statement.execute(sql);
                return true;
            } else
            {
                return false;
            }
        }catch (Exception e)
        {
            return false;
        }
    }

    /**
     * Método que finaliza o treino actual de um paciente.
     *
     * @param pacienteID ID do paciente
     * @return True se for bem sucessida a alteração ou false se ocorrer um erro.
     */
    public boolean finalizarTreino(String pacienteID)
    {
        try
        {
            String sql = "UPDATE vw_paciente SET em_treino = 'n' WHERE id_paciente = '" + pacienteID + "';";
            Statement statement = connection.createStatement();
            statement.execute(sql);
            return true;
        }catch (Exception e)
        {
            return false;
        }
    }

    /**
     * Método que devolve todos os alertas de um paciente
     *
     * @param idPaciente ID do paciente
     * @return ResultSet com a informação relativa aos alertas de um paciente
     */
    public ResultSet getAlertas(String idPaciente)
    {
        String sql = "SELECT * FROM alertas WHERE paciente = '" + idPaciente + "';";
        return queryDB(sql);
    }

    /**
     * Método que envia os dados da pulseira para a base de dados.
     *
     * @param idPaciente ID do paciente
     * @param pressao_arterial_min Tensão baixa
     * @param pressao_arterial_max Tensão alta
     * @param freq_cardiaca Batimento cardiaco
     * @return True se for bem sucessida a adição ou false se ocorrer um erro.
     */
    public boolean enviarDados(String idPaciente, String pressao_arterial_min, String pressao_arterial_max, String freq_cardiaca)
    {
        ZonedDateTime DateTime = ZonedDateTime.now();
        String temp = DateTime.toString();
        String date = new String();
        String time = new String();

        for(int i = 0; i < 10; i++)
        {
            date = date + temp.charAt(i);
        }

        for(int i = 11; i < 19; i++)
        {
            time = time + temp.charAt(i);
        }

        try
        {
            String sql = "INSERT INTO dados VALUES('" + idPaciente + "','" + date + "','" + time + "','" +
                    pressao_arterial_min + "','" + pressao_arterial_max + "','" + freq_cardiaca + "');";

            Statement statement = connection.createStatement();
            statement.execute(sql);
            Thread.sleep(1000);

            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }

    /**
     * Método que devolve todos os dados registados pela pulseira de um paciente.
     *
     * @param idPaciente ID do paciente
     * @return ResultSet com todos os dados registados relacionados com o paciente.
     */
    public ResultSet getDados(String idPaciente)
    {
        String sql = "SELECT * FROM dados WHERE paciente = '" + idPaciente + "';";
        return queryDB(sql);
    }

    /**
     * Método que define os limites máximos e mínimos de cada paciente. Limites utilizados para a determinação dos alertas.
     *
     * @param idPaciente ID do paciente
     * @param baixaMinima Limite minimo da tensão baixa
     * @param baixaMaxima Limite máximo da tensão baixa
     * @param altaMinima Limite minimo da tensão alta
     * @param altaMaxima Limite minimo da tensão alta
     * @param freqMinima Batimento minimo
     * @param freqMaxima Batimento máximo
     * @return True se for bem sucessida a alteração de valores ou false se ocorrer um erro.
     */
    public boolean setLimites(String idPaciente, String baixaMinima, String baixaMaxima, String altaMinima,
                              String altaMaxima, String freqMinima, String freqMaxima)
    {
        String sql = "UPDATE paciente_alertas SET min_pressao_arterial_min = '" + baixaMinima + "', min_pressao_arterial_max = '" +
                baixaMaxima + "', max_pressao_arterial_min = '" + altaMinima + "', max_pressao_arterial_max = '" + altaMaxima +
                "', min_freq_cardiaca = '" + freqMinima + "', max_freq_cardiaca = '" + freqMaxima + "' WHERE id_paciente = '"
                + idPaciente + "';";

        try
        {
            Statement statement = connection.createStatement();
            statement.execute(sql);

            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    /**
     * Método que devolve os limites de cada paciente.
     *
     * @param idPaciente ID de paciente
     * @return ResultSet com os limites de cada paciente
     */
    public ResultSet getLimites(String idPaciente)
    {
        String sql = "SELECT * FROM paciente_alertas WHERE id_paciente = '" + idPaciente + "';";
        return queryDB(sql);
    }

    public ResultSet getLogsPaciente()
    {
        String sql = "SELECT * FROM logs_paciente;";
        return queryDB(sql);
    }

    public ResultSet getLogsFuncionario()
    {
        String sql = "SELECT * FROM logs_funcionario;";
        return queryDB(sql);
    }

    public ResultSet getLogsPacienteAlertas()
    {
        String sql = "SELECT * FROM logs_paciente_alertas;";
        return queryDB(sql);
    }

    public ResultSet getLogsTreino()
    {
        String sql = "SELECT * FROM logs_treino;";
        return queryDB(sql);
    }

    public ResultSet getLogsTratamento()
    {
        String sql = "SELECT * FROM logs_tratamento;";
        return queryDB(sql);
    }

    public ResultSet getLogsUtilizador()
    {
        String sql = "SELECT * FROM logs_utilizador;";
        return queryDB(sql);
    }

    public boolean resetPassword(String nif, String newPass)
    {
        try
        {
            String sql = "ALTER ROLE \"" + nif + "\" WITH PASSWORD '" + newPass + "';" +
                    "UPDATE utilizador SET password = '" + newPass + "' WHERE nif = '" + nif + "';";
            Statement statement = connection.createStatement();
            statement.execute(sql);

            return true;
        }catch (Exception e)
        {
            return false;
        }
    }
}

package pt.pinho;

import java.lang.Thread;
import java.time.ZonedDateTime;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Driver
{
    private Connection connection;

    public Driver(String username, String pwd)
    {
        connection = login(username, pwd);
    }

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

    public boolean isConnected()
    {
        return connection != null;
    }

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

    public boolean editPaciente(String pacienteID, String pwd, String nome, String rua, String codpos, String localidade, String nacionalidade,
                                String sexo, String dataNascimento, String tlf, String email)
    {
        String sql = "UPDATE vw_paciente SET password = '" + pwd + "', nome = '" + nome + "', morada = '" + rua +
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

    public boolean editFunc(String funcID, String pwd, String nome, String rua, String codpos, String localidade, String nacionalidade,
                            String sexo, String dataNascimento, String tlf, String email)
    {
        String sql = "UPDATE vw_func SET password = '" + pwd + "', nome = '" + nome + "', morada = '" + rua +
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

    public ResultSet getAllPacientes()
    {
        String sql = "SELECT * FROM vw_paciente;";
        return queryDB(sql);
    }

    public ResultSet getAllFuncionarios()
    {
        String sql = "SELECT * FROM vw_func;";
        return queryDB(sql);
    }

    public String getTratamentoEstado(String idPac)
    {
        String sql = "SELECT em_tratamento FROM paciente WHERE id_paciente = '" + idPac +"';";

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

    public ResultSet pacientesComTratamento()
    {
        String sql = "SELECT * FROM vw_paciente WHERE em_tratamento = 's';";
        return queryDB(sql);
    }

    public ResultSet pacientesSemTratamento()
    {
        String sql = "SELECT * FROM vw_paciente WHERE em_tratamento = 'n';";
        return queryDB(sql);
    }

    public ResultSet dadosPaciente(String nifPaciente)
    {
        String idPaciente = getPacienteID(nifPaciente);

        String sql = "SELECT * FROM vw_paciente WHERE id_paciente = '" + idPaciente + "';";
        return queryDB(sql);
    }

    public ResultSet dadosFuncionario(String nifFuncionario)
    {
        String idFunc = getFuncID(nifFuncionario);

        String sql = "SELECT * FROM vw_func WHERE id_funcionario = '" + idFunc + "';";
        return queryDB(sql);
    }

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


    public ResultSet getTratamentos(String nifDoPaciente)
    {
        String pacienteID = getPacienteID(nifDoPaciente);

        String sql = "SELECT * FROM treino WHERE tratamento = '" + pacienteID + "';";
        return queryDB(sql);
    }

    public ResultSet getDadosTratamento(String idTrat)
    {
        String sql = "SELECT * FROM tratamento WHERE id_tratamento = '" + idTrat + "';";
        return queryDB(sql);
    }

    public ResultSet getTreinos(String idTratamento)
    {
        String sql = "SELECT * FROM treino WHERE tratamento = '" + idTratamento + "';";
        return queryDB(sql);
    }

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

    public ResultSet getAlertas(String idPaciente)
    {
        String sql = "SELECT * FROM alertas WHERE paciente = '" + idPaciente + "';";
        return queryDB(sql);
    }

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
            Thread.sleep(1000);

            Statement statement = connection.createStatement();
            statement.execute(sql);
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }

    public ResultSet getDados(String idPaciente)
    {
        String sql = "SELECT * FROM dados WHERE paciente = '" + idPaciente + "';";
        return queryDB(sql);
    }

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

    public ResultSet getLimites(String idPaciente)
    {
        String sql = "SELECT * FROM paciente_alertas WHERE id_paciente = '" + idPaciente + "';";
        return queryDB(sql);
    }
}
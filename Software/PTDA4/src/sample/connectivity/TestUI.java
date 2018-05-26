package sample.connectivity;

import java.util.Scanner;
import java.sql.ResultSet;

public class TestUI
{
    public static void main(String args[])
    {
        Scanner sc = new Scanner(System.in);
        String username, password;
        Driver d;

        do
        {
            //Teste de login continuo
            System.out.print("User: ");
            username = sc.nextLine();
            System.out.print("Password: ");
            password = sc.nextLine();

            d = new Driver(username, password);

            if(!d.isConnected())
            {
                System.out.println("Dados de login errados. Tente novamente.\n");
            }
        }while(!d.isConnected());

        System.out.println(d.resetPassword("868701391", "lol"));

        /*
        //Teste de adição de funcionarios
        if(d.addFunc("2017-06-15","s","pass", "Rui", "Rua do cemiterio 2ª campa à direita", "3830", "lmao",
                "tuga", "16", "15", "m", "2017-05-06" ,"123456789", "ayy@lmao.org", "fisioterapeuta"))
        {
            System.out.println("Added!");
        }
        else
        {
            System.out.println("Fail add!");
        }

        //Teste de adição de pacientes
        if(d.addPac("pass","Pedro", "Rua do cemiterio 2ª campa à direita", "3830", "lmao",
                "tuga", "4", "4" ,"m","2017-11-11" ,"123456789", "ayy@lmao.org"))
        {
            System.out.println("Added!");
        }
        else
        {
            System.out.println("Fail add!");
        }

        /*
        System.out.println("Funcao editPaciente");
        System.out.println(d.editPaciente("1","lmao","abc","street","9876","gafanha",
                "tugas","f", "2004-05-05", "158", "1574"));

        System.out.println("Funcao editfunc");
        System.out.println(d.editFunc("1","lmao","abc","street","9876","gafanha",
                "tugas","f", "2004-05-05", "158", "1574"));


        System.out.println("Funcao editTratamento");
        System.out.println(d.editTratamento("1", "a", "a", "2017-01-01", "2017-01-02"));
        */

        /*
        System.out.println("Funcao editTreino");
        System.out.println(d.editTreino("2", "a", "a", "2017-01-01", "2017-01-02"));


        System.out.println("Funcao getTratamentoEstado");
        System.out.println(d.getTratamentoEstado("1"));
        */

        /*
        //Adição de Tratamentos e Treinos
        System.out.println("Funcao addTratamento");
        System.out.println(d.addTratamento("ola", "ayy", "2017-05-01", "2017-11-01",
                "1", "1"));
        */


        /*
        //Exemplo de como imprimir o resultado de uma query (porque passar para array é complicado/estranho/má prática)
        ResultSet rs = d.pacientesSemTratamento();

        try
        {
            while(rs.next())
            {
                System.out.println(rs.getString("id_paciente"));
                System.out.println(rs.getString("nome"));
                System.out.println(rs.getString("nif"));
                System.out.println(rs.getString("cc"));
                //Como fiz SELECT * na query da funcao pacientesSemTratamento() podes escolher qq campo do paciente para imprimir
            }
        }catch(Exception e)
        {
            System.out.println("Erro!");
        }
        //Fim do exemplo
        */

        /*
        //Testes de getters
        System.out.println(d.userType("123456789"));
        System.out.println(d.getUserID("123456789"));
        System.out.println(d.getFuncID("123456788"));
        System.out.println(d.getPacienteID("123456789"));
        */

        //System.out.println(d.finalizarTreino("123456788"));
        //System.out.println(d.finalizarTratamento("123456788"));
        //System.out.println(d.updateEstadoFuncionario("123456789", "n"));

        //System.out.println(d.addTreino("descrição", "notas", "2017-05-05", "2019-09-09", "1", "3"));

        //Teste de ligação
        //System.out.println("Connection = " + d.isConnected());
        /*
        System.out.println(d.getTreinoEstado("1"));
        System.out.println(d.getTreinoEstado("2"));

        //Teste de adição de dados
        System.out.println(d.enviarDados("1", "1", "1", "1"));
        System.out.println(d.enviarDados("1", "2", "1", "1"));
        System.out.println(d.enviarDados("1", "3", "1", "1"));
        System.out.println(d.enviarDados("1", "4", "1", "1"));
        System.out.println(d.enviarDados("1", "5", "1", "1"));
        System.out.println(d.enviarDados("1", "1", "1", "1"));
        System.out.println(d.enviarDados("1", "2", "1", "1"));
        System.out.println(d.enviarDados("1", "3", "1", "1"));
        System.out.println(d.enviarDados("1", "4", "1", "1"));
        System.out.println(d.enviarDados("1", "5", "1", "1"));
        System.out.println(d.enviarDados("1", "1", "1", "1"));
        System.out.println(d.enviarDados("1", "2", "1", "1"));
        System.out.println(d.enviarDados("1", "3", "1", "1"));
        System.out.println(d.enviarDados("1", "4", "1", "1"));
        System.out.println(d.enviarDados("1", "5", "1", "1"));

        //Teste de alteração de limites
        //System.out.println(d.setLimites("1", "1", "1", "1", "1", "1", "1"));
*/
        //Teste de logout
        if(d.logout())
        {
            System.out.println("Logout feito.");
        }
        else
        {
            System.out.println("Erro logout!");
        }

        //Teste de ligação
        System.out.println("Connection = " + d.isConnected());
    }
}
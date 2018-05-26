package pt.pinho;

import java.util.*;

public class Main {

    private static Driver driver;
    static String[] gender = {"m", "f"};
    static String[] names;
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        names = new String[]{"Flowers",
                "Floyd",
                "Flynn",
                "Foley",
                "Forbes",
                "Ford",
                "Foreman",
                "Foster",
                "Fowler",
                "Fox",
                "Francis",
                "Franco",
                "Frank",
                "Franklin",
                "Franks",
                "Frazier",
                "Frederick",
                "Freeman",
                "French",
                "Frost",
                "Fry",
                "Frye",
                "Fuentes",
                "Fuller",
                "Fulton",
                "Gaines",
                "Gallagher",
                "Gallegos",
                "Galloway",
                "Gamble",
                "Garcia",
                "Gardner",
                "Garner",
                "Garrett",
                "Garrison",
                "Garza",
                "Gates",
                "Gay",
                "Gentry",
                "George",
                "Gibbs",
                "Gibson",
                "Gilbert",
                "Giles",
                "Gill",
                "Gillespie",
                "Gilliam",
                "Gilmore",
                "Glass",
                "Glenn",
                "Glover",
                "Goff",
                "Golden",
                "Gomez",
                "Gonzales",
                "Gonzalez",
                "Good",
                "Goodman",
                "Goodwin",
                "Gordon",
                "Gould",
                "Graham",
                "Grant",
                "Graves",
                "Gray",
                "Green",
                "Greene",
                "Greer",
                "Gregory",
                "Griffin",
                "Griffith",
                "Grimes",
                "Gross",
                "Guerra",
                "Guerrero",
                "Guthrie",
                "Gutierrez",
                "Guy",
                "Guzman",
                "Hahn",
                "Hale",
                "Haley",
                "Hall",
                "Hamilton",
                "Hammond",
                "Hampton",
                "Hancock",
                "Haney",
                "Hansen",
                "Hanson",
                "Hardin",
                "Harding",
                "Hardy",
                "Harmon",
                "Harper",
                "Harrell",
                "Harrington",
                "Harris",
                "Harrison",
                "Hart",
                "Hartman",
                "Harvey",
                "Hatfield",
                "Hawkins",
                "Hayden",
                "Hayes",
                "Haynes",
                "Hays",
                "Head",
                "Heath",
                "Hebert",
                "Henderson",
                "Hendricks",
                "Hendrix",
                "Henry",
                "Hensley",
                "Henson",
                "Herman",
                "Hernandez",
                "Herrera",
                "Herring",
                "Hess",
                "Hester",
                "Hewitt",
                "Hickman",
                "Hicks",
                "Higgins",
                "Hill",
                "Hines",
                "Hinton",
                "Hobbs",
                "Hodge",
                "Hodges",
                "Hoffman",
                "Hogan",
                "Holcomb",
                "Holden",
                "Holder",
                "Holland",
                "Holloway",
                "Holman",
                "Holmes",
                "Holt",
                "Hood",
                "Hooper",
                "Hoover",
                "Hopkins",
                "Hopper",
                "Horn",
                "Horne",
                "Horton",
                "House",
                "Houston",
                "Howard",
                "Howe",
                "Howell",
                "Hubbard",
                "Huber",
                "Hudson",
                "Huff",
                "Huffman",
                "Hughes",
                "Hull",
                "Humphrey",
                "Hunt",
                "Hunter",
                "Hurley",
                "Hurst",
                "Hutchinson",
                "Hyde",
                "Ingram",
                "Irwin",
                "Jackson",
                "Jacobs",
                "Jacobson",
                "James",
                "Jarvis",
                "Jefferson",
                "Jenkins",
                "Jennings",
                "Jensen",
                "Jimenez",
                "Johns",
                "Johnson",
                "Johnston",
                "Jones",
                "Jordan",
                "Joseph",
                "Joyce",
                "Joyner",
                "Juarez",
                "Justice",
                "Kane",
                "Kaufman",
                "Keith",
                "Keller",
                "Kelley",
                "Kelly",
                "Kemp",
                "Kennedy",
                "Navarro",
                "Neal",
                "Nelson",
                "Newman",
                "Newton",
                "Nguyen",
                "Nichols",
                "Nicholson",
                "Nielsen",
                "Nieves",
                "Nixon",
                "Noble",
                "Noel",
                "Nolan",
                "Norman",
                "Norris",
                "Norton",
                "Nunez",
                "Obrien",
                "Ochoa",
                "Oconnor",
                "Odom",
                "Odonnell",
                "Oliver",
                "Olsen",
                "Olson",
                "Oneal",
                "Oneil",
                "Oneill",
                "Orr",
                "Ortega",
                "Ortiz",
                "Osborn",
                "Osborne",
                "Owen",
                "Owens",
                "Pace",
                "Pacheco",
                "Padilla",
                "Page",
                "Palmer",
                "Park",
                "Parker",
                "Parks",
                "Parrish",
                "Parsons",
                "Pate",
                "Patel",
                "Patrick",
                "Patterson",
                "Patton",
                "Paul",
                "Payne",
                "Pearson",
                "Peck",
                "Pena",
                "Pennington",
                "Perez",
                "Perkins",
                "Perry",
                "Peters",
                "Petersen",
                "Peterson",
                "Petty",
                "Phelps",
                "Phillips",
                "Pickett",
                "Pierce",
                "Pittman",
                "Pitts",
                "Pollard",
                "Poole",
                "Pope",
                "Porter",
                "Potter",
                "Potts",
                "Powell",
                "Powers",
                "Pratt",
                "Preston",
                "Price",
                "Prince",
                "Pruitt",
                "Puckett",
                "Pugh",
                "Quinn",
                "Ramirez",
                "Ramos",
                "Ramsey",
                "Randall",
                "Randolph",
                "Rasmussen",
                "Ratliff",
                "Ray",
                "Raymond",
                "Reed",
                "Reese",
                "Reeves",
                "Reid",
                "Reilly",
                "Reyes",
                "Reynolds",
                "Rhodes",
                "Rice",
                "Rich",
                "Richard",
                "Richards",
                "Richardson",
                "Richmond",
                "Riddle",
                "Riggs",};


        System.out.println("\nThis script is for devs only, it does not contain any type of safeguards or checks\n\nInsert user and password for login on Postgres (localhost:5432)");
        System.out.print("User: ");
        String user = scanner.next();
        System.out.print("Pass: ");
        String pass = scanner.next();

        driver = new Driver(user, pass);

        if(!driver.isConnected()) {
            System.out.println("Connection failed!");
            return;
        }

        boolean tmp = true;

        System.out.println("\n\nOptions:\n1 - Insert 10 random pacients\n2 - Insert 1 medic, 1 fisio and 1 admin\n" +
                "3 - Insert 7 random dados on pacient 1\n4 - Do all of the above in that order\n5 - Exit\n\n");

        while(tmp) {
            System.out.print("Which option do you desire? (please choose just one valid option, either 1, 2, 3, 4 or 5): ");
            Integer option = scanner.nextInt();


            switch (option) {
                case 1:
                    for (int i = 0; i < 10; i++)
                        System.out.println(createPaciente());
                    break;

                case 2:
                    addFuncs();
                    break;

                case 3:
                    for (int i = 0; i < 7; i++)
                        System.out.println(addDadosPaciente());
                    break;

                case 4:
                    for (int i = 0; i < 10; i++) {
                        System.out.println(createPaciente());
                    }

                    addFuncs();

                    for (int i = 0; i < 7; i++)
                        System.out.println(addDadosPaciente());
                    break;
                case 5:
                    System.out.println("\nThanks! See you on the other side!");
                    tmp = false;
                    break;

                default:
                    System.out.println("\n       I said ONE VALID option... -_-\n");
            }
        }

        //Faz o logout após ter feito todas as operações
        if(driver.isConnected())
            driver.logout();
    }

    private static boolean createPaciente(){
        Random rnd = new Random();
        int nif, cc, n1, n2, gendernum;

        nif = 100000000 + rnd.nextInt(900000000);

        cc = rnd.nextInt(90000000);

        n1 = rnd.nextInt(names.length);
        n2 = rnd.nextInt(names.length);

        gendernum = rnd.nextInt(1);

        return driver.addPac("pass", names[n1] + " " + names[n2], "Rua do camelo", "3810-100", "Aveiro",
                "Português", Integer.toString(nif), Integer.toString(cc), gender[gendernum], "2015-04-23", "961234567",
                "paciente@gmail.com");
    }

    private static void addFuncs(){
        System.out.println(driver.addFunc("2015-01-22", "s", "1", "Peter Smith The Medic", "Rua do camelo", "3810-100", "Aveiro",
                "Português", "111111111", "123456789123", "m", "2015-05-22", "961234567", "medico@gmail.com", "medico"));


        System.out.println(driver.addFunc("2015-04-11", "s", "1", "Peter Smith The Admin God", "Rua do camelo", "3810-100", "Aveiro",
                "Português", "222222222", "123456789124", "m", "2015-05-22", "961234567", "admin@gmail.com", "administrador"));

        System.out.println(driver.addFunc("2015-04-11", "s", "1", "Jonh the Fisio", "Rua do camelo", "3810-100", "Aveiro",
                "Português", "333333333", "123456789125", "f", "2015-05-22", "961234567", "fisio@gmail.com", "fisioterapeuta"));
        System.out.println("nif -> pass\n\nMedic - 111111111 -> 1\nFisio - 333333333 -> 1\nAdmin - 222222222 -> 1");

    }

    private static boolean addDadosPaciente(){

        Random rnd = new Random();
        int pressMin = rnd.nextInt(50) + 35;
        int pressMax = rnd.nextInt(60) + 85;
        int freq = rnd.nextInt(50) + 55;

        return driver.enviarDados("1", Integer.toString(pressMin), Integer.toString(pressMax),
                Integer.toString(freq));
    }
}

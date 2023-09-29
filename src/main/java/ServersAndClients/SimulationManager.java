package ServersAndClients;
import Interface.SimulationFrame;
import StrategyUsed.SelectionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.io.FileWriter;
import java.io.IOException;

//Clasa in care organizam multe lucruri:
public class SimulationManager implements Runnable
{
    //Datele de intrare, le extragem din Interfata;
    //Sunt 0 la inceput deoarece daca nu primim date corecte de intrare, sa afisam 0;
    public int timeLimit = 0;
    public int maxProcessingTime = 0;
    public int minProcessingTime = 0;
    public int maxArrivalTime = 0;
    public int minArrivalTime = 0;
    public int numberOfClients = 0;
    public int numberOfServers = 0;;

    //Pentru interfata:
    public SimulationFrame SimulareFrame;

    //Pentru calculare average service time:
    public int nrClienti = 0;
    public int serviceTime = 0;
    public float averageServiceTime = 0;

    //Pentru calculare average waiting time:
    public int waitingTime = 0;
    public float averageWaitingTime = 0;

    //Pentru calculare peakHour: (calculez cati clienti au fost atunci, nu la ce moment, desi puteam ambele)
    public int peakHour = 0;
    //Pastrez lista de threaduri a serverelor pentru folosire ulterioara
    public Thread[] Threads;

    //Am selectat shortest time, se putea ca strategie si shortest queue;
    public SelectionPolicy selectionPolicy = SelectionPolicy.SHORTEST_TIME;
    //public SelectionPolicy selectionPolicy = SelectionPolicy.SHORTEST_QUEUE;

    //Avem un scheduler, si o lista de taskuri generate random: (de o metoda din aceasta clasa)
    private Scheduler scheduler;
    private List<Task> generatedTasks;

    //Pentru scrierea in fiser:
    private static FileWriter F;

    //Constructorul:
    public SimulationManager() {

        //Instantiem simularea, aici declaram view, facem vizibil, etc...;
        SimulareFrame = new SimulationFrame();
        SimulareFrame.DateIntrare();
        SimulareFrame.Controler();
        SimulareFrame.setVisible(true);

        //Nu continui cu threadurile pana nu am luat date de la gui;
        //Daca iau date, adica daca nu sunt goale text fieldurile, atunci verific daca pot converti in int
        //datele de intrare. Daca pot, continui executia, daca nu pot scriu pe GUI, in exceptie;
        while(true)
        {
            if(SimulareFrame.getclientNumber() != null && SimulareFrame.getclientNumber().equals("") == false
            && SimulareFrame.getserverNumber() != null && SimulareFrame.getserverNumber().equals("") == false
            && SimulareFrame.getmaxProc() != null && SimulareFrame.getmaxProc().equals("") == false
            && SimulareFrame.getminProc() != null && SimulareFrame.getminProc().equals("") == false
            && SimulareFrame.getmaxArr() != null && SimulareFrame.getmaxArr().equals("") == false
            && SimulareFrame.getminArr() != null && SimulareFrame.getminArr().equals("") == false
            && SimulareFrame.getmaxTime() != null && SimulareFrame.getmaxTime().equals("") == false
            && SimulareFrame.getTestButon() == 1){
            //if(SimulareFrame.getTestButon() == 1){
                try
                {
                    numberOfClients = Integer.parseInt(SimulareFrame.getclientNumber());
                    numberOfServers = Integer.parseInt(SimulareFrame.getserverNumber());
                    maxProcessingTime = Integer.parseInt(SimulareFrame.getmaxProc());
                    minProcessingTime = Integer.parseInt(SimulareFrame.getminProc());
                    maxArrivalTime = Integer.parseInt(SimulareFrame.getmaxArr());
                    minArrivalTime = Integer.parseInt(SimulareFrame.getminArr());
                    timeLimit = Integer.parseInt(SimulareFrame.getmaxTime());
                }
                catch(Exception e)
                {
                    SimulareFrame.setGenerareServereClienti("Date gresite de intrare, reintroduce-ti datele. \n");
                }
                break;
            }
        }

        //Dupa instantiez ce imi mai trebuie in constructor:
        scheduler = new Scheduler(numberOfServers, numberOfClients);
        Threads = new Thread[numberOfServers];

        //Adaug la lista de servere din scheduler, si incep aici threadurile:
        for (int i = 0; i < numberOfServers; i++) {
            Server s = new Server();

            scheduler.getServers().add(s);
            Thread t = new Thread(s);
            t.start();

            //Asa le salvez:
            Threads[i] = t;
            //Aici aleg si strategia:
            scheduler.changeStrategy(selectionPolicy);
        }

        //Initializez si lista de taskuri generate:
        generatedTasks = new ArrayList<Task>(0);
        generateNRandomTasks();
    }

    //Functie pentru calculare average waiting time: (Defapt urmatoarele 2 metode)
    public int currentWaiting(Task newt)
    {
        for (int i = 0; i < numberOfServers; i++)
        {
            for(Task t: scheduler.getServers().get(i).getTasks())
            {
                if(newt.equals(t) == true)
                {
                    //Daca am gasit un task dintr-un server cautat, ii returnem waiting period la acel server;
                    return scheduler.getServers().get(i).getWaitingPeriod().get();
                }
            }
        }
        return 0;
    }

    public float averageWaitingTime()
    {
        for(int i=0; i<numberOfServers; i++)
        {
            int ok = 0;
            int suma = 0;
            for(Task t: scheduler.getServers().get(i).getTasks())
            {
                //Daca este primul din coada, atunci el nu asteapta pe nimeni, dar adaug la suma
                //pentru cel care asteapta dupa el;
                if(ok == 0)
                {
                    suma = suma + t.getProcessingTime() + 1;
                    ok = 1;
                    continue;
                }

                //In cazul in care dupa terminarea executiei au mai ramas clienti neprocesati, scad ce a ramas:
                //si asa calculez media, fara ce a ramas:
                waitingTime = waitingTime - suma;
                suma = suma + t.getProcessingTime();
            }
        }

        //Calculez si returnez rezultatul:
        System.out.println("Waiting: " + waitingTime + " ,Clienti: " + nrClienti);
        if(nrClienti == 0)
        {
            return 0;
        }
        else
        {
            averageWaitingTime = (float) waitingTime / nrClienti;
        }
        return averageWaitingTime;
    }

    //Pentru calculare average service timeul:
    public float averageServiceeeTime()
    {
        for(int i=0; i<numberOfServers; i++)
        {
            //Daca am taskuri in servere, deci dupa terminare thread principal, atunci scad ce a ramas
            //ca service time;
            if(scheduler.getServers().get(i).getTasks().size() != 0)
            {
                serviceTime = serviceTime - scheduler.getServers().get(i).getWaitingPeriod().get() - 1;
            }
            else
            {
                serviceTime = serviceTime - scheduler.getServers().get(i).getWaitingPeriod().get();
            }
        }

        int Clienti = generatedTasks.size();

        //Dupa fac la fel pentru clienti, si scad si processing timeul la fiecare task;
        for(int i=1; i<Clienti; i++)
        {
            nrClienti = nrClienti - 1;
            serviceTime = serviceTime - generatedTasks.get(i).getProcessingTime();
        }

        //Aici afisez rezultatul, convertit in float, fractie:
        System.out.println("Service: " + serviceTime + " ,Clienti: " + nrClienti);
        if(nrClienti != 0)
        {
            averageServiceTime = (float) serviceTime / nrClienti;
        }
        else
        {
            averageServiceTime = (float) serviceTime;
        }

        return averageServiceTime;
    }

    //Pentru calculare peak hour, verific dupa fiecare iteratie de 1 secunda, cate taskuri sunt in total in toate serverele,
    //si schimb valoarea la peak hour (aici puteam sa gasesc si cand se intampla daca doream)
    public void calculatePeakHour()
    {
        int sum = 0;
        for(int i=0; i<numberOfServers; i++)
        {
            sum = sum + scheduler.getServers().get(i).getTasks().size();
        }

        if(sum > peakHour)
        {
            peakHour = sum;
        }
    }

    //Aici generez N taskuri random:
    private void generateNRandomTasks()
    {
        //Am o valoare de inceput nefolosita: (deci tot timpul va ramane un element in lista mea, pe care il ignor tot timpul)
        Task Tampon = new Task(100000,100000, -5);
        generatedTasks.add(Tampon);

        //Generez pt numarul de clienti pe care il am:
        for (int i = 0; i < numberOfClients; i++) {
            Random rand = new Random();

            //Asa calculez un numar random cuprins intre min si max pentru arrival si processing:
            //Tratez si cazurile exceptie, cand min > max spre ex;
            int randarrival = rand.nextInt(maxArrivalTime+1);
            int randprocessing = rand.nextInt(maxProcessingTime+1);

            if(maxArrivalTime == minArrivalTime)
            {
                randarrival = minArrivalTime;
            }
            else if(maxArrivalTime < minArrivalTime)
            {
                System.out.println("Date gresite de intrare.");
                return;
            }
            else
            {
                while (randarrival < minArrivalTime) {
                    randarrival = rand.nextInt(maxArrivalTime+1);
                }
            }

            if(maxProcessingTime == minProcessingTime)
            {
                randprocessing = minProcessingTime;
            }
            else if(maxProcessingTime < minProcessingTime)
            {
                System.out.println("Date gresite de intrare.");
                return;
            }
            else
                {
                while (randprocessing < minProcessingTime) {
                    randprocessing = rand.nextInt(maxProcessingTime+1);
                }
            }

            //Dupa adaug taskul la lista de taskuri a serverului anume;
            Task aux = new Task(randarrival, randprocessing, i + 1);
            generatedTasks.add(aux);

            //Si adaug si nr de clienti existenti;
            serviceTime = serviceTime + randprocessing;
            nrClienti = nrClienti + 1;
            //Aici sortez in functie de arrival time: (Comparable)
            Collections.sort(generatedTasks);
        }
    }

    //Threadul principal care le sincronizeaza pe toate celelalte, adica pe toate serverele:
    @Override
    public void run()
    {
        int currentTime = 0;

        //Conditie de terminare, daca s-a terminat timpul de procesare:
        while (currentTime < timeLimit)
        {
            //Verific daca mai sunt taskuri sau nu in servere, daca nu mai sunt termin threadul
            //Adica nu mai astept daca exista acest caz special, de mai jos;
            int verificare = 0;
            for(int i=0; i<numberOfServers; i++)
            {
                int for2 = 0;
                for(Task t: scheduler.getServers().get(i).getTasks())
                {
                    verificare = 1;
                    for2 = 1;
                    break;
                }
                if(for2 == 1)
                {
                    break;
                }
            }

            //Daca nu avem nimic pe niciun server si nici nu mai avem clienti ce asteapta, atunci scoatem din thread;
            //Si calculam toate cele ramase in continuare;
            if(generatedTasks.size() == 1 && verificare == 0)
            {
                break;
            }

            //Iau ultimul element din lista de taskuri generate random:
            Task aux = new Task(0, 0, 0);
            if(generatedTasks.size() - 1 >= 0) {
                aux = generatedTasks.get(generatedTasks.size() - 1);
            }

            //Daca are arrival time egal cu timpul curent, il adaugam pe servere;
            while(aux.getArrivalTime() == currentTime && generatedTasks.size() != 1)
            {
                //Asa il adaug la un server: (folosind scheduler)
                scheduler.dispatchTask(aux);

                //Calculez aici waiting time, il afisez doar in consola, nicaieri altundeva:
                System.out.println("(Pentru " + aux.getId() + " ,waiting timeul este: " + (currentWaiting(aux) -
                        aux.getProcessingTime()) + ")");
                waitingTime = waitingTime + currentWaiting(aux) - aux.getProcessingTime();

                //Scoatem ultimul task, pentru ca l-am introdus deja in servere, deci nu mai asteapta;
                if(generatedTasks.size() - 1 >= 0) {
                    int ultimulTermen = generatedTasks.size() - 1;
                    generatedTasks.remove(ultimulTermen);
                }

                //Alegem urmatorul de luat, in bucla, pentru urmatoarea iteratie;
                if(generatedTasks.size() - 1 >= 0) {
                    aux = generatedTasks.get(generatedTasks.size() - 1);
                }
            }
            //Crestem timpul de procesare;
            currentTime++;

            //Afisez datele curente, inainte de sleep: (pe consola, fisier, gui)
            try
            {
                System.out.println("Current time " + (currentTime-1) + " :");
                try {
                    F.write("Current time " + (currentTime-1) + " :\n\n\n");
                } catch (IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }

                SimulareFrame.setGenerareServereClienti("Current time " + (currentTime-1) + " :\n");

                try {
                    //Afisez lista de servere actuala;
                    testareListaServere();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //Afisez lista de clienti
                testareListaClienti();

                System.out.printf("\n\n\n");

                //Calculez peak hour dupa fiecare iteratie;
                calculatePeakHour();

                //Dau sleep la threadul principal 1 secunda, dupa care reintra in functiune (pt sincronizare cu serverele)
                Thread.sleep(1000);
                //Se reseteaza gui dupa fiecare iteratie, pentru a afisa urmatoarele servere cu clienti:
                SimulareFrame.resetGenerareServereClienti();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //Dupa terminare thread principal, OPRIM restul threadurilor:
        for(int i=0; i< numberOfServers; i++)
        {
            Threads[i].stop();
        }

        //De asemenea, calculam average service time, average waiting time si peak hour, si le scriu in text file
        //si pe consola;
        float rezultat1 = averageServiceeeTime();
        System.out.println("Average timpul de service: " + rezultat1 + " ;");
        int rezultat2 = peakHour;
        System.out.println("Peak hour : " + rezultat2 + " ;");
        float rezultat3 = averageWaitingTime();
        System.out.println("Average timpul de waiting: " + rezultat3 + " ;");

        try {
            F.write("Average timpul de service: " + rezultat1 + " ;\n" +
                    "Peak hour : " + rezultat2 + " ;\n" +
                    "Average timpul de waiting: " + rezultat3 + " ;");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            F.write("\n\n\n\nFinalul logului.");
            F.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        SimulareFrame.setGenerareServereClienti("Programul s-a terminat.");
    }

    //Pentru lista de clienti ce nu au intrat inca pe servere:
    public void testareListaClienti()
    {
        System.out.println("\nLista actuala de clienti ce nu au intrat pe servere:");
        try {
            F.write("\nLista actuala de clienti ce nu au intrat pe servere:\n"); //\n\n nu;
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        //Ma uit la lista, si verific daca mai este sau nu cu clienti, si ii iau in ordine:
        //Afisez datele despre ei: (sunt deja sortati in acest moment)
        for(int i=0; i< generatedTasks.size(); i++)
        {
            if(generatedTasks.get(i).getId() != -5)
            {
                System.out.println("Id) : " + (i + 1) + " Arr time: " + generatedTasks.get(i).getArrivalTime() +
                        " ,Processing time: " + generatedTasks.get(i).getProcessingTime() + " ,Id: " +
                        generatedTasks.get(i).getId() + ";");

                try {
                    F.write("Clientul " + generatedTasks.get(i).getId() + " are arrival time " +
                            generatedTasks.get(i).getArrivalTime() + " si processing time " +
                            generatedTasks.get(i).getProcessingTime() + ";\n");
                } catch (IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }
            }
        }

        //Dupa lista de clienti ce nu au intrat in servere:
        System.out.println("Lista clienti terminata.");
        try {
            F.write("Lista terminata.\n\n\n");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    //Pentru lista de servere: (si taskurile pe care le au la acel moment de timp)
    public void testareListaServere() throws InterruptedException
    {
        //Ma uit la toate serverele:
        for(int i=0; i < numberOfServers; i++)
        {
            System.out.println("Testare servere: " + (i + 1) +  " ,Waiting Time: " +
                    scheduler.getServers().get(i).getWaitingPeriod() + " )");

            try {
                F.write("Serverul " + (i + 1) +  ", are Waiting Time: " +
                        scheduler.getServers().get(i).getWaitingPeriod() + ")\n\n");
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }

            SimulareFrame.setGenerareServereClienti("S" + (i+1) + " : ");

            //Ma uit la taskurile unui server, sa extrag toate informatiile despre client, la acel
            //moment de timp:
            for(Task t: scheduler.getServers().get(i).getTasks())
            {
                if(t.getId() != -5)
                {
                    System.out.println("La acest server avem: Clientul " + t.getId()
                            + ", are Arrival Time: " + t.getArrivalTime() +
                            " si Processing Time: " + t.getProcessingTime() + " ;");
                }

                try {
                    F.write("La acest server avem: Clientul " + t.getId()
                            + ", are Arrival Time: " + t.getArrivalTime() +
                            " si Processing Time: " + t.getProcessingTime() + " ;\n");
                } catch (IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }

                SimulareFrame.setGenerareServereClienti("C " + t.getId() +
                        " , Proc " + t.getProcessingTime() + "; ");
            }
            System.out.println("-------------------------------------------------------------------------");
            try {
                F.write("-------------------------------------------------------------------------\n\n");
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }

            SimulareFrame.setGenerareServereClienti("\n---------------------------------------------------------------" +
                    "-----------------------------------------------------------------------------------------------------------" +
                    "-------------------------------------------------------------------------------------------------------------------" +
                    "--\n");
        }
        try {
            F.write("Servere terminate.\n");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        //Pun ----- pentru afisare mai frumoasa, diferentiere intre serverel
    }

    //Functia main:
    public static void main(String[] args) throws IOException {

        //Instantiez SimulationManager, care contine threadurile din servere incepute deja;
        SimulationManager gen = new SimulationManager();

        //Pentru scriere in fisier: (inainte de thread scriu datele de intrare in fisier text)
        F = new FileWriter("Log of events.txt");

        try {
            F.write("Logul are aceste date de intrare: " +
                    "\n\n   Time limit: " + gen.timeLimit + "\n   Timp procesare maxim: " + gen.maxProcessingTime +
                    "\n   Timp procesare minim: " + gen.minProcessingTime + "\n   Timp ajungere maxim: " +
                    gen.maxArrivalTime + "\n   Timp ajungere minim: " + gen.minArrivalTime + "\n   Numarul de " +
                    "clienti: " + gen.numberOfClients + "\n   Numarul de servere: " + gen.numberOfServers + " :\n\n\n");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        //incep threadul principal:
        Thread t = new Thread(gen);
        t.start();
        //Acesta se opreste ATUNCI CAND ori nu mai sunt clienti nici in servere nici in lista de clienti generata random,
        //ori cand timpul de rulare a ajuns la timpul max de rulare. In acel moment se opresc si celelalte threaduri,
        //cele de la servere, si se afiseaza timpii de average calculari, dupa terminarea executarii.
        //Dupa se termina programul;

        System.out.println("Program terminat.");
    }
}






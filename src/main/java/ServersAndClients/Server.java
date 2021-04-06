package ServersAndClients;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable
{
    //Avem o lista de taskuri; am folosit blocking queue pentru a putea sa sincronizez threadurile cum trebuie;
    private BlockingQueue<Task> tasks;
    //Am folosim AtomicInteger pentru sincronizare intre threaduri;
    private AtomicInteger waitingPeriod;

    public Server()
    {
        //Initializez lista de taskuri cu 10000 de posibili clienti, pentru ca ArrayBlockingQueue necesita limita superioara
        tasks = new ArrayBlockingQueue<Task>(10000);
        waitingPeriod = new AtomicInteger(0); //initializat cu 0
    }

    //Adaug taskul la lista de taskuri din serverul acesta
    public void addTask(Task newTask)
    {
        tasks.add(newTask);
        //Adaug timpul de procesare la waiting timeul serverului respectiv;
        waitingPeriod.addAndGet(newTask.getProcessingTime());
    }

    //Functia de run pentru fiecare server in parte:
    public void run()
    {
        //Pentru a lucra cu lista de taskuri:
        Task aux;
        while(true) //Pana cand avem alta conditie de oprire
        {
            //Daca nu este goala lista
            if(tasks.size() != 0)
            {
                //Luam varful cozii: (fara sa il scoatem din coada)
                aux = tasks.peek();
                try
                {
                    int max = aux.getProcessingTime(); //pentru a stii cat timp sa ii dau sleep, sa fie procesat
                    for(int i = 0; i < max; i++)
                    {
                        Thread.sleep(1000); //1 secunda;
                        //Dupa ce dau sleep 1 secunda, scad waiting period la acel server cu 1 secunda; (din waiting total)
                        waitingPeriod.addAndGet(-1);
                        //Setez timpul de procesare cu -1 pentru ca acel task din varf a fost procesat deja 1 secunda
                        int timpActual = aux.getProcessingTime()-1;
                        aux.setProcessingTime(timpActual);
                        if(timpActual == 0)
                        {
                            //Daca s-a terminat, il scot din coada;
                            tasks.take();
                        }
                    }
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //Getteri:
    public BlockingQueue<Task> getTasks()
    {
        return tasks;
    }
    public AtomicInteger getWaitingPeriod()
    {
        return waitingPeriod;
    }
}


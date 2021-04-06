package StrategyUsed;
import ServersAndClients.Server;
import ServersAndClients.Task;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

//Nu sunt sigur daca aceasta clasa functioneaza, m-am axat pe cea de timp,
//si pe aceasta am lasat-o implementata, dar nu am testat-o foarte mult;
public class ConcreteStrategyQueue implements Strategy
{
    //Folosesc auxiliare pentru parcurgere lista servere si taksuri:
    @Override
    public void addTask(List<Server> servers, Task t)
    {
        Server aux = new Server();
        //Am nevoie de o lista de taskuri:
        BlockingQueue<Task> aux1 = new ArrayBlockingQueue<Task>(999);

        for(Server s: servers)
        {
            BlockingQueue<Task> aux2 = s.getTasks();
            if(aux1.size() > aux2.size())
            {
                aux1 = aux2;
                aux = s;
            }
        }

        //In functie de care este lista de taskuri cea mai scurta, deci in fct de care server are cele mai putine taskuri,
        //adaug in acel server taskul nou;
        aux.addTask(t);
    }
}



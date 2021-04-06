package StrategyUsed;
import ServersAndClients.Server;
import ServersAndClients.Task;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcreteStrategyTime implements Strategy
{
    //Dau add la task in serverul ales:
    @Override
    public void addTask(List<Server> servers, Task t)
    {
        //Salvez in auxiliar;
        Server aux = new Server();
        //Ce salvez este waiting periodul serverului;
        AtomicInteger aux1 = new AtomicInteger(999);

        for(Server s: servers)
        {
            AtomicInteger aux2 = s.getWaitingPeriod();
            if(aux1.get() > aux2.get())
            {
                aux1 = aux2;
                aux = s;
            }
        }

        //Dupa ce am ales cel mai mic waiting time, stiu din care server face parte, si adaug la acel server taskul;
        aux.addTask(t);
    }
}


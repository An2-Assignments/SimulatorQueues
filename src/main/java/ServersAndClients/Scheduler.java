package ServersAndClients;
import StrategyUsed.ConcreteStrategyQueue;
import StrategyUsed.ConcreteStrategyTime;
import StrategyUsed.SelectionPolicy;
import StrategyUsed.Strategy;
import java.util.ArrayList;
import java.util.List;

public class Scheduler
{
    //Avem: lista de servere, numar max de servere (dat de la intrare) si nr max de task per server (dar de nr de taskuri)
    private List<Server> servers;
    private int maxNoServers;
    private int maxTasksPerServer;
    //Strategia de implementare:
    private Strategy strategy;

    //Initializez lista de servere, prin constructor;
    public Scheduler(int maxNoServers, int maxTasksPerServer)
    {
        servers = new ArrayList<Server>(0);

        this.maxNoServers = maxNoServers;
        this.maxTasksPerServer = maxTasksPerServer;
    }

    //Pentru selectare a strategiei de intrare in cozi:
    public void changeStrategy(SelectionPolicy policy)
    {
        if(policy == SelectionPolicy.SHORTEST_QUEUE)
        {
            strategy = new ConcreteStrategyQueue();
        }
        if(policy == SelectionPolicy.SHORTEST_TIME)
        {
            strategy = new ConcreteStrategyTime();
        }
    }

    //Pentru selectare in care server introducem acel task;
    public void dispatchTask(Task t)
    {
        strategy.addTask(servers, t);
    }

    //Getter pentru servere;
    public List<Server> getServers()
    {
        return servers;
    }
}






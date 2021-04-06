package StrategyUsed;
import ServersAndClients.Server;
import ServersAndClients.Task;
import java.util.List;

public interface Strategy
{
    //O metoda de a adauga taskul la lista de servere, in functie de o metoda aleasa de noi din SelectionPolicy
    public void addTask(List<Server> servers, Task t);
}

















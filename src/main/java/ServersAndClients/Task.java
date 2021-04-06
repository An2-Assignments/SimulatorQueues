package ServersAndClients;

public class Task implements Comparable<Task>
{
    //Am datele unui client: arrival time, processing time, id-ul sau: (generate in SimulationManager)
    private int arrivalTime;
    private int processingTime;
    private int id;

    //Constructor normal:
    public Task(int arrivalTime, int processingTime, int id)
    {
        this.arrivalTime = arrivalTime;
        this.processingTime = processingTime;
        this.id = id;
    }

    //Setteri si Getteri:
    public void setArrivalTime(int arrivalTime)
    {
        this.arrivalTime = arrivalTime;
    }
    public void setProcessingTime(int processingTime)
    {
        this.processingTime = processingTime;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getArrivalTime()
    {
        return arrivalTime;
    }
    public int getProcessingTime()
    {
        return processingTime;
    }
    public int getId() {
        return id;
    }

    //Cu comparable, aranjez lista de clienti in functie de arrival time (pentru a stii cine ajunge cel mai
    // devreme si cine cel mai tarziu, am nevoie de o ordine);
    public int compareTo(Task m)
    {
        if(this.arrivalTime >  m.arrivalTime) {
            return -1;
        }
        else if(this.getArrivalTime() ==  m.arrivalTime) {
            return 0;
        }
        else {
            return +1;
        }
    }
}






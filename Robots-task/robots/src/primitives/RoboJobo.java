package primitives;


public class RoboJobo extends Thread {
    private IRobot robot;
    private int sleepTime;

    public RoboJobo(IRobot rob, int sleep)
    {
        robot = rob;
        sleepTime = sleep;
    }
    @Override
    public void run ()
    {
        while (true)
        {
            robot.onModelUpdateEvent();
            try
            {
                Thread.sleep(sleepTime);
            }catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}

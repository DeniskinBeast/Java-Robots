package primitives;

public class DefaultRobot implements IRobot{

    private volatile double m_robotPositionX;
    private volatile double m_robotPositionY;
    private volatile double m_robotDirection;

    public DefaultRobot()
    {
        m_robotPositionX = 100;
        m_robotDirection = 100;
        m_robotDirection = 0;
    }


    @Override
    public double getM_robotPositionX() {
        return 0;
    }

    @Override
    public double getM_robotPositionY() {
        return 0;
    }

    @Override
    public double getM_robotDirection() {
        return 0;
    }

//    @Override
//    public double getMaxVelocity() {
//        return 0;
//    }
//
//    @Override
//    public double getMaxAngularVelocity() {
//        return 0;
//    }
//
//    @Override
//    public void moveRobot(double velocity, double angularVelocity, double duration) {
//
//    }

    @Override
    public void onModelUpdateEvent(){
    }
}

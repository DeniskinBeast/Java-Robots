package primitives;

public interface IRobot {
    double getM_robotPositionX();
    double getM_robotPositionY();
    double getM_robotDirection();
//    void moveRobot(double velocity, double angularVelocity, double duration);
    void onModelUpdateEvent();
}

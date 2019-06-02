package primitives;

public interface IRobot {
    double getM_robotPositionX();
    double getM_robotPositionY();
    double getM_robotDirection();
    double getMaxVelocity();
    double getMaxAngularVelocity();
    void moveRobot(double velocity, double angularVelocity, double duration);
    void onModelUpdateEvent();
}

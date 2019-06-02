import primitives.IRobot;
import primitives.Target;

public class TestRobot implements IRobot{
    private volatile double m_robotPositionX;
    private volatile double m_robotPositionY;
    private volatile double m_robotDirection;

    private Target target;

    private static final double maxVelocity = 0.1;
    private static final double maxAngularVelocity = 0.001;

    public TestRobot(double x, double y, double direct, Target targ){
        m_robotPositionX = x;
        m_robotPositionY = y;
        m_robotDirection = direct;
        target = targ;
    }

    public TestRobot(){
        m_robotPositionX = 100;
        m_robotDirection = 100;
        m_robotDirection = 0;
    }

    public double getM_robotPositionX() {
        return m_robotPositionX;
    }

    public double getM_robotPositionY() {
        return m_robotPositionY;
    }

    public double getMaxVelocity() {
        return maxVelocity;
    }

    public double getMaxAngularVelocity() {
        return maxAngularVelocity;
    }

    public double getM_robotDirection() {
        return m_robotDirection;
    }

    public void onModelUpdateEvent()
    {
        double distance = TestRobot.distance(target.getM_targetPositionX(), target.getM_targetPositionY(),
                m_robotPositionX, m_robotPositionY);
        if (distance < 0.5)
        {
            return;
        }
        double velocity;
        double angleToTarget = TestRobot.angleTo(m_robotPositionX, m_robotPositionY,
                target.getM_targetPositionX(), target.getM_targetPositionY());
        double angularVelocity;
        double angle = TestRobot.asNormalizedRadians(angleToTarget - m_robotDirection);

        if (angle > Math.PI) angularVelocity = -maxAngularVelocity;
        else angularVelocity = maxAngularVelocity;

        if (Math.abs(angle) < 0.1) velocity = maxVelocity;
        else velocity = distance * Math.abs(angularVelocity)/2;

        moveRobot(velocity, angularVelocity, 10);
    }

    public void moveRobot(double velocity, double angularVelocity, double duration)
    {
        velocity = applyLimits(velocity, 0, maxVelocity);
        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
        double newX = m_robotPositionX + velocity / angularVelocity *
                (Math.sin(m_robotDirection  + angularVelocity * duration) -
                        Math.sin(m_robotDirection));
        if (!Double.isFinite(newX))
        {
            newX = m_robotPositionX + velocity * duration * Math.cos(m_robotDirection);
        }
        double newY = m_robotPositionY - velocity / angularVelocity *
                (Math.cos(m_robotDirection  + angularVelocity * duration) -
                        Math.cos(m_robotDirection));
        if (!Double.isFinite(newY))
        {
            newY = m_robotPositionY + velocity * duration * Math.sin(m_robotDirection);
        }
        m_robotPositionX = newX;
        m_robotPositionY = newY;
        double newDirection = asNormalizedRadians(m_robotDirection + angularVelocity * duration);
        m_robotDirection = newDirection;
    }

    private static double applyLimits(double value, double min, double max)
    {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }

    public static double asNormalizedRadians(double angle)
    {
        while (angle < 0)
        {
            angle += 2*Math.PI;
        }
        while (angle >= 2*Math.PI)
        {
            angle -= 2*Math.PI;
        }
        return angle;
    }


    public static double angleTo(double fromX, double fromY, double toX, double toY)
    {
        double diffX = toX - fromX;
        double diffY = toY - fromY;

        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }


    public static double distance(double x1, double y1, double x2, double y2)
    {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }
}

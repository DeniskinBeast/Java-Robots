package primitives;

public class Target {
    private volatile int m_targetPositionX;
    private volatile int m_targetPositionY;

    public Target(Integer x, Integer y){
        m_targetPositionX = x;
        m_targetPositionY = y;
    }

    public int getM_targetPositionX() {
        return m_targetPositionX;
    }

    public int getM_targetPositionY() {
        return m_targetPositionY;
    }

    public void setM_targetPositionX(Integer x){
        m_targetPositionX = x;
    }

    public void setM_targetPositionY(Integer y){
        m_targetPositionY = y;
    }
}

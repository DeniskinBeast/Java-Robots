package primitives;

import Annotations.RobotClassAnnotation;
import Annotations.RobotMethodAnnotation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

public class AnnotationsResolver implements IRobot{

    private Target target;

    private Class clazz;
    private Object robot;

    private Method getM_robotPositionX;
    private Method getM_robotPositionY;
    private Method getM_robotDirection;
    private Method onModelUpdateEvent;


    public AnnotationsResolver(Class someClass, Target targ)
    {
        this.target = targ;
        this.clazz = someClass;
    }


    @Override
    public double getM_robotPositionX() {
        try {
            return (double) getM_robotPositionX.invoke(robot, new Object[0]);
        }
        catch (InvocationTargetException | IllegalAccessException e)
        {

        }
        return 0;
    }

    @Override
    public double getM_robotPositionY() {
        try {
            return (double) getM_robotPositionY.invoke(robot, new Object[0]);
        }
        catch (InvocationTargetException | IllegalAccessException e)
        {

        }
        return 0;
    }

    @Override
    public double getM_robotDirection() {
        try {
            return (double) getM_robotDirection.invoke(robot, new Object[0]);
        }
        catch (InvocationTargetException | IllegalAccessException e)
        {

        }
        return 0;
    }


    @Override
    public void onModelUpdateEvent() {
        try {
            if (onModelUpdateEvent == null) System.out.println("NULL");
            onModelUpdateEvent.invoke(robot);
        }
        catch (InvocationTargetException | IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }

    public void resolveClassMethods() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        robot = clazz.getConstructor(double.class, double.class, double.class, Target.class).newInstance(200, 200, 50, target);
        try
        {
            var robotAnnotation = robot.getClass().getAnnotation(RobotClassAnnotation.class);
        }
        catch (Exception e)
        {
            System.out.println("Wrong annotation");
        }
        ArrayList<Method> methodsList = new ArrayList<>();
        var methods = clazz.getDeclaredMethods();
        methodsList.addAll(Arrays.asList(methods));

        for (Method method:methodsList)
        {
            var annotation = method.getDeclaredAnnotation(RobotMethodAnnotation.class);
            if (annotation == null) continue;
            var methodName = annotation.methodName();
            System.out.println(methodName);
            switch (methodName)
            {
                case("getM_robotPositionX"): {
                    getM_robotPositionX = method;
                    break;
                }
                case("getM_robotPositionY"): {
                    getM_robotPositionY = method;
                    break;
                }

                case("getM_robotDirection"): {
                    getM_robotDirection = method;
                    break;
                }

                case("onModelUpdateEvent"): {
                    onModelUpdateEvent = method;
                    break;
                }

            }
        }
    }
}

package MyLoader;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

public class MyLoader extends ClassLoader {

    private File file;

    public MyLoader(File classFile)
    {
        file = classFile;
    }

    @Override
    protected Class<?> findClass(String name)
            throws ClassNotFoundException
    {
        ClassNotFoundException savedEx = null;
        try
        {
            return super.findClass(name);
        }
        catch (ClassNotFoundException ex)
        {
            savedEx = ex;
        }
        if (name.contains("."))
        {
            throw savedEx;
        }

        try
        {
            FileInputStream in = new FileInputStream(file);
            try
            {
                BufferedInputStream bin = new BufferedInputStream(in);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte [] buffer = new byte[8192];
                while (true)
                {
                    int iCount = bin.read(buffer);
                    if (iCount >= 0)
                    {
                        out.write(buffer, 0, iCount);
                    }
                    else
                    {
                        break;
                    }
                }
                byte [] classCode = out.toByteArray();
                Class clazz = super.defineClass(name, classCode, 0, classCode.length);
                return clazz;
            }
            finally
            {
                in.close();
            }
        }
        catch (Exception ex)
        {
            throw savedEx;
        }
    }
}

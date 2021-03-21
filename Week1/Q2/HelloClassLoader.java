import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HelloClassLoader extends ClassLoader {
    public static void main(String[] args) {
        try {
            Object Hello = new HelloClassLoader().findClass("Hello").getDeclaredConstructor().newInstance();
            Method hello = Hello.getClass().getMethods()[0];
            hello.invoke(Hello);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected Class<?> findClass(String name) {
        File classFile = new File(System.getProperty("user.dir") + File.separator + name + ".xlass");
        Path path = Paths.get(classFile.getAbsolutePath());
        byte[] data = null;
        try {
            data = Files.readAllBytes(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) (255 - data[i]);
        }
        return defineClass(name, data, 0, data.length);
    }
}

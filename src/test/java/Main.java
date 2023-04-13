import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {

        Process proc = null;
        String[] arr = new String[]{"C:\\Users\\10023\\py\\python.exe", "D:\\Code\\CT\\src\\main\\resources\\analyse.py", "1"};
        try {
            proc = Runtime.getRuntime().exec(arr);
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            proc.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

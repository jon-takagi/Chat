
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class BufferedReaderExample {

    public static void main(String[] args) {

        ArrayList<String > contents = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("/Users/40095/.ssh/authorized_keys"))) {

            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                contents.add(sCurrentLine);
            }
            if(contents.contains("ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDR4n8TTdv6emJJveaX5HN6ptyFbhj1o4VigM3sy6UDk1hpC42gLWO+UvbYy/wykcTNuwGwdn3/hSo8MswwOAg4pHFITQl7pyVbCFnzybph+tIHFT5kwOACi5JGP/Tuublzmr8Fw4150Vg0lLHqYllv5dwkZ4Ky+Kt8oXpCqmkGJcc2Ny5zYyATJUdmoS+UnyvK2DT7idVntl74boDd2R6VlFtoybPObniKVFj3cGS4JoqF32BYqCI6zqH69KPY8iU4ixI4k6CJGJtDDz3yoIZwsKLJd/h/YXUC3VYyJ/d0pLNNir3UWtIj7UxlM0TfQYj4Oo3eovqJKjxw2Z2xGN+z 40095@2016-40095.jisedu.or.id"))
                System.out.println("its there");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

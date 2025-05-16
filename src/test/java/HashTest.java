import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lllllllhp.data.UserData;
import com.lllllllhp.utils.dataUtils.DataChecker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class HashTest {
    public static void main(String[] args) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        Path path = Path.of("src/main/resources/User/1/userData.json");
        String data;
        try {
            data = Files.readString(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        UserData userData = gson.fromJson(data, UserData.class);

        String newHash = DataChecker.toHash(userData);
        boolean isValid = DataChecker.checkData(userData, path);

        if (isValid) System.out.println("data is valid");
        else System.out.println("data is invalid");
    }
}

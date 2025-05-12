import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lllllllhp.data.UserData;
import com.lllllllhp.utils.dataChecker.DataChecker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CreateHash {
    public static void main(String[] args) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        Path path = Path.of("src/main/resources/User/lllllllhp/userData.json");
        String data;
        try {
            data = Files.readString(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        UserData userData = gson.fromJson(data, UserData.class);
        String hashCode = DataChecker.toHash(userData);
        DataChecker.createHashFile(hashCode, path);
    }
}

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lllllllhp.data.MapPre;
import com.lllllllhp.model.game.MapModel;
import com.lllllllhp.model.game.MovementRecord;
import com.lllllllhp.utils.aiSolver.AISolver;
import com.lllllllhp.utils.aiSolver.BoxData;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Deque;
import java.util.Map;

public class AITest {
    public static void main(String[] args) {
        Path mapFolder = Path.of("src/main/resources/maps");
        mapFolder = mapFolder.resolve("hard_5_5_map2.json");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String mapS;
        try {
             mapS = Files.readString(mapFolder);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        MapPre mapPre = gson.fromJson(mapS, MapPre.class);
        MapModel mapModel = new MapModel(mapPre);


        System.out.println("a*:--------------------------------------------------------------------");
        AISolver aiSolver = new AISolver(mapModel);
        Deque<MovementRecord> solution1 = aiSolver.aStarSolver();
        System.out.println("尝试次数：");
        System.out.println(AISolver.tryNum);

        /*System.out.println("bfs:--------------------------------------------------------------------");
        AISolver aiSolver2 = new AISolver(mapModel);
        Deque<MovementRecord> solution2 = aiSolver2.bfsSolver();

        System.out.println("尝试次数：");
        System.out.println(AISolver.tryNum);*/

    }
}

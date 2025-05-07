import com.lllllllhp.data.MapPreset;
import com.lllllllhp.model.game.MapModel;
import com.lllllllhp.model.game.MovementRecord;
import com.lllllllhp.utils.aiSolver.AISolver;
import com.lllllllhp.utils.aiSolver.BoxData;

import java.util.Deque;
import java.util.Map;

public class AITest {
    public static void main(String[] args) {
        MapModel mapModel = new MapModel(MapPreset.MAP1);
        AISolver aiSolver = new AISolver(mapModel);
        for (Map.Entry<Integer, BoxData> entry:  aiSolver.getOriginBoxDataMap().entrySet()) {

            System.out.println(entry.getValue());
        }

        Deque<MovementRecord> solution = aiSolver.bfsSolver();

        System.out.println("尝试次数：");
        System.out.println(AISolver.tryNum);

    }
}

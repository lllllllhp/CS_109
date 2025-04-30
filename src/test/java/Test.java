import com.proj510.data.MapPreset;
import com.proj510.model.game.MapModel;
import com.proj510.model.game.MovementRecord;
import com.proj510.utils.aiSolver.AISolver;
import com.proj510.utils.aiSolver.BoxData;

import java.util.Deque;
import java.util.Map;

public class Test {
    public static void main(String[] args) {
        MapModel mapModel = new MapModel(MapPreset.MAP4);
        AISolver aiSolver = new AISolver(mapModel);
        for (Map.Entry<Integer, BoxData> entry:  aiSolver.getOriginBoxDataMap().entrySet()) {

            System.out.println(entry.getValue());
        }

        Deque<MovementRecord> solution = aiSolver.bfsSolver();

        if (solution != null) {
            for (MovementRecord move : solution) {
                System.out.println(move);
            }
        } else {
            System.out.println("无解或 AI 出错");
        }
        System.out.println("尝试次数：");
        System.out.println(AISolver.tryNum);

    }
}

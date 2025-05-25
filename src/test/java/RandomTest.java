import com.lllllllhp.model.game.MapModel;

public class RandomTest {
    public static void main(String[] args) {
        MapModel mapModel = MapModel.getRandomMap();

        mapModel.showMap();
    }
}

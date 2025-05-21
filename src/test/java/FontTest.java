import javafx.scene.text.Font;

public class FontTest {
    public static void main(String[] args) {
        Font font = Font.loadFont(
                FontTest.class.getResourceAsStream("/styles/fonts/zpix/zpix.ttf"),
                12
        );
        System.out.println(font);
    }
}

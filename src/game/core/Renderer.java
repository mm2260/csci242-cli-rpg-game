package game.core;

public class Renderer {

    public static void render(Scene scene) {
//        System.out.println("\n".repeat(5));
        clear();
        System.out.printf(scene.toString());
    }

    private static void clear(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}

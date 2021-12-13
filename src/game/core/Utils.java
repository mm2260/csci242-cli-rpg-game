package game.core;

import java.io.InputStream;

public class Utils {

    public static void printc(String string, GColor color){ System.out.printf(color+string+ GColor.RESET); }

    public static void printc(String format, GColor color, Object ... args) {
        System.out.printf(color+format+ GColor.RESET, args);
    }

    public static String format(GColor color, String format, Object ... args) { return String.format(color+format+GColor.RESET, args);}

    public static InputStream getResource(String name){ return Utils.class.getClassLoader().getResourceAsStream(name); }

    //Colors referred from https://www.codegrepper.com/code-examples/java/java+console+text+color
    public static enum GColor {
        RESET("\u001B[0m"),
        RED("\u001B[31m"),
        GREEN("\u001B[32m"),
        BLUE("\u001B[34m"),
        YELLOW("\u001B[33m"),
        PURPLE("\u001B[35m"),
        CYAN("\u001B[36m");

        public final String value;

        GColor(String value) {
            this.value = value;
        }

        @Override
        public String toString(){
            return this.value;
        }
    }

}

package game.core;

import static game.core.Utils.*;

import java.util.Scanner;
import java.util.stream.IntStream;

public class Scene {

    //Constant:
    private final boolean TRUNCATE = false;
    protected enum LineType { EMPTY, DASHED }

    //Not constant:
    protected StringBuffer buffer;

    public Scene() {
        this.buffer = new StringBuffer();
    }

    public Scene appendToBuffer(String content){
        buffer.append( content );
        return this;
    }

    public static StringBuffer combineVertical(StringBuffer topSection, StringBuffer bottomSection, int sizeX){
        IntStream.range(0,sizeX+1).forEach(i -> topSection.deleteCharAt(topSection.length()-1));
        if(topSection.charAt(topSection.length()-2)=='n'){ topSection.deleteCharAt(topSection.length()-1);}

        return new StringBuffer().append(topSection.toString())
                                 .append(String.format(bottomSection.toString()));
    }

    public static StringBuffer combineHorizontal(StringBuffer leftSection, StringBuffer rightSection){
        Scanner leftLines = new Scanner( String.format(leftSection.toString()) );
        Scanner rightLines = new Scanner( String.format(rightSection.toString()) );

        StringBuffer temp = new StringBuffer();
        while(leftLines.hasNext() && rightLines.hasNext()){
            temp.append(leftLines.nextLine())
                .deleteCharAt(temp.length()-1)
                .append(" ")
                .append(rightLines.nextLine())
                .append(String.format("%n"));
        }

        return temp;//.append(" ");
    }

    public Scene drawSection(String title, int sizeX, int sizeY, String content){
        drawSection(this.buffer, title, sizeX, sizeY, content);
        return this;
    }

    public Scene drawSection(StringBuffer buffer, String title, int sizeX, int sizeY, String content ){
        return drawSection(buffer, title, sizeX, sizeY, 18, content);
    }

    protected Scene drawSection(StringBuffer buffer, String title, int sizeX, int sizeY, int paddingX, String content ){

        //SECTION-BEGINNING:
        if(title!=null){
            int titleLength = getActualLength(title);
            drawLine(buffer, "+","-","+",titleLength+2);
            drawLine(buffer, "", "-", "+%n", sizeX-titleLength-5);
            buffer.append( "| "+title+" |");
            drawLine(buffer, ""," ","|%n",sizeX-titleLength-5);
            drawLine(buffer, "+","-","+",titleLength+2);
            drawLine(buffer, "", " ", "|%n", sizeX-titleLength-5);
        } else { drawLine(buffer, LineType.DASHED, sizeX); }

        //SECTION-CONTENT:
        int lineCount = 0;
        // Take in the content and break it down into separate lines.
        // The content requires a \n escape character to work. If a %n is used,
        // pass function parameter using ... String.format(content) ...
        Scanner lines = new Scanner(content);
        while(lines.hasNext() && lineCount<sizeY-3 || lines.hasNext() && !TRUNCATE ){
            String line = lines.nextLine();
            buffer.append("|").append(" ".repeat(paddingX)).append(line).append(GColor.RESET);
            drawLine(buffer,
                    ""," ","|%n",
                    sizeX-getActualLength(line)-2-paddingX); // White spaces and '|'
            lineCount = lineCount+1;
        }

        IntStream.range(0,sizeY-3-lineCount).forEach( i -> drawLine(buffer,  LineType.EMPTY, sizeX) );

        //SECTION-END:
        drawLine(buffer, LineType.DASHED, sizeX);

        return this;
    }

    protected int getActualLength(String string){
        //The ANSI escape sequences messed with the calculation of string length.
        //Regex referred from StackOverflow 14652538/remove-ascii-color-codes.
        return string.replaceAll("\u001B\\[[;\\d]*[ -/]*[@-~]","").length();
    }

    protected Scene drawLine(StringBuffer buffer, LineType type, int length){
        switch (type){
            case EMPTY:
                drawLine(buffer, "|", " ", "|%n", length-2);
                break;
            case DASHED:
                drawLine(buffer, "+","-","+%n",length-2);
                break;
            default:
                //Should not be reachable.
        }

        return this;
    }

    protected Scene drawLine(StringBuffer buffer, String start, String middle, String end, int length){
        buffer.append(start);
        buffer.append(middle.repeat(length));
        buffer.append(end);
        return this;
    }

    @Override
    public String toString(){ return this.buffer.toString(); }

}

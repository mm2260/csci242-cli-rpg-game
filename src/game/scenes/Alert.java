package game.scenes;

import game.core.Scene;
import game.core.Utils;

import java.util.Scanner;
import java.util.stream.IntStream;

public class Alert extends Scene {

    public static StringBuffer createAlertBox(int sizeX, int sizeY, String content) {
        Alert alertBox = new Alert();

        final String dark = "▒";
        final String gray = "░";
        final String beamV = "║";
        final String beamH = "═";
        final String[] corners = new String[]{"╔", "╗%n", "╚", "╝%n"};

        //SECTION-BEGINNING:
        alertBox.drawLine(alertBox.buffer,
                corners[0], beamH, corners[1],
                sizeX - 2);

        //SECTION-CONTENT:
        int lineCount = 0;
        alertBox.drawLine(alertBox.buffer,
                beamV, " ", beamV + "%n",
                sizeX - 2);

        // Take in the content and break it down into separate lines.
        // The content requires a \n escape character to work. If a %n is used,
        // pass function parameter using ... String.format(content) ...
        Scanner lines = new Scanner(content);
        while (lines.hasNext() && lineCount < sizeY - 5) {
            String line = lines.nextLine();

            int lineLength = alertBox.getActualLength(line);
            int linePadding;

            if (lineLength % 2 == 0) {
                linePadding = (sizeX - lineLength) / 2;
            } else {
                linePadding = (sizeX - lineLength) / 2 + 1;
            }

            if (sizeX % 2 == 0) {
                linePadding = linePadding - 1;
            } else {
                linePadding = linePadding - 2 * (lineLength % 2);
            }


            alertBox.buffer.append(beamV).append(" ".repeat(linePadding)).append(line).append(Utils.GColor.RESET);
            alertBox.drawLine(alertBox.buffer,
                    "", " ", beamV + "%n",
                    linePadding - lineLength % 2 - (lineLength % 2 == 0 ? (sizeX % 2) : (sizeX % 2) * (-1)));
            lineCount = lineCount + 1;
        }

        IntStream.range(0, sizeY - 4 - lineCount).forEach(i -> alertBox.drawLine(alertBox.buffer,
                beamV, " ", beamV + "%n",
                sizeX - 2));

        //SECTION-END:
        alertBox.drawLine(alertBox.buffer,
                corners[2], beamH, corners[3],
                sizeX - 2);

        return alertBox.buffer;
    }

}

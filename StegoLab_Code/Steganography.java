import java.awt.Color;
import java.util.ArrayList;
import java.awt.Point;

public class Steganography {
    
    public static void main(String[] args){
        // Activity 1
        Picture beach = new Picture("/workspace/steno-lab/StegoLab_Code/beach.jpg");
        beach.explore();

        Picture copy = testClearLow(beach);
        copy.explore();

        Picture copy2 = testSetLow(beach, Color.PINK);
        copy2.explore();

        Picture copy3 = revealPicture(copy2);
        copy3.explore();

        // Activity 2
        Picture redMotorcycle = new Picture("/workspace/steno-lab/StegoLab_Code/redMotorcycle.jpg");
        redMotorcycle.explore();

        Picture blueMotorcycle = new Picture("/workspace/steno-lab/StegoLab_Code/blueMotorcycle.jpg");
        blueMotorcycle.explore();

        if (canHide(redMotorcycle, blueMotorcycle)){
            Picture copy4 = hidePicture(redMotorcycle, blueMotorcycle, 0, 0);
            copy4.explore();

            Picture copy5 = revealPicture(copy4);
            copy5.explore();
        }

        // ACTIVITY 3
        beach = new Picture("/workspace/steno-lab/StegoLab_Code/beach.jpg");
        Picture robot = new Picture("/workspace/steno-lab/StegoLab_Code/robot.jpg");
        Picture flower1 = new Picture("/workspace/steno-lab/StegoLab_Code/flower1.jpg");
        
        Picture hidden1 = hidePicture(beach, robot, 65, 208);
        Picture hidden2 = hidePicture(hidden1, flower1, 280, 110);
        hidden2.explore();

        Picture unhidden = revealPicture(hidden2);
        unhidden.explore();

        Picture swan = new Picture("/workspace/steno-lab/StegoLab_Code/swan.jpg");
        Picture swan2 = new Picture("/workspace/steno-lab/StegoLab_Code/swan.jpg");
        System.out.println("Swan and swan2 are the same: " + isSame(swan, swan2));
        swan = testClearLow(swan);
        System.out.println("Swan and swan2 after clearLow on swan: " + isSame(swan, swan2));

        Picture arch1 = new Picture("/workspace/steno-lab/StegoLab_Code/arch.jpg");
        Picture koala = new Picture("/workspace/steno-lab/StegoLab_Code/koala.jpg");
        ArrayList<Point> pointList = findDifferences(arch1, arch1);
        System.out.println("PointList after comparing two identical pictures has a size of: " + pointList.size());
        pointList = findDifferences(arch1, koala);
        System.out.println("PointList after comparing two different sized pictures: "+ pointList.size());
        Picture arch2 = hidePicture(arch1, robot, 0, 0);
        pointList = findDifferences(arch1, arch2);
        System.out.println(isSame(arch1, arch2));
        revealPicture(arch2).explore();
        System.out.println("PointList after hiding a picture has a size of: " + pointList.size());
        arch1.explore();
        arch2.explore();

        Picture hall = new Picture("/workspace/steno-lab/StegoLab_Code/femaleLionAndHall.jpg");
        Picture robot2 = new Picture("/workspace/steno-lab/StegoLab_Code/robot.jpg");
        Picture flower2 = new Picture("/workspace/steno-lab/StegoLab_Code/flower1.jpg");

        Picture hall2 = hidePicture(hall, robot2, 50, 300);
        Picture hall3 = hidePicture(hall2, flower2, 115, 275);
        hall3.explore();
        if (!isSame(hall, hall3)){
            Picture hall4 = showDifferentArea(hall, findDifferences(hall, hall3));
            hall4.explore();
            Picture unhiddenHall3 = revealPicture(hall3);
            unhiddenHall3.explore();

        }

        // --- ACTIVITY 4 --- //
        koala = new Picture("/workspace/steno-lab/StegoLab_Code/koala.jpg");
        koala.explore();
        hideText(koala, "HELLO");
        koala.explore();
        System.out.println(revealText(koala));

    }

    // --- ACTIVITY 1 --- //

    /**
     * Clear the lower (rightmost) two bits in a pixel.
     */
    public static void clearLow(Pixel p){
        p.setRed((p.getRed()/4) * 4);
        p.setGreen((p.getGreen()/4) * 4);
        p.setBlue((p.getBlue()/4) * 4);
    }

    /**
    *For all pixels in Picture p, clear the lower (rightmost) two bits in the pixel.
    */
    public static Picture testClearLow(Picture p){
        Pixel[][] pixels = p.getPixels2D();

        for (Pixel[] rowArray : pixels){
            for (Pixel pixelObj : rowArray){
                clearLow(pixelObj);
            }
        }
        return p;
    }

    /**
     * Set the lower 2 bits in a pixel to the highest 2 bits in c.
    */
    public static void setLow(Pixel p, Color c){
       clearLow(p);

       p.setRed(p.getRed() + (c.getRed()/64));
       p.setGreen(p.getGreen() + (c.getGreen()/64));
       p.setBlue(p.getBlue() + (c.getBlue()/64));
    }

    /**
     * For each pixel in Picture p, set the lowest (rightmost) two bits to the highest (leftmost) two bits of Color c.
     */
    public static Picture testSetLow(Picture p, Color c){
        Pixel[][] pixels = p.getPixels2D();
        for (Pixel[] rowArray : pixels)
        {
            for (Pixel pixelObj : rowArray)
            {
                setLow(pixelObj, c);
            }
        }
        return p;
    }

    /**
     * Sets the highest two bits of each pixel's colors
     * to the lowest two bits of each pixel's colors
     */

    public static Picture revealPicture(Picture hidden){
        Picture copy = new Picture(hidden);
        Pixel[][] pixels = copy.getPixels2D();
        Pixel[][] source = hidden.getPixels2D();

        for (int r = 0; r < pixels.length; r++){
            for (int c = 0; c < pixels[0].length; c++){
                Color col = source[r][c].getColor();

                pixels[r][c].setRed(((col.getRed()%4)*64));
                pixels[r][c].setGreen(((col.getGreen()%4)*64));
                pixels[r][c].setBlue(((col.getBlue()%4)*64));
            }
        }
        return copy;
    }

    // --- ACTIVITY 2 --- //

    /**
     * Determines whether secret can be hidden in source, which is 
     * true if source and secret are the same dimensions.
     * @param source is not null
     * @param secret is not null
     * @return true if secret can be hidden in source, false otherwise.
     */

    public static boolean canHide(Picture source, Picture secret){
        if (source.getHeight() >= secret.getHeight() && source.getWidth() >= secret.getWidth()){
            return true;
        }
        return false;
    }   

    /**
     * Creates a new Picture with data from secret hidden in data from source.
     * @param source is not null
     * @param secret is not null
     * @param startRow is within the source height
     * @param startColumn is within the source width
     * @return combined Picture with secret hidden in source
     * precondition: source is same width and height as secret
     */

    public static Picture hidePicture(Picture source, Picture secret, int startRow, int startColumn){
        Pixel[][] pixels = source.getPixels2D();
        Pixel[][] pixels2 = secret.getPixels2D();

        for (int i = 0; i < secret.getHeight(); i++){
            for (int j = 0; j < secret.getWidth(); j++){
                int altStartRow = i+startRow;
                int altStartCol = j+startColumn;

                clearLow(pixels[altStartRow][altStartCol]);
                setLow(pixels[altStartRow][altStartCol], pixels2[i][j].getColor());
            }
        }
        return source;
    }

    // --- ACTIVITY 3 --- //

    public static boolean isSame (Picture source, Picture question){
        Pixel[][] srcArr = source.getPixels2D();
        Pixel[][] qtnArr = question.getPixels2D();

        try{
            for (int i = 0; i < srcArr.length; i++){
                for (int j = 0; j < srcArr[i].length; j++){
                    if (srcArr[i][j].getAverage() != qtnArr[i][j].getAverage()){
                        return false;
                    }
                }
            }
        } catch (Exception e){
            return false;
        }
        return true;
    }

    public static ArrayList<Point> findDifferences(Picture source, Picture diff){
        ArrayList<Point> coordinates = new ArrayList<Point>(0);

        if (isSame(source, diff)){
            Pixel[][] srcArr = source.getPixels2D();
            Pixel[][] diffArr = diff.getPixels2D();

            for (int y = 0; y < srcArr.length; y++){
                for (int x = 0; x < srcArr[y].length; x++){
                    if (!srcArr[y][x].getColor().equals(diffArr[y][x].getColor())){
                        coordinates.add(new Point(x,y));
                    }
                }
            }

        }
        return coordinates;
    }

    public static Picture showDifferentArea(Picture src, ArrayList<Point> coordinates){
        Picture pic = src;
        Pixel[][] pixels = pic.getPixels2D();

        int lowestY = pixels.length;
        int highestY = 0;
        int lowestX = pixels[0].length;
        int highestX = 0;

        for (Point p : coordinates){
            if (p.getY() > highestY){
                highestY = (int) p.getY();
            } else if (p.getY() < lowestY){
                lowestY = (int) p.getY();
            } else if (p.getX() > highestX){
                highestX = (int) p.getX();
            } else if (p.getX() < lowestX){
                lowestX = (int) p.getX();
            }
        }
        for (int i = lowestY; i < highestY; i++){
            for (int j = lowestX; j < highestX; j++){
                pixels[i][j].setColor(Color.BLACK);
            }
        }

        return pic;
    }




    public static ArrayList<Integer> encodeString (String s){
        s = s.toUpperCase();
        String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        ArrayList<Integer> result = new ArrayList<Integer>();

        for (int i = 0; i < s.length(); i++){
            if (s.substring(i, i+1).equals(" ")){
                result.add(27);
            } else {
                result.add(alpha.indexOf(s.substring(i, i+1))+1);
            }
        }
        result.add(0);
        return result;
        
    }

    private static String decodeString(ArrayList<Integer> codes){
        String result = "";
        String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (int i = 0; i < codes.size(); i++){
            if (codes.get(i) == 27){
                result = result + "";
            } else {
                try{
                    result = result + alpha.substring(codes.get(i)-1, codes.get(i));
                } catch (Exception e){
                    break;
                }
            }
        }
        return result;
    }

    private static int[] getBitPairs(int num){
        int[] bits = new int[3];
        int code = num;
        for (int i = 0; i < 3; i++){
            bits[i] = code % 4;
            code = code/4;
        }
        return bits;
    }

    public static void hideText(Picture source, String s){
        ArrayList<Integer> ints = encodeString(s);
        ArrayList<int[]> bits = new ArrayList<int[]>();
        for (Integer i : ints){
            bits.add(getBitPairs(i));
        }

        Pixel[][] pixels = source.getPixels2D();

        int index = 0;

        for (Pixel[] row : pixels){
            for (Pixel p : row){
                try{
                    clearLow(p);

                    p.setRed(p.getRed() + bits.get(index)[0]);
                    p.setGreen(p.getGreen() + bits.get(index)[1]);
                    p.setBlue(p.getBlue() + bits.get(index)[2]);
                    index++;
                } catch (Exception e ){break;}
            }
        }
    }

    public static String revealText(Picture source){
        ArrayList<Integer> ints = new ArrayList<Integer>();
        ArrayList<int[]> bits = new ArrayList<int[]>();

        Pixel[][] pixels = source.getPixels2D();


        for (Pixel[] row : pixels){
            for (Pixel p : row){
                bits.add(new int[]{(p.getRed()%4), (p.getGreen()%4), (p.getBlue()%4)});
            }
        }

        for (int[] bit : bits){
            ints.add((bit[0]) + (bit[1]*4) + (bit[2]*16));
        }

        return decodeString(ints);
    }
}

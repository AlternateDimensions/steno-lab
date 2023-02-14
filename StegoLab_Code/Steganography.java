import java.awt.Color;
import java.util.ArrayList;

public class Steganography {
    
    public static void main(String[] args){
        Picture beach = new Picture("/workspace/steno-lab/StegoLab_Code/beach.jpg");
        beach.explore();

        Picture copy2 = testSetLow(beach, Color.PINK);
        copy2.explore();

        Picture copy3 = revealPicture(copy2);
        copy3.explore();
        
        // ------- //

        Picture robot = new Picture("/workspace/steno-lab/StegoLab_Code/robot.jpg");
        Picture flower1 = new Picture("/workspace/steno-lab/StegoLab_Code/flower1.jpg");

        Picture hidden1 = hidePicture(beach, robot, 65, 208);
        Picture hidden2 = hidePicture(hidden1, flower1, 280, 110);
        hidden2.explore();

        Picture unhidden = revealPicture(hidden2);
        unhidden.explore();

        // ------- //

        Picture motorcycle = new Picture("/workspace/steno-lab/StegoLab_Code/redMotorcycle.jpg");
        motorcycle.explore();
        
        if (canHide(beach, motorcycle)){
            Picture hidden = hidePicture(beach, motorcycle, 0, 0);
            hidden.explore();
            Picture revealTime = revealPicture(hidden);
            revealTime.explore();
        }

        // ------- //

        Picture swan = new Picture("/workspace/steno-lab/StegoLab_Code/swan.jpg");
        Picture swan2 = new Picture("/workspace/steno-lab/StegoLab_Code/swan.jpg");

        System.out.println("Swan and Swan 2 are the same: "+ isSame(swan, swan2));
        swan = testClearLow(swan);
        System.out.println("Swan and Swan 2 are the same after testClearLow on swan: "+ isSame(swan, swan2));


        Picture arch = new Picture("/workspace/steno-lab/StegoLab_Code/arch.jpg");
        Picture robot1 = new Picture("/workspace/steno-lab/StegoLab_Code/robot.jpg");

        ArrayList<int[]> coordList = findDifferences(arch, arch);
        System.out.println("Comparing arch to itself reveals "+coordList.size()+" differences.");
        
        Picture arch2 = hidePicture(arch, robot1, 65, 102);
        arch2 = revealPicture(arch2);

        coordList = findDifferences(arch, arch2);
        System.out.println("Differences between original and modified arch: "+coordList.size());

        hideText(arch,"Hello World!");

        // ------ //
        Picture koala = new Picture("/workspace/steno-lab/StegoLab_Code/koala.jpg");
        koala.explore();
        hideText(koala, "HELLO");
        koala.explore();
        System.out.println(revealText(koala));




        



        


    }

    /*
     * Clear the lower (rightmost) two bits in a pixel.
     */

     public static void clearLow(Pixel p){
        
        p.setRed((p.getRed()/4) * 4);
        p.setGreen((p.getGreen()/4) * 4);
        p.setBlue((p.getBlue()/4) * 4);
     }

    public static Picture testClearLow(Picture p){
        Pixel[][] pixels = p.getPixels2D();
        for (Pixel[] rowArray : pixels)
        {
            for (Pixel pixelObj : rowArray)
            {
                clearLow(pixelObj);
            }
        }
        return p;
    }

    public static void setLow(Pixel p, Color c){
       clearLow(p);

       p.setRed(p.getRed() + (c.getRed()/64));
       p.setGreen(p.getGreen() + (c.getGreen()/64));
       p.setBlue(p.getBlue() + (c.getBlue()/64));
    }

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

    public static Picture revealPicture(Picture hidden){
        Picture copy = new Picture(hidden);
        Pixel[][] pixels = copy.getPixels2D();
        Pixel[][] source = hidden.getPixels2D();

        for (int r = 0; r < pixels.length; r++){
            for (int c = 0; c < pixels[0].length; c++){
                Color col = source[r][c].getColor();

                pixels[r][c].setRed((col.getRed()%4)*64);
                pixels[r][c].setGreen((col.getGreen()%4)*64);
                pixels[r][c].setBlue((col.getBlue()%4)*64);
            }
        }
       
        return copy;
    }




    public static boolean canHide(Picture source, Picture secret){
        if (source.getHeight() == secret.getHeight() && source.getWidth() == secret.getWidth()){
            return true;
        }
        return false;
    }   

    public static Picture hidePicture(Picture source, Picture secret, int startRow, int startColumn){
        Pixel[][] pixels = source.getPixels2D();
        Pixel[][] pixels2 = secret.getPixels2D();

        for (int i = 0; i < secret.getHeight(); i++){
            for (int j = 0; j < secret.getWidth(); j++){
                clearLow(pixels[i+startRow][j+startColumn]);

                pixels[i+startRow][j+startColumn].setRed(pixels[i+startRow][j+startColumn].getRed() + (pixels2[i][j].getRed()/64));
                pixels[i+startRow][j+startColumn].setGreen(pixels[i+startRow][j+startColumn].getGreen() + (pixels2[i][j].getGreen()/64));
                pixels[i+startRow][j+startColumn].setBlue(pixels[i+startRow][j+startColumn].getBlue() + (pixels2[i][j].getBlue()/64));
            }
        }


        return source;
    }



    public static boolean isSame (Picture source, Picture question){
        Pixel[][] sourcePixels = source.getPixels2D();
        Pixel[][] questionPixels = question.getPixels2D();

        if (canHide(source, question)){
            for (int i = 0; i < source.getHeight(); i++){
                for (int j = 0; j < source.getWidth(); j++){
                    if (!sourcePixels[i][j].getColor().equals(questionPixels[i][j].getColor())){
                       return false;
                    } 
                    
                }
            }
            return true;
        }
        return false;
    }

    public static ArrayList<int[]> findDifferences(Picture imageOne, Picture imageTwo){
        Pixel[][] onePixels = imageOne.getPixels2D();
        Pixel[][] twoPixels = imageTwo.getPixels2D();

        ArrayList<int[]> coordinates = new ArrayList<int[]>();
        int height = imageOne.getHeight() > imageTwo.getHeight() ? imageOne.getHeight() : imageTwo.getHeight();
        int width = imageOne.getWidth() > imageTwo.getWidth() ? imageOne.getWidth() : imageTwo.getWidth();

        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                if (onePixels[i][j].getColor().getRGB() == twoPixels[i][j].getColor().getRGB()){
                    continue;
                } else {
                    coordinates.add((new int[]{i, j}));
                }
            }
        }

        return coordinates;
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

public class Activity5 {

    public static void main(String[] args){
        // Figure out how to merge two images together

        Picture flower1 = new Picture ("/workspace/steno-lab/StegoLab_Code/flower1.jpg");
        Picture flower2 = new Picture("/workspace/steno-lab/StegoLab_Code/flower2.jpg");

        Pixel[][] flower1Pixels = flower1.getPixels2D();
        Pixel[][] flower2Pixels = flower2.getPixels2D();
        

        for (int i = 0; i < flower1Pixels.length; i++){
            for (int j = 0; j < flower1Pixels[i].length; j++){
                if (flower1Pixels[i][j].getRed() >= 126 && flower1Pixels[i][j].getGreen() >= 126 && flower1Pixels[i][j].getBlue() >= 126) {
                    flower1Pixels[i][j].setColor(flower2Pixels[i][j].getColor());
                }
            }
        }

        flower1.explore();



        Picture barb = new Picture("/workspace/steno-lab/StegoLab_Code/barbaraS.jpg");
        Picture beach = new Picture("/workspace/steno-lab/StegoLab_Code/beach.jpg");

        barb.explore();
        beach.explore();

        // barb only

        Pixel[][] barbPixels = barb.getPixels2D();
        Pixel[][] beachPixels = beach.getPixels2D();

        int rowStart = 10;
        int rowEnd = 100;
        int colStart = 31;
        int colEnd = 91;

        int pixelStartRow = 207;
        int pixelStartCol = 507;



        for (int i = pixelStartRow; i < pixelStartRow+(rowEnd-rowStart); i++){
            for (int j = pixelStartCol; j < pixelStartCol+(colEnd-colStart); j++){
                int rowDiff = i-pixelStartRow;
                int colDiff = j-pixelStartCol;

                beachPixels[i][j].setColor(barbPixels[rowStart+rowDiff][colStart+colDiff].getColor());
            }
        }

        beach.explore();
    }
    
}

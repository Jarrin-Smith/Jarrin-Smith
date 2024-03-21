import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;

public class SeamCarving {
    private final String filename;
    private Image img;

    SeamCarving (String filename) throws IOException {
        this.filename = filename;
        img = new Image(filename);
    }

    void write (String prefix) throws IOException {
        img.write(prefix + filename);
    }

    Image getImg () { return img; }

    /**
     * The method finds the seam with the least energy starting
     * from the current pixel and going down to the last row.
     * Here we just write a recursive solution that may
     * recompute subproblems.
     *
     * If the current pixel is at the bottom row, we return
     * a path that contains just the current pixel and its
     * energy.
     *
     * If the current cell is somewhere other than the last row,
     * then we compute the best seam from each of its southern
     * neighbors and take the minimum.
     *
     * A short elegant solution is possible using the map and
     * min methods on streams.
     */
    PathAndCost findSeamFrom (Image.Pixel px) {
        if(px.inLastRow()){
            return new PathAndCost(Path.singleton(px), getImg().computeEnergy(px));
        }
        else{
            return getImg().getBelowNeighbors(px).stream().map(n -> (findSeamFrom(n).add(px, getImg().computeEnergy(px)))).min(PathAndCost::compareTo).get();
        }
    }

    /**
     * The method finds all the seams that start at the top
     * row and returns the minimum.
     */
    Path<Image.Pixel> bestSeam () {
        return getImg().topRow().stream().map(n -> (findSeamFrom(n))).min(PathAndCost::compareTo).get().seam();
    }

    void cutN (int n) {
        for (int i=0; i<n; i++) img = img.cutSeam(bestSeam());
    }
}


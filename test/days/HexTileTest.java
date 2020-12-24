package days;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HexTileTest {
    @Test
    void assertLoop() {
        var grid = new HexGrid();
        var position = new HexGrid.Position(0,0,0);

        HexTile start = new HexTile(grid, position);
        grid.put(position, start);
        List<Direction> loop = Direction.parseLine("nwwswee");

        HexTile end = start.walk(loop);
        assertEquals(start, end);
    }
}
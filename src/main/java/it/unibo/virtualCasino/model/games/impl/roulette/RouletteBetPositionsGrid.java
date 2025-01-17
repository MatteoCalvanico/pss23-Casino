package it.unibo.virtualCasino.model.games.impl.roulette;

import java.util.ArrayList;

import it.unibo.virtualCasino.model.games.impl.roulette.dtos.RouletteTableLayout;
import it.unibo.virtualCasino.model.games.impl.roulette.dtos.Coordinate;
import it.unibo.virtualCasino.model.games.impl.roulette.dtos.RouletteBetPositionIndicator;
import it.unibo.virtualCasino.model.games.impl.roulette.enums.RouletteBetType;

/**
 * The {@code RouletteBetPositionsGrid} class manages the layout of bets in a
 * roulette game. It prepares and stores indicators for various bet types based
 * on the provided table layout data.
 * <p>
 * This class uses a grid to represent different betting positions on the
 * roulette table. The grid layout is determined by offsets for both horizontal
 * and vertical lines, which are used to define the positions of different bet
 * types.
 */
public class RouletteBetPositionsGrid extends RouletteBase {

    /**
     * The vertical offset between horizontal lines in the grid.
     */
    private final double horizontalLinesVerticalOffset;

    /**
     * The horizontal offset between vertical lines in the grid.
     */
    private final double verticalLinesHorizontalOffset;

    /**
     * The layout of the roulette table.
     */
    private final RouletteTableLayout tableLayout;

    /**
     * The list of bet position indicators.
     */
    private final ArrayList<RouletteBetPositionIndicator> betPositionIdicatorsList = new ArrayList<RouletteBetPositionIndicator>();

    /**
     * Constructs a {@code RouletteBetPositionsGrid} with the specified table
     * layout.
     *
     * @param tableLayout the layout of the roulette table
     */
    public RouletteBetPositionsGrid(RouletteTableLayout tableLayout) {
        this.tableLayout = new RouletteTableLayout(tableLayout);

        this.horizontalLinesVerticalOffset = Math
                .abs(tableLayout.topLeftCoordinate.yAxisValue - tableLayout.bottomRightCoordinate.yAxisValue)
                / this.TABLE_COLS;

        this.verticalLinesHorizontalOffset = Math
                .abs(tableLayout.topLeftCoordinate.xAxisValue - tableLayout.bottomRightCoordinate.xAxisValue)
                / (this.TABLE_ROWS + 1);

        prepareBetPositionIdicatorsLayoutData();
    }

    /**
     * Retrieves a filtered list of {@code RouletteBetIndicatorDto} objects that
     * match the specified bet type.
     * <p>
     *
     * @param betType the type of bet to filter by
     * @return a list of {@code RouletteBetIndicatorDto} objects matching the
     *         specified bet type
     */
    public ArrayList<RouletteBetPositionIndicator> getBetPositionIdicatorsListByBetType(RouletteBetType betType) {
        ArrayList<RouletteBetPositionIndicator> listCopy = new ArrayList<>(betPositionIdicatorsList);

        listCopy.removeIf(betPositionIndicator -> betPositionIndicator.betType != betType);

        return listCopy;
    }

    public RouletteBetPositionIndicator getBetPositionIndicatorById(String betPositionIndicatorId) throws Exception {
        for (int i = 0; i < betPositionIdicatorsList.size(); i++) {
            RouletteBetPositionIndicator rouletteBetPositionIndicator = betPositionIdicatorsList.get(i);
            if (rouletteBetPositionIndicator.Id != null
                    && rouletteBetPositionIndicator.Id.equals(betPositionIndicatorId)) {
                return rouletteBetPositionIndicator;
            }
        }
        throw new Exception("Indicator not found");
    }

    /**
     * Prepares the layout data for bet position indicators by creating various
     * types of bet indicators.
     */
    private void prepareBetPositionIdicatorsLayoutData() {

        // Bet type: Straight up
        createStraightUpBetsPositionsIndicators();

        // Bet type: Split
        createSplitBetsPositionsInidicators();

        // Bet type: Street
        createStreetBetsPositionsInidicators();

        // Bet type: Double
        createDoubleStreetBetsPositionsInidicators();

        // Bet type: Corner
        createCornerBetsPositionsInidicators();

        // Bet type: Column
        createColumnBetsPositionsInidicators();

        // Bet type: Dozen
        createDozenBetsPositionsInidicators();

        // Bet type: Half
        createHalfBetsPositionsInidicators();

        // Bet type: Even
        createEvenOddBetsPositionsInidicators();

        // Bet type: Red or Black
        createRedBlackBetsPositionsInidicators();
    }

    /**
     * Creates position indicators for strainght up bets and adds them to the list.
     */
    private void createStraightUpBetsPositionsIndicators() {
        // Creates horizontal split bets indicators layout data items
        double halfHorizontalLinesVerticalOffset = horizontalLinesVerticalOffset / 2;
        double halfVerticalLinesHorizontalOffset = verticalLinesHorizontalOffset / 2;
        for (int i = 1; i <= this.TABLE_COLS; i++) {
            double yAxisValue = tableLayout.bottomRightCoordinate.yAxisValue
                    - (horizontalLinesVerticalOffset * i)
                    + (halfHorizontalLinesVerticalOffset);

            for (int j = 1; j <= this.TABLE_ROWS; j++) {
                double xAxisValue = tableLayout.topLeftCoordinate.xAxisValue
                        + (verticalLinesHorizontalOffset * j)
                        - (halfVerticalLinesHorizontalOffset);

                betPositionIdicatorsList.add(
                        new RouletteBetPositionIndicator(
                                RouletteBetType.STRAIGHT_UP,
                                (i + j) - 1,
                                new Coordinate(xAxisValue, yAxisValue)));
            }
        }
    }

    /**
     * Creates position indicators for split bets and adds them to the list.
     */
    private void createSplitBetsPositionsInidicators() {
        // Creates horizontal split bets indicators layout data items
        int splitBetsCounter = 0;
        double halfVerticalLinesHorizontalOffset = verticalLinesHorizontalOffset / 2;
        for (int i = 0; i < this.TABLE_ROWS; i++) {
            double xAxisValue = tableLayout.topLeftCoordinate.xAxisValue
                    + (verticalLinesHorizontalOffset * i)
                    + halfVerticalLinesHorizontalOffset;

            for (int j = 1; j < this.TABLE_COLS; j++) {
                double yAxisValue = tableLayout.bottomRightCoordinate.yAxisValue
                        - (horizontalLinesVerticalOffset * j);

                betPositionIdicatorsList.add(
                        new RouletteBetPositionIndicator(
                                RouletteBetType.SPLIT,
                                splitBetsCounter++,
                                new Coordinate(xAxisValue, yAxisValue)));
            }
        }

        // Creates vertical split bets positions indicators

        for (int j = 1; j <= this.MAX_HORIZONTAL_SPLITS / 3; j++) {
            double xAxisValue = tableLayout.topLeftCoordinate.xAxisValue
                    + (verticalLinesHorizontalOffset * j);

            for (int i = 0; i < this.TABLE_COLS; i++) {
                double yAxisValue = tableLayout.bottomRightCoordinate.yAxisValue
                        - (horizontalLinesVerticalOffset * i)
                        - horizontalLinesVerticalOffset / 2;

                betPositionIdicatorsList.add(
                        new RouletteBetPositionIndicator(
                                RouletteBetType.SPLIT,
                                splitBetsCounter++,
                                new Coordinate(xAxisValue, yAxisValue)));
            }
        }
    }

    /**
     * Creates position indicators for street bets and adds them to the list.
     */
    private void createStreetBetsPositionsInidicators() {
        double halfVerticalLinesHorizontalOffset = verticalLinesHorizontalOffset / 2;
        for (int i = 0; i < this.TABLE_ROWS; i++) {
            double xAxisValue = tableLayout.topLeftCoordinate.xAxisValue
                    + halfVerticalLinesHorizontalOffset
                    + (verticalLinesHorizontalOffset * (i));

            betPositionIdicatorsList.add(
                    new RouletteBetPositionIndicator(
                            RouletteBetType.STREET,
                            i + 1,
                            new Coordinate(xAxisValue, tableLayout.topLeftCoordinate.yAxisValue)));
        }
    }

    /**
     * Creates position indicators for double street bets and adds them to the list.
     */
    private void createDoubleStreetBetsPositionsInidicators() {
        for (int i = 1; i < TABLE_ROWS; i++) {
            double xAxisValue = tableLayout.topLeftCoordinate.xAxisValue
                    + (verticalLinesHorizontalOffset * (i));

            betPositionIdicatorsList.add(
                    new RouletteBetPositionIndicator(
                            RouletteBetType.DOUBLE_STREET,
                            i,
                            new Coordinate(xAxisValue, tableLayout.bottomRightCoordinate.yAxisValue)));
        }
    }

    /**
     * Creates position indicators for corner bets and adds them to the list.
     */
    private void createCornerBetsPositionsInidicators() {
        int counter = 1;
        for (int i = 1; i < this.TABLE_ROWS; i++) {
            double xAxisValue = tableLayout.topLeftCoordinate.xAxisValue
                    + (verticalLinesHorizontalOffset * i);

            for (int j = 1; j < this.TABLE_COLS; j++) {
                double yAxisValue = tableLayout.bottomRightCoordinate.yAxisValue
                        - (horizontalLinesVerticalOffset * j);

                betPositionIdicatorsList.add(
                        new RouletteBetPositionIndicator(
                                RouletteBetType.CORNER,
                                counter++,
                                new Coordinate(xAxisValue, yAxisValue)));
            }
        }
    }

    /**
     * Creates position indicators for column bets and adds them to the list.
     */
    private void createColumnBetsPositionsInidicators() {
        double xAxisValue = tableLayout.bottomRightCoordinate.xAxisValue
                - verticalLinesHorizontalOffset / 2;

        double halfHorizontalLinesVerticalOffset = horizontalLinesVerticalOffset / 2;
        for (int i = 0; i < this.TABLE_COLS; i++) {
            double yAxisValue = tableLayout.bottomRightCoordinate.yAxisValue
                    - (horizontalLinesVerticalOffset * i)
                    - halfHorizontalLinesVerticalOffset;

            betPositionIdicatorsList.add(
                    new RouletteBetPositionIndicator(
                            RouletteBetType.COLUMN,
                            i + 1,
                            new Coordinate(xAxisValue, yAxisValue)));
        }
    }

    /**
     * Creates position indicators for dozen bets and adds them to the list.
     */
    private void createDozenBetsPositionsInidicators() {
        double yAxisValue = tableLayout.bottomRightCoordinate.yAxisValue
                + (Math.abs(tableLayout.bottomRightCoordinate.yAxisValue - tableLayout.bottomLeftCoordinate.yAxisValue)
                        / (this.TABLE_ROWS / this.TABLE_COLS));
        double offset = (tableLayout.bottomRightCoordinate.xAxisValue - verticalLinesHorizontalOffset
                - tableLayout.topLeftCoordinate.xAxisValue) / this.TABLE_COLS;
        double halfOffset = offset / 2;

        for (int i = 0; i < this.TABLE_COLS; i++) {
            double xAxisValue = tableLayout.topLeftCoordinate.xAxisValue + ((offset * i) + (halfOffset));

            betPositionIdicatorsList.add(
                    new RouletteBetPositionIndicator(
                            RouletteBetType.DOZEN,
                            i + 1,
                            new Coordinate(xAxisValue, yAxisValue)));
        }
    }

    /**
     * Creates position indicators for half bets and adds them to the list.
     */
    private void createHalfBetsPositionsInidicators() {
        double yAxisValue = tableLayout.bottomLeftCoordinate.yAxisValue
                - (Math.abs(tableLayout.bottomRightCoordinate.yAxisValue - tableLayout.bottomLeftCoordinate.yAxisValue)
                        / (this.TABLE_ROWS / this.TABLE_COLS));
        double offset = ((tableLayout.bottomRightCoordinate.xAxisValue - verticalLinesHorizontalOffset
                - tableLayout.topLeftCoordinate.xAxisValue) / (this.TABLE_ROWS / 2))
                / 2;

        double xAxisValue = tableLayout.topLeftCoordinate.xAxisValue + offset;
        betPositionIdicatorsList.add(
                new RouletteBetPositionIndicator(
                        RouletteBetType.HALF,
                        1,
                        new Coordinate(xAxisValue, yAxisValue)));

        xAxisValue = tableLayout.bottomRightCoordinate.xAxisValue
                - verticalLinesHorizontalOffset
                - offset;
        betPositionIdicatorsList.add(
                new RouletteBetPositionIndicator(
                        RouletteBetType.HALF,
                        2,
                        new Coordinate(xAxisValue, yAxisValue)));

    }

    /**
     * Creates position indicators for even or odd bets and adds them to the list.
     */
    private void createEvenOddBetsPositionsInidicators() {
        double yAxisValue = tableLayout.bottomLeftCoordinate.yAxisValue
                - (Math.abs(tableLayout.bottomRightCoordinate.yAxisValue - tableLayout.bottomLeftCoordinate.yAxisValue)
                        / (this.TABLE_ROWS / this.TABLE_COLS));
        double offset = (tableLayout.bottomRightCoordinate.xAxisValue - verticalLinesHorizontalOffset
                - tableLayout.topLeftCoordinate.xAxisValue) / (this.TABLE_ROWS / 2);

        double xAxisValue = tableLayout.topLeftCoordinate.xAxisValue + (offset / 2) + offset;
        betPositionIdicatorsList.add(
                new RouletteBetPositionIndicator(
                        RouletteBetType.ODD_EVEN,
                        1,
                        new Coordinate(xAxisValue, yAxisValue)));

        xAxisValue = tableLayout.bottomRightCoordinate.xAxisValue - verticalLinesHorizontalOffset
                - (offset / 2) - offset;
        betPositionIdicatorsList.add(
                new RouletteBetPositionIndicator(
                        RouletteBetType.ODD_EVEN,
                        2,
                        new Coordinate(xAxisValue, yAxisValue)));
    }

    /**
     * Creates position indicators for red or black bets and adds them to the list.
     */
    private void createRedBlackBetsPositionsInidicators() {
        double yAxisValue = tableLayout.bottomLeftCoordinate.yAxisValue
                - (Math.abs(tableLayout.bottomRightCoordinate.yAxisValue - tableLayout.bottomLeftCoordinate.yAxisValue)
                        / (this.TABLE_ROWS / this.TABLE_COLS));
        double offset = (tableLayout.bottomRightCoordinate.xAxisValue - verticalLinesHorizontalOffset
                - tableLayout.topLeftCoordinate.xAxisValue) / (this.TABLE_ROWS / 2);

        double xAxisValue = tableLayout.topLeftCoordinate.xAxisValue + (offset / 2) + offset * 2;
        betPositionIdicatorsList.add(
                new RouletteBetPositionIndicator(
                        RouletteBetType.RED_BLACK,
                        1,
                        new Coordinate(xAxisValue, yAxisValue)));

        xAxisValue = tableLayout.bottomRightCoordinate.xAxisValue - verticalLinesHorizontalOffset
                - (offset / 2) - offset * 2;
        betPositionIdicatorsList.add(
                new RouletteBetPositionIndicator(
                        RouletteBetType.RED_BLACK,
                        2,
                        new Coordinate(xAxisValue, yAxisValue)));
    }
}

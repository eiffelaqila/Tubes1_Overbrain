package za.co.entelect.challenge;

import za.co.entelect.challenge.command.*;
import za.co.entelect.challenge.entities.*;
import za.co.entelect.challenge.enums.PowerUps;
import za.co.entelect.challenge.enums.Terrain;

import java.util.*;

import static java.lang.Math.max;

public class Bot {

    private static final int maxSpeed = 9;
    private List<Command> directionList = new ArrayList<>();

    private Random random;
    private GameState gameState;
    private Car opponent;
    private Car myCar;
    private final static Command ACCELERATE = new AccelerateCommand();
    private final static Command LIZARD = new LizardCommand();
    private final static Command OIL = new OilCommand();
    private final static Command BOOST = new BoostCommand();
    private final static Command EMP = new EmpCommand();
    private final static Command FIX = new FixCommand();

    private final static Command TURN_RIGHT = new ChangeLaneCommand(1);
    private final static Command TURN_LEFT = new ChangeLaneCommand(-1);

    public Bot(Random random, GameState gameState) {
        this.random = random;
        this.gameState = gameState;
        this.myCar = gameState.player;
        this.opponent = gameState.opponent;

        directionList.add(TURN_LEFT);
        directionList.add(TURN_RIGHT);
    }

    public Command run() {
        List<Object> blocks = getBlocksInFront(myCar.position.lane, myCar.position.block, getSpeedAfterAccelerate());
        List<Object> leftBlocks;
        List<Object> rightBlocks;

        if (doSaveTheCar()) {
            return FIX;
        }
        else if (doTurbo()) {
            return BOOST;
        }
        else if (myCar.speed != 0 && (blocks.contains(Terrain.MUD) || blocks.contains(Terrain.WALL))) {
            int i = random.nextInt(directionList.size());
            if (hasPowerUp(PowerUps.LIZARD, myCar.powerups)) {
                if (myCar.position.lane == 1) {
                    rightBlocks = getBlocksInFront(myCar.position.lane + 1, myCar.position.block, getSpeedAfterAccelerate());
                    if (rightBlocks.contains(Terrain.MUD) || rightBlocks.contains(Terrain.WALL)) {
                        return LIZARD;
                    } else {
                        return TURN_RIGHT;
                    }
                } else if (myCar.position.lane == 4) {
                    leftBlocks = getBlocksInFront(myCar.position.lane - 1, myCar.position.block, getSpeedAfterAccelerate());
                    if (leftBlocks.contains(Terrain.MUD) || leftBlocks.contains(Terrain.WALL)) {
                        return LIZARD;
                    } else {
                        return TURN_LEFT;
                    }
                } else {
                    leftBlocks = getBlocksInFront(myCar.position.lane - 1, myCar.position.block, getSpeedAfterAccelerate());
                    rightBlocks = getBlocksInFront(myCar.position.lane + 1, myCar.position.block, getSpeedAfterAccelerate());
                    if (leftBlocks.contains(Terrain.MUD) || leftBlocks.contains(Terrain.WALL)) {
                        if (rightBlocks.contains(Terrain.MUD) || rightBlocks.contains(Terrain.WALL)) {
                            return LIZARD;
                        } else {
                            return TURN_RIGHT;
                        }
                    } else {
                        return TURN_LEFT;
                    }
                }
            } else {
                if (myCar.position.lane == 1) {
                    return TURN_RIGHT;
                } else if (myCar.position.lane == 4) {
                    return TURN_LEFT;
                } else {
                    leftBlocks = getBlocksInFront(myCar.position.lane - 1, myCar.position.block, getSpeedAfterAccelerate());
                    rightBlocks = getBlocksInFront(myCar.position.lane + 1, myCar.position.block, getSpeedAfterAccelerate());
                    if (leftBlocks.contains(Terrain.MUD) || leftBlocks.contains(Terrain.WALL)) {
                        if (!rightBlocks.contains(Terrain.MUD) || !rightBlocks.contains(Terrain.WALL)) {
                            return TURN_RIGHT;
                        }
                    } else if (rightBlocks.contains(Terrain.MUD) || rightBlocks.contains(Terrain.WALL)) {
                        if (!leftBlocks.contains(Terrain.MUD) || !leftBlocks.contains(Terrain.WALL)) {
                            return TURN_LEFT;
                        }
                    }
                }
            }
            return directionList.get(i);
        }
        return ACCELERATE;
    }
 
    public boolean doSaveTheCar() {
        /* Fungsi ini bertujuan untuk memprioritaskan mobil untuk melakukan FIX ketika mobil:
           - menerima damage >= 2 dan telah mencapai max speed atau 
           - menerima damage > 0 dan memiliki powerups: Boost */
        if (((myCar.damage == 2 && myCar.speed == 8) ||
                (myCar.damage == 3 && myCar.speed == 6) ||
                (myCar.damage == 4 && myCar.speed == 3) ||
                (myCar.damage >= 5)) && (!hasPowerUp(PowerUps.BOOST, myCar.powerups))) {
            return true;
        }
        else if (myCar.damage > 0 && hasPowerUp(PowerUps.BOOST, myCar.powerups)) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean doTurbo() {
        /* Fungsi ini bertujuan untuk memprioritaskan penggunaan Boost ketika mobil
           memiliki boost, tidak menerima damage, dan tidak berada di kecepatan maksimal */
        if (myCar.damage == 0 && hasPowerUp(PowerUps.BOOST, myCar.powerups) && myCar.speed != 15) {
            return true;
        }
    }

    private Boolean hasPowerUp(PowerUps powerUpToCheck, PowerUps[] available) {
        for (PowerUps powerUp: available) {
            if (powerUp.equals(null)) {
                return false;
            }
            if (powerUp.equals(powerUpToCheck)) {
                return true;
            }
        }
        return false;
    }

    private int getSpeedAfterAccelerate() {
        if (myCar.speed2 == 3 || myCar.speed == 5) {
            return 6;
        } else if (myCar.speed == 6) {
            return 8;
        } else if (myCar.speed == 8 || (myCar.speed == 9 && !hasPowerUp(PowerUps.BOOST, myCar.powerups))) {
            return 9;
        } else if ((myCar.speed == 9 && hasPowerUp(PowerUps.BOOST, myCar.powerups)) || myCar.speed == 15) {
            return 15;
        } else {
            return 0;
        }
    }

    /**
     * Returns map of blocks and the objects in the for the current lanes, returns the amount of blocks that can be
     * traversed at max speed.
     **/
    private List<Object> getBlocksInFront(int lane, int block, int speed) {
        List<Lane[]> map = gameState.lanes;
        List<Object> blocks = new ArrayList<>();
        int startBlock = map.get(0)[0].position.block;

        Lane[] laneList = map.get(lane - 1);
        for (int i = max(block - startBlock, 0); i <= block - startBlock + speed; i++) {
            if (laneList[i] == null || laneList[i].terrain == Terrain.FINISH) {
                break;
            }

            blocks.add(laneList[i].terrain);

        }
        return blocks;
    }
}

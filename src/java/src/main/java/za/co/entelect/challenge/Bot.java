package za.co.entelect.challenge;

import za.co.entelect.challenge.command.*;
import za.co.entelect.challenge.entities.*;
import za.co.entelect.challenge.enums.PowerUps;
import za.co.entelect.challenge.enums.Terrain;

import java.util.*;

import static java.lang.Math.max;

public class Bot {

    private static final int maxSpeed = 9;
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
    private static Command TWEET;

    private final static Command TURN_RIGHT = new ChangeLaneCommand(1);
    private final static Command TURN_LEFT = new ChangeLaneCommand(-1);

    public Bot(Random random, GameState gameState) {
        this.random = random;
        this.gameState = gameState;
        this.myCar = gameState.player;
        this.opponent = gameState.opponent;
    }

    public Command run() {
        List<Object> blocks = getBlocksInFront(myCar.position.lane, myCar.position.block, getSpeedAfterAccelerate(myCar));
        List<Object> leftBlocks;
        List<Object> rightBlocks;
        int countLeft, countRight;
        int countFront = getDamageInFront(myCar.position.lane , myCar.position.block, getSpeedAfterAccelerate(myCar));

        /* Prioritas 1 & Strategi Save The Car */
        if (((myCar.damage == 2 && myCar.speed == 8) ||
                (myCar.damage == 3 && myCar.speed == 6) ||
                (myCar.damage == 4 && myCar.speed == 3) ||
                (myCar.damage >= 5)) && (!hasPowerUp(PowerUps.BOOST, myCar.powerups))) {
            return FIX;
        } else if (myCar.damage > 0 && hasPowerUp(PowerUps.BOOST, myCar.powerups)) {
            return FIX;
        }

        /* Prioritas 2 & Strategi Avoid */
        if (myCar.speed != 0 && isBlocks(blocks)) {
            if (hasPowerUp(PowerUps.LIZARD, myCar.powerups)) {
                if (myCar.position.lane == 1) {
                    rightBlocks = getBlocksInFront(myCar.position.lane + 1, myCar.position.block, myCar.speed - 1);
                    if (isBlocks(rightBlocks)) {
                        return LIZARD;
                    } else {
                        return TURN_RIGHT;
                    }
                } else if (myCar.position.lane == 4) {
                    leftBlocks = getBlocksInFront(myCar.position.lane - 1, myCar.position.block, myCar.speed - 1);
                    if (isBlocks(leftBlocks)) {
                        return LIZARD;
                    } else {
                        return TURN_LEFT;
                    }
                } else {
                    leftBlocks = getBlocksInFront(myCar.position.lane - 1, myCar.position.block, myCar.speed - 1);
                    rightBlocks = getBlocksInFront(myCar.position.lane + 1, myCar.position.block, myCar.speed - 1);
                    if (isBlocks(leftBlocks)) {
                        if (isBlocks(rightBlocks)) {
                            return LIZARD;
                        } else {
                            return TURN_RIGHT;
                        }
                    } else {
                        if (isBlocks(rightBlocks)) {
                            return TURN_LEFT;
                        }
                    }
                }
            } else {
                if (myCar.position.lane == 1) {
                    rightBlocks = getBlocksInFront(myCar.position.lane + 1, myCar.position.block, myCar.speed - 1);
                    if (!isBlocks(rightBlocks)) {
                        return TURN_RIGHT;
                    } else {
                        countRight =  getDamageInFront(myCar.position.lane + 1, myCar.position.block, myCar.speed - 1);
                        if (countRight < countFront) {
                            return TURN_RIGHT;
                        }
                    }
                } else if (myCar.position.lane == 4) {
                    leftBlocks = getBlocksInFront(myCar.position.lane - 1, myCar.position.block, myCar.speed - 1);
                    if (!isBlocks(leftBlocks)) {
                        return TURN_LEFT;
                    } else {
                        countLeft =  getDamageInFront(myCar.position.lane - 1, myCar.position.block, myCar.speed - 1);
                        if (countLeft < countFront) {
                            return TURN_LEFT;
                        }
                    }
                } else {
                    leftBlocks = getBlocksInFront(myCar.position.lane - 1, myCar.position.block, myCar.speed - 1);
                    rightBlocks = getBlocksInFront(myCar.position.lane + 1, myCar.position.block, myCar.speed - 1);
                    if (isBlocks(leftBlocks)) {
                        if (!isBlocks(rightBlocks)) {
                            return TURN_RIGHT;
                        } else {
                            countRight =  getDamageInFront(myCar.position.lane + 1, myCar.position.block, myCar.speed - 1);
                            countLeft =  getDamageInFront(myCar.position.lane - 1, myCar.position.block, myCar.speed - 1);
                            if (countLeft <= countRight && countLeft < countFront) {
                                return TURN_LEFT;
                            } else if (countRight < countLeft && countRight < countFront) {
                                return TURN_RIGHT;
                            }
                        }
                    } else {
                        if(isBlocks(rightBlocks)) {
                            return TURN_LEFT;
                        }
                    }
                }
            }
        }

        /* Prioritas 3 & Strategi Turbo */
        if (myCar.damage == 0 && hasPowerUp(PowerUps.BOOST, myCar.powerups) && myCar.speed != 15 && !isBlocks(blocks)) {
            return BOOST;
        }

        /* Prioritas 4 & Strategi Max Speed */
        if (((myCar.speed < 9 && myCar.damage < 2) || (myCar.speed < 8 && myCar.damage == 2) ||
                (myCar.speed < 6 && myCar.damage == 3) || (myCar.speed < 3 && myCar.damage == 4)) &&
                !isBlocks(blocks)) {
            return ACCELERATE;
        }

        /* Prioritas 5 & Strategi Get Power Up */
        if (myCar.position.lane == 1) {
            rightBlocks = getBlocksInFront(myCar.position.lane + 1, myCar.position.block, myCar.speed - 1);
            if (!isPowerUp(blocks)) {
                if (isPowerUp(rightBlocks) && !isBlocks(rightBlocks)) {
                    return TURN_RIGHT;
                }
            }
        } else if (myCar.position.lane == 4) {
            leftBlocks = getBlocksInFront(myCar.position.lane - 1, myCar.position.block, myCar.speed - 1);
            if (!isPowerUp(blocks)) {
                if (isPowerUp(leftBlocks) && !isBlocks(leftBlocks)) {
                    return TURN_LEFT;
                }
            }
        } else {
            leftBlocks = getBlocksInFront(myCar.position.lane - 1, myCar.position.block, myCar.speed - 1);
            rightBlocks = getBlocksInFront(myCar.position.lane + 1, myCar.position.block, myCar.speed - 1);
            if (isBlocks(blocks)) {
                if (!isPowerUp(rightBlocks)) {
                    if (isPowerUp(leftBlocks)) {
                        return TURN_LEFT;
                    } else {
                        if (myCar.position.lane == 2) {
                            return TURN_RIGHT;
                        } else {
                            return TURN_LEFT;
                        }
                    }
                } else {
                    if (isPowerUp(leftBlocks)) {
                        if (myCar.position.lane == 2) {
                            return TURN_RIGHT;
                        } else {
                            return TURN_LEFT;
                        }
                    } else {
                        return TURN_RIGHT;
                    }
                }
            } else {
                if (!isPowerUp(blocks)) {
                    if (!isPowerUp(rightBlocks)) {
                        if (isPowerUp(leftBlocks) && !isBlocks(leftBlocks)) {
                            return TURN_LEFT;
                        }
                    } else {
                        if (!isBlocks(rightBlocks)) {
                            return TURN_RIGHT;
                        }
                    }
                }
            }
        }

        /* Prioritas 6 */

        /* Strategi USE_TWEET, USE_OIL, USE_EMP */
        if (hasPowerUp(PowerUps.TWEET, myCar.powerups)) {
            TWEET = new TweetCommand(opponent.position.lane, opponent.position.block + getSpeedAfterAccelerate(opponent) + 1);
            return TWEET;
        }

        if (myCar.position.block > opponent.position.block + opponent.speed) {
            if (hasPowerUp(PowerUps.OIL, myCar.powerups)) {
                return OIL;
            }
        } else if (myCar.position.block < opponent.position.block){
            if (myCar.position.lane == opponent.position.lane && hasPowerUp(PowerUps.EMP, myCar.powerups)) {
                return EMP;
            }
        }

        return ACCELERATE;
    }

    /* Mengembalikan true jika mobil memiliki powerUps powerUpToCheck */
    private Boolean hasPowerUp(PowerUps powerUpToCheck, PowerUps[] available) {
        if (available == null) {
            return false;
        }
        for (PowerUps powerUp: available) {
            if (powerUp.equals(powerUpToCheck)) {
                return true;
            }
        }
        return false;
    }

    /* Mengembalikan kecepatan mobil setelah accelerate */
    private int getSpeedAfterAccelerate(Car car) {
        if (car.speed == 3 || car.speed == 5) {
            return 6;
        } else if (car.speed == 6) {
            return 8;
        } else if (car.speed == 8 || (car.speed == 9 && !hasPowerUp(PowerUps.BOOST, car.powerups))) {
            return 9;
        } else if ((car.speed == 9 && hasPowerUp(PowerUps.BOOST, car.powerups)) || car.speed == 15) {
            return 15;
        } else {
            return 3;
        }
    }

    /* Mengembalikan jumlah damage dari jalur di depan mobil */
    private int getDamageInFront(int lane, int block, int speed) {
        List<Lane[]> map = gameState.lanes;
        int sumDamage = 0;
        int startBlock = map.get(0)[0].position.block;

        Lane[] laneList = map.get(lane - 1);
        for (int i = max(block - startBlock, 0); i <= block - startBlock + speed; i++) {
            if (laneList[i] == null || laneList[i].terrain == Terrain.FINISH) {
                break;
            }
            if (laneList[i].terrain == Terrain.MUD || laneList[i].terrain == Terrain.OIL_SPILL) {
                sumDamage += 1;
            } else if (laneList[i].terrain == Terrain.WALL || laneList[i].isOccupiedByCyberTruck) {
                sumDamage += 2;
            }
        }
        return sumDamage;
    }

    /* Mengembalikan block di depan mobil pada kecepatan speed */
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
            if (laneList[i].isOccupiedByCyberTruck) {
                blocks.add("TRUCK");
            }
            if (laneList[i].occupiedByPlayerId == opponent.id) {
                blocks.add("OPPONENT");
            }
        }
        return blocks;
    }

    /* Fungsi yang mengembalikan true jika block berisi obstacle */
    private boolean isBlocks(List<Object> block) {
        return (block.contains(Terrain.MUD) || block.contains(Terrain.WALL) ||
                block.contains(Terrain.OIL_SPILL) || block.contains("TRUCK") ||
                block.contains("OPPONENT"));
    }

    /* Fungsi yang mengembalikan true jika block berisi powerup */
    private boolean isPowerUp(List<Object> block) {
        return (block.contains(Terrain.BOOST) || block.contains(Terrain.LIZARD));
    }
}

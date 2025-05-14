package com.lllllllhp.utils.aiSolver;

import com.lllllllhp.model.game.*;

import java.util.*;

//创建新gameModel，用于解决
//解决时原game不得移动
public class AISolver extends GameModel {
    //用于减少ai算法占用内存
    private final Map<Integer, BoxData> originBoxDataMap = new HashMap<>();
    int mainKey;//曹操
    public static int tryNum;

    public AISolver(MapModel mapModel, Map<String, Integer> keyMap) {
        super(mapModel);
        initGameData();
        //导入目前box的key
        if (keyMap != null && !keyMap.isEmpty()) loadKeyMap(keyMap);

        for (Map.Entry<Integer, BoxModel> boxModelEntry : getBoxes().entrySet()) {
            int key = boxModelEntry.getKey();
            BoxModel box = boxModelEntry.getValue();
            if (getMainBox() == box) {
                mainKey = key;
            }
            originBoxDataMap.put(key, new BoxData(box));
        }
    }

    public AISolver(MapModel mapModel) {
        this(mapModel, null);
    }

    //bfs算法，返回可用于原game的record
    //无解则返回null
    public Deque<MovementRecord> bfsSolver() {
        long startTime = System.currentTimeMillis();
        tryNum = 0;
        //
        Deque<GameState> deque = new ArrayDeque<>();
        Set<String> isVisited = new HashSet<>();
        //初始状态
        GameState firstState = new GameState(this);
        deque.add(firstState);
        isVisited.add(firstState.encode());

        while (!deque.isEmpty()) {
            GameState curState = deque.pollFirst();

            //成功
            BoxData mainBox = curState.getBoxDataMap().get(mainKey);
            if (mainBox.getCol() == getMapModel().getTargetCol()
                    && mainBox.getRow() == getMapModel().getTargetRow()) {
                System.out.println("visit map: " + isVisited.size());
                System.out.println("TOTAL STEP: " + curState.getMovementRecords().size());
                long endTime = System.currentTimeMillis();
                System.out.println("Time cost(ms: " + (endTime - startTime));
                System.out.println("try numbers: " + tryNum);

                if (curState.getMovementRecords() != null) {
                    System.out.println("find solution!");
                } else {
                    System.out.println("no solution/error");
                }

                return curState.getMovementRecords();
            }

            //拓展
            List<GameState> nextState = curState.generateNextState();

            //加入deque，visited
            for (GameState gameState : nextState) {
                String stateKey = gameState.encode();
                if (!isVisited.contains(stateKey)) {
                    deque.offer(gameState);
                    isVisited.add(stateKey);
                }
            }
        }
        System.out.println("no solution");
        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);
        return null;
    }

    public Map<Integer, BoxData> getOriginBoxDataMap() {
        return originBoxDataMap;
    }

}

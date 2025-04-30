package com.proj510.utils.aiSolver;

import com.proj510.model.game.*;

import java.util.*;

//创建新gameModel，用于解决
//解决时原game不得移动
public class AISolver extends GameModel {
    //用于减少ai算法占用内存
    private final Map<Integer, BoxData> originBoxDataMap = new HashMap<>();
    int mainKey;//曹操
    public static int tryNum;

    public AISolver(MapModel mapModel) {
        super(mapModel);
        this.setGameController(new GameControllerModel(this));
        initGameData();
        for (Map.Entry<Integer, BoxModel> boxModelEntry : getBoxes().entrySet()) {
            int key = boxModelEntry.getKey();
            BoxModel box = boxModelEntry.getValue();
            if (getMainBox() == box) {
                mainKey = key;
            }
            originBoxDataMap.put(key, new BoxData(box));
        }
    }

    //bfs算法，返回可用于原game的record
    //无解则返回null
    public Deque<MovementRecord> bfsSolver() {
        long startTime = System.currentTimeMillis();
        //
        Deque<GameState> deque = new ArrayDeque<>();
        Set<Integer> isVisited = new HashSet<>();
        //初始状态
        GameState firstState = new GameState(this);
        deque.add(firstState);
        isVisited.add(firstState.encode().hashCode());

        while (!deque.isEmpty()) {
            GameState curState = deque.poll();

            //成功
            BoxData mainBox = curState.getBoxDataMap().get(mainKey);
            if (mainBox.getCol() == getMapModel().getTargetCol()
                    && mainBox.getRow() == getMapModel().getTargetRow()) {
                System.out.println("已访问状态数：" + isVisited.size());
                System.out.println("剩余待搜索状态数：" + deque.size());
                System.out.println("总步数：" + curState.getMovementRecords().size());
                long endTime = System.currentTimeMillis();
                System.out.println("用时(ms)：" + (endTime - startTime));
                return curState.getMovementRecords();
            }

            //拓展
            List<GameState> nextState = curState.generateNextState();

            //加入deque，visited
            for (GameState gameState : nextState) {
                Integer stateKey = gameState.encode().hashCode();
                if (!isVisited.contains(stateKey)) {
                    deque.offer(gameState);
                    isVisited.add(stateKey);
                }
            }
        }
        System.out.println("无解");
        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);
        return null;
    }

    public Map<Integer, BoxData> getOriginBoxDataMap() {
        return originBoxDataMap;
    }

}

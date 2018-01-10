package com.tjgs.rat.input.intent;

import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.Queue;

/**
 * Created by Tyler Johnson on 4/24/2017.
 *
 */
public class IntQueueMap<T> {

    private IntMap<Queue<T>> map;

    public IntQueueMap(){
        map = new IntMap<Queue<T>>();
    }

    public void clearValues(){
        for(Queue queue: map.values()){
            queue.clear();
        }
    }

    public void clearMap(){
        map.clear();
    }

    public void add(int queueId, T item){
        if(!map.containsKey(queueId)){
            map.put(queueId, new Queue<T>());
        }
        map.get(queueId).addLast(item);
    }

    public Queue<T> getQueue(int queueId){
        if(!map.containsKey(queueId)){
            return null;
        }
        return map.get(queueId);
    }

    public boolean containsKey(int queueId){
        return map.containsKey(queueId);
    }

    public void remove(int queueId, T item){
        Queue<T> queue = getQueue(queueId);
        queue.removeValue(item, true);//remove based on reference
    }

}

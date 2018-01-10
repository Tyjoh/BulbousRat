package com.tjgs.rat.event;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Queue;
import com.tjgs.rat.input.intent.IntQueueMap;

/**
 * Created by Tyler Johnson on 4/25/2017.
 *
 * This class is used to store all of the events that are used across the game.
 * It is also intended to be used by injection systems via artemis injection.
 *
 */
public class EventBlackboard {

    private static final String TAG = EventBlackboard.class.getSimpleName();

    //this object stores all game related events
    private ObjectMap<Class<? extends Event>, IntQueueMap> events;

    public EventBlackboard(){
        events = new ObjectMap<Class<? extends Event>, IntQueueMap>();
    }

    /**
     * Finds the events of eventType associated with the entityId
     * @param eventType type of event
     * @param entityId Id of the entity
     * @param <T> class type of the event
     * @return A queue of events ordered oldest first, newest last.
     */
    public <T extends Event> Queue<T> queryEntityEvents(Class<T> eventType, int entityId){
        //if the event type exists
        if(events.containsKey(eventType)){
            IntQueueMap<T> entityEventMap = events.get(eventType);

            //returns the event queue associated with the entity. If the entity has never had an event null is returned
            return entityEventMap.getQueue(entityId);
        }
        Gdx.app.log(TAG, "Event type \"" + eventType.getSimpleName() + "\" doesn't exist in the event blackboard");
        return null;//event doesn't exist so return null;
    }

    public <T extends Event> boolean entityHasEvent(Class<T> eventType, int entityId){
        if(events.containsKey(eventType)){
            return events.get(eventType).containsKey(entityId);
        }
        return false;
    }

    /**
     * Adds an event to an entities event queue
     * @param event event to add
     * @param entityId id of the entity
     */
    public <T extends Event> void addEvent(T event, int entityId){
        //System.out.println("Adding event of type " + event.getClass().getSimpleName());

        if(!events.containsKey(event.getClass())){
            events.put(event.getClass(), new IntQueueMap<T>());
        }

        IntQueueMap<Event> map = events.get(event.getClass());
        map.add(entityId, event);
    }

    /**
     * This function should be used when an event should be consumed and no further event processing should be done.
     * @param event event to remove
     * @param entityId id of the entity
     */
    public void removeEvent(Event event, int entityId){
        Queue queue = this.queryEntityEvents(event.getClass(), entityId);
        queue.removeValue(event, true);
    }

    /**
     * Clears all types of events related to the entity.
     * @param entityId entity to remove events for
     */
    public void clearEntityEvents(int entityId){
        for(IntQueueMap queueMap: events.values()){
            queueMap.getQueue(entityId).clear();
        }
    }

    /**
     * Clears all events for a certain type of event.
     * NOTE: both event types and entity event queues remain in their maps.
     */
    public <T extends Event> void clearEventType(Class<T> tClass){
        if(events.containsKey(tClass)) {
            events.get(tClass).clearValues();
        }
    }

    /**
     * Performs a hard reset of the blackboard. All event types and entity event queues are completely removed.
     */
    public void clearAllEvents(){
        events.clear();
    }

}

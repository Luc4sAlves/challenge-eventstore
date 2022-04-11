import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.naming.event.EventContext;

import net.intelie.challenges.Event;
import net.intelie.challenges.EventIterator;
import net.intelie.challenges.EventStore;

public class Store implements EventStore, EventIterator {

    private ArrayList<Event> EventCollection = new ArrayList<Event>();

    private Event currentEvent;


    @Override
    public void close(){
        this.EventCollection.clear();
        this.currentEvent = null;
    }

    @Override
    public boolean moveNext() {
        try {
            int currentIndex = EventCollection.indexOf(currentEvent);
            this.currentEvent = EventCollection.get(currentIndex + 1);
            return true;
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Chegou no final");
            return false;
        }
    }

    @Override
    public Event current() {
        return this.currentEvent;
    }

    @Override
    public void remove() {
        Event tempEvent = this.currentEvent;
        moveNext();
        this.EventCollection.remove(tempEvent);        
    }

    @Override
    public void insert(Event event) {
        this.EventCollection.add(event);
        if(this.currentEvent == null){
            this.currentEvent = event;
        }
    }

    @Override
    public void removeAll(String type) {

        this.EventCollection.clear();
        this.currentEvent = null;
        
    }

    @Override
    public EventIterator query(String type, long startTime, long endTime) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
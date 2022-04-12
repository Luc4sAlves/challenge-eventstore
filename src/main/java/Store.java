import java.util.Vector;

import net.intelie.challenges.Event;
import net.intelie.challenges.EventIterator;
import net.intelie.challenges.EventStore;

public class Store implements EventStore, EventIterator {

    //Vectors are thread-safe as they're sincronized
    //(https://docs.oracle.com/javase/8/docs/api/java/util/Vector.html)
    private Vector<Event> EventCollection = new Vector<Event>();

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
    public Event current() throws IllegalStateException {
        if(this.currentEvent == null){
            throw new IllegalStateException();
        }
        return this.currentEvent;
    }

    @Override
    public void remove() throws IllegalStateException {
        if(this.currentEvent == null){
            throw new IllegalStateException();
        }
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

        this.EventCollection.removeIf(event -> event.type().equals(type));

        //I Don't know what should happen with current event so 
        //the safe assumption is just setting it to null
        if(this.currentEvent.type().equals(type)){
            this.currentEvent = null;
        }
        
        
    }

    @Override
    public EventIterator query(String type, long startTime, long endTime) {
        // TODO Auto-generated method stub
        return null;
    }
    
    public void showAllEvents(){
        for(Event event: this.EventCollection){
            System.out.println(event.type());
            System.out.println(event.timestamp());
        }
    }

    public Event currentEvent(){
        return this.currentEvent;
    }

}

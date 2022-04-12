package net.intelie.challenges;

import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

public class Store implements EventStore, EventIterator {

    //Vectors are thread-safe as they're sincronized
    //(https://docs.oracle.com/javase/8/docs/api/java/util/Vector.html)
    private List<Event> eventCollection = new Vector<Event>();

    private Event currentEvent;

    private boolean hasCurrent = false;

    public Store(List<Event> eventCollection){
        this.eventCollection = eventCollection;
    }

    public Store(){

    }

    @Override
    public void close(){
        this.eventCollection.clear();
        this.currentEvent = null;
    }

    @Override
    public boolean moveNext() {
        try {
            int currentIndex = eventCollection.indexOf(currentEvent);
            this.currentEvent = eventCollection.get(currentIndex + 1);
            hasCurrent = true;
            return true;
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Chegou no final");
            hasCurrent = false;
            return false;
        }
    }

    @Override
    public Event current() throws IllegalStateException {
        if(!hasCurrent){
            throw new IllegalStateException();
        }
        return this.currentEvent;
    }

    @Override
    public void remove() throws IllegalStateException {
        if(!hasCurrent){
            throw new IllegalStateException();
        }
        //Since we're removing the current from list I think this is a good measure
        hasCurrent = false;
        this.eventCollection.remove(this.currentEvent);        
    }

    @Override
    public void insert(Event event) {
        this.eventCollection.add(event);
    }

    @Override
    public void removeAll(String type) {

        this.eventCollection.removeIf(event -> event.type().equals(type));

        //I Don't know what should happen with current event so 
        //the safe assumption is just setting it to null
        if(hasCurrent && this.currentEvent.type().equals(type)){
            this.currentEvent = null;
        }

    }

    @Override
    public EventIterator query(String type, long startTime, long endTime) {

        //This operation isn't done by Vector but it also doesn't modify the collection 
        //so it's not a problem for thread-safety
        //I used Collectors (from java.util) to make the filter operation as simple as possible
        List<Event> queryEventList = this.eventCollection.stream().filter(event -> (event.type().equals(type) && event.timestamp() >= startTime && event.timestamp() < endTime)).collect(Collectors.toList());

        //Returning a Store as it implements EventIterator
        return new Store(queryEventList);
    }
    
    public void showAllEvents(){
        for(Event event: this.eventCollection){
            System.out.println(event.type());
            System.out.println(event.timestamp());
        }
    }

    public int size(){
        return this.eventCollection.size();
    }

}

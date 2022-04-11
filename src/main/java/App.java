import net.intelie.challenges.Event;

public class App {
    public static void main(String[] args) {
        System.out.println("Hello El");
        Event water = new Event("type", 13);
        Event dog = new Event("type", 131);
        Event music = new Event("type", 13);

        Store eventStore = new Store();
        eventStore.insert(water);
        eventStore.insert(dog);
        System.out.println(eventStore.currentEvent);
        eventStore.insert(music);
        eventStore.moveNext();
        eventStore.moveNext();
        System.out.println(eventStore.currentEvent);
        eventStore.moveNext();
        eventStore.close();
    }
}

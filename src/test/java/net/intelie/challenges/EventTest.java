package net.intelie.challenges;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class EventTest {

    @Test
    public void thisIsAWarning() throws Exception {
        Event event = new Event("some_type", 123L);

        //THIS IS A WARNING:
        //Some of us (not everyone) are coverage freaks.
        assertEquals(123L, event.timestamp());
        assertEquals("some_type", event.type());
    }

    @Test
    public void testSimpleInsertion(){
        Store store = new Store();
        store.insert(new Event("type", 1));
        assertEquals(1 , store.size());
        store.close();
    }

    @Test
    public void testNInsertion(){
        Store store = new Store();
        int n;

        for(n = 0; n < 10; n++){
            store.insert(new Event("type", n));
        }
        
        assertEquals(n, store.size());
        store.close();
    }

    @Test
    public void testSimpleMoveNext(){
        Store store = new Store();
        Event dummy = new Event("type", 1);
        store.insert(dummy);
        store.moveNext();
        assertTrue(store.current().equalTypeTimestamp(dummy));
        store.close();
    }

    @Test
    public void testNMoveNext(){
        Store store = new Store();
        int n;

        for(n = 0; n < 10; n++){
            store.insert(new Event("type", n));
        }
        for(n = 0; n < 10; n++){
            store.moveNext();
        }

        assertTrue(store.current().equalTypeTimestamp(new Event("type", n-1)));

        store.close();
    }

    @Test
    public void testRemove(){
        Store store = new Store();

        store.insert(new Event("type", 1));
        store.moveNext();
        store.insert(new Event("type", 2));
        store.remove();
        assertEquals(1, store.size());
        store.close();

    }

    @Test 
    public void testCurrentAfterRemove(){
        Store store = new Store();

        store.insert(new Event("type", 1));
        store.moveNext();
        store.insert(new Event("type", 2));

        store.remove();
        assertThrows(IllegalStateException.class, 
            () -> {
                store.current();
            }
        );
        store.close();
    }

    @Test 
    public void testRemoveAll(){
        Store store = new Store();
        store.insert(new Event("type", 1));
        store.insert(new Event("type", 2));
        store.insert(new Event("type", 3));

        store.insert(new Event("another", 1));

        store.removeAll("type");
        assertEquals(1, store.size());
        store.close();
    }

    @Test 
    public void testRemoveAllWrongType(){
        Store store = new Store();
        store.insert(new Event("type", 1));
        store.insert(new Event("type", 2));
        store.insert(new Event("type", 3));

        store.insert(new Event("another", 1));

        store.removeAll("typo");
        assertEquals(4, store.size());
        store.close();
    }

    @Test
    public void testQuery(){
        Store store = new Store();
        store.insert(new Event("type", 10));
        store.insert(new Event("type", 20));
        store.insert(new Event("type", 14));
        store.insert(new Event("another", 13));
        store.insert(new Event("type", 11));

        Store queryStore = (Store) store.query("type", 10, 14);
        assertEquals(2, queryStore.size());
        store.close();
    }

    @Test
    public void testQueryZeroResults(){
        Store store = new Store();
        store.insert(new Event("type", 1));
        store.insert(new Event("type", 2));
        store.insert(new Event("type", 3));
        store.insert(new Event("another", 4));

        Store queryStore = (Store) store.query("type", 4, 7);
        assertEquals(0, queryStore.size());
        store.close();
    }

}
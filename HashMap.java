/** HashMap 
 * Use separate chaining to resolve collisions.
 */


import java.util.LinkedList;
import java.util.Random;

public class HashMap<Key, Value> {
    private static final int INIT_CAPACITY = 4;
    private int n; // number of elements
    private int m; // hash table size
    private LinkedListForHash<Key, Value>[] st;
    // array of linked-list.

    public HashMap() {
        this(INIT_CAPACITY);
    }

    // used in resize.
    public HashMap(int m) {
        this.m = m;
        st = (LinkedListForHash<Key, Value>[]) new LinkedListForHash[m];
        for (int i = 0; i < m; i++)
            st[i] = new LinkedListForHash<Key, Value>();
    }

    // resize the hash table to have the given number of chains,
    // rehashing all of the keys
    private void resize(int n) {

        HashMap<Key, Value> temp = new HashMap<Key, Value>(n);

        for (int i = 0; i < m; i++) {
            for (Key key : st[i].keys()) {
                temp.put(key, st[i].get(key));
            }
        }
        this.m = temp.m;
        this.n = temp.n;
        this.st = temp.st;
    }

    // hash value between 0 and m-1
    private int myhash(Key key) {
        int hash = 7;
        String k = (String) key; //here we assume keys are strings
        int base=31;

        for (int i=0; i<k.length(); i++) {
            //hash = k.charAt(i).hashCode();
            hash = (hash << 5) | (hash >>> 27); // 5-bit cyclic shift of the running sum
            hash += (int)k.charAt(i); // add in next character
        }


        hash=Math.abs(hash) % m;
        return hash;
    }

    public Value get(Key key) {
        int i = myhash(key);
        return st[i].get(key);
    }

    public void put(Key key, Value val) {
        // double table size if load factor m/n >0.1
        if (n >= 10 * m)
            resize(2 * m);
       
        int i = myhash(key);
        if (st[i].get(key)==null){
            n++;
        }
        st[i].put(key , val);
        

    }

    public void delete(Key key) {
        if (key == null)
            throw new IllegalArgumentException("argument to delete() is null");
        int i = myhash(key);
        if (st[i].get(key)!=null)
            n--;
        st[i].delete(key);

        // halve table size if average length of list <= 2
        if (m > INIT_CAPACITY && n <= 2 * m)
            resize(m / 2);
    }

    public LinkedList<Key> keys() {
        LinkedList<Key> queue = new LinkedList<Key>();
        for (int i = 0; i < m; i++) {
            for (Key key : st[i].keys())
                queue.add(key);
        }
        return queue;
    }
}

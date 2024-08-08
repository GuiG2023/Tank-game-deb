package tankrotationexample;

import tankrotationexample.game.Poolable;
import tankrotationexample.game.ResourcePool;

import java.util.HashMap;
import java.util.Map;

/**
 * 7/28/24 @ 21:33
 *
 * @ Author : Guiran LIU
 * Description:
 */
public class ResourcePools {
    private static final Map<String, ResourcePool<? extends Poolable>> pools = new HashMap<>();

    public static void addPool(String key, ResourcePool<? extends Poolable> pool) {
        ResourcePools.pools.put(key, pool);
    }

    public static Poolable getPoolInstance(String key) {
        return ResourcePools.pools.get(key).removeFormPool();
    }

//    public static void returnPoolInstance(String key,Poolable pool){
//        return ResourcePools.pools.get(key).addToPool(pool);
//
//    }

}

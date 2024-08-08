package tankrotationexample.game;

import tankrotationexample.ResourceManager;

import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * 7/28/24 @ 19:34
 *
 * @ Author : Guiran LIU
 * Description:
 */
public class ResourcePool<G extends Poolable> {
    private final static int INIT_CAPACITY = 100;
    private final String resourceName;
    private final Class<G> resourceClass;
    private final ArrayList<G> resources;

    public ResourcePool(String resourceName, Class<G> resourceClass, int initCapacity) {
        this.resourceName = resourceName;
        this.resourceClass = resourceClass;
        this.resources = new ArrayList<>(initCapacity);

    }

    public G removeFormPool() {
        if (this.resources.isEmpty()) {
            this.refillPool();
        }
        return this.resources.removeLast();
    }

    public void addToPool(G obj) {
        this.resources.addLast(obj);
    }

    public ResourcePool<G> fillPool(int size) {
        BufferedImage img = ResourceManager.getSprites(this.resourceName);
        for (int i = 0; i < size; i++) {//reflection to create new instance
            try {
                var g = this.resourceClass.getDeclaredConstructor(BufferedImage.class).newInstance(img);
                this.resources.add(g);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }

        }
        return this;
    }

    private void refillPool() {
        this.fillPool(INIT_CAPACITY);
    }
}

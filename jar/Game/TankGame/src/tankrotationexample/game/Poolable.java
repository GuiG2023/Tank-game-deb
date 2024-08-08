package tankrotationexample.game;

/**
 * 7/28/24 @ 19:35
 *
 * @ Author : Guiran LIU
 * Description:
 */
public interface Poolable {
    void initObject(float x,float y);
    void initObject(float x,float y,float angle);
    void resetObject();

}

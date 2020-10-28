
/**
 * Created by Sergio on 19.01.2017.
 */
public interface ModelCallback<T>{

    public void onComplete(T t);

    public void onError(Exception e);
}

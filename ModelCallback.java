public interface ModelCallback<T>{

    public void onComplete(T t);

    public void onError(Exception e);
}

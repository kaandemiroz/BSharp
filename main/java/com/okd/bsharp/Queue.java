package com.okd.bsharp;

/**
 * Created by OKD on 24/05/16.
 */
public class Queue<T> {

    private int size;
    private T[] array;
    private int startIndex = 0;
    private int endIndex = 0;

    public Queue(int size){
        this.size = size;
        array = (T[]) new Object[size];
    }

    public T get(int index){
        return array[index];
    }

    public int getSize(){
        return size;
    }

    public void enqueue(T w){
        if(!isFull()){
            array[endIndex] = w;
            endIndex = (endIndex + 1) % size;
        }
    }

    public T dequeue(){
        if(!isEmpty()){
            T temp = array[startIndex];
            startIndex = (startIndex + 1) % size;
            return temp;
        }else return null;
    }

    private boolean isEmpty(){
        return array[startIndex] == null;
    }

    private boolean isFull(){
        return (endIndex - startIndex) == 0 && array[startIndex] != null;
    }

}


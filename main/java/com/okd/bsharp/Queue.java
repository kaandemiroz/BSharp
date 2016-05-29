package com.okd.bsharp;

/**
 * Created by OKD on 24/05/16.
 */
public class Queue {

    private int size;
    private int[] array;
    private int startIndex = 0;
    private int endIndex = 0;

    public Queue(int size){
        this.size = size;
        array = new int[size];
        for(int i=0; i<array.length; i++){
            array[i] = -1;
        }
    }

    public int get(int index){
        return array[index];
    }

    public int getSize(){
        return size;
    }

    public void addItem(int item){
        if(isFull()) dequeue();
        enqueue(item);
    }

    private void enqueue(int w){
        if(!isFull()){
            array[endIndex] = w;
            endIndex = (endIndex + 1) % size;
        }
    }

    private int dequeue(){
        if(!isEmpty()){
            int temp = array[startIndex];
            startIndex = (startIndex + 1) % size;
            return temp;
        }else return -1;
    }

    public int[] getArray(){
        int[] result = new int[size];
        for(int i=startIndex; i<size; i++){
            result[i-startIndex] = array[i];
        }
        for(int i=0; i<startIndex; i++){
            result[size - startIndex + i] = array[i];
        }
        return result;
    }

    private boolean isEmpty(){
        return array[startIndex] == -1;
    }

    private boolean isFull(){
        return (endIndex - startIndex) == 0 && array[startIndex] != -1;
    }

}


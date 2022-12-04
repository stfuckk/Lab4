package example.lab.model;

import example.lab.model.api.ModelAPI;
import example.lab.model.api.Updatable;

class T implements Runnable{
    private Thread t;
    private double max;
    private Updatable updater;

    private boolean canPause = false;
    private boolean canStop = false;

    public T(String name, double valueInit, Updatable updater){
        t = new Thread(this, name);
        this.max = valueInit;
        this.updater = updater;
        start();
    }

    public void start(){
        t.start();
    }
    public void stop(){
        canStop = true;
    }
    public void pause(){
        canPause = true;
    }
    public void resume(){
        synchronized (t){
            canPause = false;
            t.notify();
        }
    }
    public void interrupt(){
        t.interrupt();
    }

    public void run(){
        for(double i=0; i<max; i++){
            updater.update(i);
            synchronized (t){
                if(canPause)
                    try {t.wait();} catch (InterruptedException e){}
                if(canStop){
                    updater.update(0); break;
                }
            }
            System.out.println("i: " + i);
            try {Thread.sleep(20);} catch (InterruptedException e) {t.interrupt(); return;}
        }
    }
}

public class Model implements ModelAPI {

    private T thread;

    private double value;

    public Model(double vIn, Updatable updater){
        thread = new T("Parallel", vIn, updater);
    }
    @Override
    public double calcProgress(double inp, double max){
        double v = inp / max;
        return v;
    }
    @Override
    public void start(){
        thread.start();
    }
    @Override
    public void stop(){
        thread.stop();
    }
    @Override
    public void pause(){
        thread.pause();
    }
    @Override
    public void resume(){
        thread.resume();
    }
    @Override
    public void interrupt(){
        thread.interrupt();
    }
}

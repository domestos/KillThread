package es.hol.varenik.killthread;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class FragmentThread extends Fragment {
    MainActivity activity;
    private boolean isWork;
    private ThreadTask threadTask;
    private String TAG_LOG = "Thread_log";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void link(MainActivity activity) {
        this.activity = activity;
    }

    public void start() {
        threadTask = new ThreadTask();
        threadTask.execute();
    }

    public void stop() {
        if (threadTask != null) {
            threadTask.cancel(false);
        }
    }

    public boolean getIsWork() {
        return isWork;
    }

    // ============== into class extends AsyncTask =========
    class ThreadTask extends AsyncTask<Void, Void, Void> {
        private Thread myTread1 = factoryThreadOne();
        private Thread myTread2 = factoryThreadTwe();
        private Thread myTread3 = factoryThreadThree();

        @Override
        protected void onPreExecute() {
            Log.i(TAG_LOG, "onPreExecute");
            super.onPreExecute();
            isWork = true;
        }

        @Override
        protected Void doInBackground(Void... params) {
            startTread(myTread1);
            startTread(myTread2);
            startTread(myTread3);
            startAsyncTask();
            return null;
        }

        private synchronized void startTread(Thread myTread) {
            if(!myTread.isAlive()){
                myTread.start();
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.i(TAG_LOG, "onPostExecute");
            super.onPostExecute(aVoid);
            isWork = false;
            activity.showProgressBar(isWork);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.i(TAG_LOG, "onCancelled");
            isWork = false;
            activity.showProgressBar(isWork);

        }

        private void startAsyncTask() {
            for (int i = 0; i < 100; i++) {
                try {
                    if (isCancelled()) {
                        myTread1.interrupt();
                        myTread2.interrupt();
                       // myTread3.interrupt();
                        break;
                    }
                    Log.i(TAG_LOG, "AsynkTask is work " + i);
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * first method kill thread
         * in loop is use Thread.sleep what is throws InterruptException  when executed interrupt
         */
        private Thread factoryThreadOne() {
            Log.i(TAG_LOG, "doInBackground");
            Thread _thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Thread thread = Thread.currentThread();
                    for (int i = 0; i < 1000000000; i++) {
                        Log.i(TAG_LOG, "Thread is work " + i + " thread " + thread.hashCode() + " and is interrupted " + thread.isInterrupted());
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Log.i(TAG_LOG, "Tread is dead - interrupted " + thread.isInterrupted());
                            break;
                        }
                    }
                }
            });
            return _thread;
        }

        /**
         * second method kill thread
         * in loop is check interrupt if it is returns true -  loop is breaking and thread finishes work
         */
        private Thread factoryThreadTwe() {
            Log.i(TAG_LOG, "doInBackground");
            Thread _thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Thread thread = Thread.currentThread();
                    for (int i = 0; i < 1000000000; i++) {
                        if (thread.isInterrupted()) {
                            Log.i(TAG_LOG, "Tread is dead - interrupted " + thread.isInterrupted());
                            break; //exit
                        }
                        Log.i(TAG_LOG, "Thread is work " + i + " thread " + thread.hashCode() + " and is interrupted " + thread.isInterrupted());
                    }
                }
            });
            return _thread;
        }

        /**
         * second method kill thread
         * use  throw new InterruptedException();
         * use check isCancelled()
         */
        private Thread factoryThreadThree() {
            Thread _thread = new Thread(new Runnable() {
                @Override
                public  void run() {
                    Thread thread = Thread.currentThread();
                    try {
                        for (int i = 0; i < 100000; i++) {
                            // exit
                            if(isCancelled()){
                                Log.i(TAG_LOG, "Tread isCancelled " + isCancelled());
                              return;
                            }
                            //exit
                            if(i == 10000){
                                throw new InterruptedException();
                            }
                            Log.i(TAG_LOG, "Thread is work " + i + " thread " + thread.hashCode() + " and is interrupted " + thread.isInterrupted());
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Log.i(TAG_LOG, "Tread is dead - interrupted " + thread.isInterrupted());
                        return; //exit

                    }
                    //exit
                }
            });
            return _thread;
        }

    }


}

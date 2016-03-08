package es.hol.varenik.killthread;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnStart;
    private Button btnStop;
    private ProgressBar progressBar;
    private String TAG_FRAGMENT = "FRAGMENT_THREAD";
    private FragmentThread fragmentThread;
    private String TAG_LOG = "Thread_log";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewElements();
        fragmentThread = getFragmentThread();
        fragmentThread.link(this);
        showProgressBar(fragmentThread.getIsWork());
        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        Log.i(TAG_LOG, "onCreate hashCode: " + this.hashCode() + " hashCode Fragment: " + fragmentThread.hashCode());
    }

    public void showProgressBar(boolean isWork) {
        btnStart.setEnabled(!isWork);
        progressBar.setVisibility(isWork ? View.VISIBLE : View.GONE);
    }

    private void findViewElements() {
        btnStart = (Button) findViewById(R.id.btnStart);
        btnStop = (Button) findViewById(R.id.btnStop);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

    }

    public FragmentThread getFragmentThread() {
        fragmentThread = (FragmentThread) getFragmentManager().findFragmentByTag(TAG_FRAGMENT);
        if (fragmentThread == null) {
            fragmentThread = new FragmentThread();
            getFragmentManager().beginTransaction().add(fragmentThread, TAG_FRAGMENT).commit();
        }
        return fragmentThread;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStart:
                fragmentThread.start();
                showProgressBar(fragmentThread.getIsWork());
                break;
            case R.id.btnStop:
                fragmentThread.stop();
                showProgressBar(fragmentThread.getIsWork());
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isFinishing()){fragmentThread.stop();}
    }
}

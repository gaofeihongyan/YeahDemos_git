package org.yeah.fragmenttabhost;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.yeah.R;

/**
 * Created with IntelliJ IDEA.
 * Author: wangjie  email:wangjie@cyyun.com
 * Date: 13-6-14
 * Time: 下午2:39
 */
public class TabCFm extends Fragment{
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        android.util.Log.d("harry","CCCCCCCCCC____onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.util.Log.d("harry","CCCCCCCCCC____onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        android.util.Log.d("harry","CCCCCCCCCC____onCreateView");
        return inflater.inflate(R.layout.tab_c, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        android.util.Log.d("harry","CCCCCCCCCC____onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        android.util.Log.d("harry","CCCCCCCCCC____onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        android.util.Log.d("harry","CCCCCCCCCC____onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        android.util.Log.d("harry","CCCCCCCCCC____onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        android.util.Log.d("harry","CCCCCCCCCC____onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        android.util.Log.d("harry","CCCCCCCCCC____onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        android.util.Log.d("harry","CCCCCCCCCC____onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        android.util.Log.d("harry","CCCCCCCCCC____onDetach");
    }




}

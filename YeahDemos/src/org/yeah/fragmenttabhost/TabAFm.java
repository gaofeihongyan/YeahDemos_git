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
 * Author: wangjie  email:tiantian.china.2@gmail.com
 * Date: 13-6-14
 * Time: 下午2:39
 */
public class TabAFm extends Fragment{
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        android.util.Log.d("harry","AAAAAAAAAA____onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.util.Log.d("harry","AAAAAAAAAA____onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        android.util.Log.d("harry","AAAAAAAAAA____onCreateView");
        return inflater.inflate(R.layout.tab_a, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        android.util.Log.d("harry","AAAAAAAAAA____onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        android.util.Log.d("harry","AAAAAAAAAA____onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        android.util.Log.d("harry","AAAAAAAAAA____onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        android.util.Log.d("harry","AAAAAAAAAA____onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        android.util.Log.d("harry","AAAAAAAAAA____onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        android.util.Log.d("harry","AAAAAAAAAA____onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        android.util.Log.d("harry","AAAAAAAAAA____onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        android.util.Log.d("harry","AAAAAAAAAA____onDetach");
    }
}

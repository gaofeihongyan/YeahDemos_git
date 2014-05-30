package org.yeah.fragmenttabhost;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import org.yeah.R;

/**
 * Created with IntelliJ IDEA.
 * Author: wangjie  email:wangjie@cyyun.com
 * Date: 13-6-14
 * Time: 下午2:39
 */
public class TabBFm extends Fragment{
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        android.util.Log.d("harry","BBBBBBBBBBB____onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.util.Log.d("harry","BBBBBBBBBBB____onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        android.util.Log.d("harry","BBBBBBBBBBB____onCreateView");
        return inflater.inflate(R.layout.tab_b, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        android.util.Log.d("harry","BBBBBBBBBBB____onActivityCreated");
        this.getView().findViewById(R.id.clickme).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 获得绑定的FragmentActivity
                FragmentTabhostTestActivity activity = ((FragmentTabhostTestActivity)getActivity());
                // 获得TabAFm的控件
                EditText editText = (EditText) activity.fragments.get(0).getView().findViewById(R.id.edit);

                Toast.makeText(activity, activity.hello + editText.getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        android.util.Log.d("harry","BBBBBBBBBBB____onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        android.util.Log.d("harry","BBBBBBBBBBB____onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        android.util.Log.d("harry","BBBBBBBBBBB____onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        android.util.Log.d("harry","BBBBBBBBBBB____onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        android.util.Log.d("harry","BBBBBBBBBBB____onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        android.util.Log.d("harry","BBBBBBBBBBB____onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        android.util.Log.d("harry","BBBBBBBBBBB____onDetach");
    }




}

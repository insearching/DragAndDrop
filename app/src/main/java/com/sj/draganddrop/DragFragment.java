package com.sj.draganddrop;


import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

public class DragFragment extends Fragment {

    private static final String IMAGEVIEW_TAG = "The Android Logo";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        view.findViewById(R.id.topRl).setOnDragListener(new DragEventListener());
        view.findViewById(R.id.bottomRl).setOnDragListener(new DragEventListener());

        ImageView myIv = (ImageView) view.findViewById(R.id.iconIv);
        myIv.setTag(IMAGEVIEW_TAG);
        myIv.setOnTouchListener(new onImageTouchListener());
        return view;
    }

    ;

    private final class onImageTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                CharSequence tag = (CharSequence) v.getTag();
                ClipData.Item item = new ClipData.Item(tag);

                ClipData dragData = new ClipData(tag, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
                View.DragShadowBuilder shadowBuilder = new DragShadowBuilder(v);
                v.startDrag(dragData, shadowBuilder, v, 0);
                v.setVisibility(View.INVISIBLE);
            }
            return true;
        }
    }
    String TAG = "DragEventListener";
    class DragEventListener implements View.OnDragListener {

        Context context;
        View lastView;
        boolean isInside = true;

        public DragEventListener() {
            context = getActivity();
        }

        @Override
        public boolean onDrag(View v, DragEvent event) {
            final int action = event.getAction();

            switch (action) {

                case DragEvent.ACTION_DRAG_ENTERED:
                    Log.d(TAG, "ACTION_DRAG_ENTERED");
                    lastView = v;
                    isInside = true;
                    v.setBackgroundColor(getResources().getColor(R.color.darker_gray));
                    break;

                case DragEvent.ACTION_DRAG_EXITED:
                    Log.d(TAG, "ACTION_DRAG_EXITED");
                    isInside = false;
                    v.setBackgroundColor(getResources().getColor(R.color.gray));
                    break;


                case DragEvent.ACTION_DROP:
                    Log.d(TAG, "ACTION_DROP");
                    if (v == getActivity().findViewById(R.id.bottomRl) || v == getActivity().findViewById(R.id.topRl)) {
                        View view = (View) event.getLocalState();
                        ViewGroup viewgroup = (ViewGroup) view.getParent();
                        viewgroup.removeView(view);

                        ViewGroup containView = (ViewGroup) v;
                        containView.addView(view);
                        view.setVisibility(View.VISIBLE);
                    }
                    break;


                case DragEvent.ACTION_DRAG_ENDED:
                    Log.d(TAG, "ACTION_DRAG_ENDED");
                    if (event.getResult()) {
                        Toast.makeText(context, "The drop was handled.", Toast.LENGTH_LONG);
                    } else {
                        Toast.makeText(context, "The drop didn't work.", Toast.LENGTH_LONG);
                    }

                    break;

            }
            return true;
        }
    }

}

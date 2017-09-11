package com.example.shenawynkov.jopfinder;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Shenawynkov on 9/11/2017.
 */

public class MyWidgetRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new MyWidgetRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
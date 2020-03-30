package com.update.apt_core;

import android.app.Activity;

import java.lang.reflect.Constructor;

/**
 * Created on 2020/03/30
 * Description:
 *
 * @author liupu
 */
public class ButterKnife {

    public static void bind(Activity activity) {
        try {
            Class bindClazz =
                    Class.forName(activity.getClass().getName() + "_ViewBinding");
            Constructor bindConstructor = bindClazz.getDeclaredConstructor(activity.getClass());
            bindConstructor.newInstance(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


package com.liule.photoalbum.ui.widget.photoview.scrollerproxy;

import android.content.Context;

/**
 * @author 刺雒
 * @time 2017/10/29 
 */
public class IcsScroller extends GingerScroller {

    public IcsScroller(Context context) {
        super(context);
    }

    @Override
    public boolean computeScrollOffset() {
        return mScroller.computeScrollOffset();
    }

}

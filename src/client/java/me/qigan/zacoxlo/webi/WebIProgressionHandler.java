package me.qigan.zacoxlo.webi;

import me.friwi.jcefmaven.EnumProgress;
import me.friwi.jcefmaven.IProgressHandler;
import me.qigan.zacoxlo.pre.PreLoad;

public class WebIProgressionHandler implements IProgressHandler {
    @Override
    public void handleProgress(EnumProgress state, float percent) {
        PreLoad.prog_hndl.WebICEF_prog_bar.setText("WebI | JCEF load: ("+state.name()+")    "+(percent==-1 ? "" : percent+"%"));
    }
}

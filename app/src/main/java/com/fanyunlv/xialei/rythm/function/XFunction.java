package com.fanyunlv.xialei.rythm.function;

/**
 * Created by xialei on 2018/11/24.
 */
public class XFunction {
    private String name;
    private boolean isenabled;

    public XFunction(String name, boolean isenabled) {
        this.name = name;
        this.isenabled = isenabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isIsenabled() {
        return isenabled;
    }

    public void setIsenabled(boolean isenabled) {
        this.isenabled = isenabled;
    }
}

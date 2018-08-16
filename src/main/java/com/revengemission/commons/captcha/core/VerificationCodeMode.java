package com.revengemission.commons.captcha.core;

/**
 * Created by zhang wanchao on 18-8-16.
 */
public enum VerificationCodeMode {
    NORMAL,//噪点和干扰线为0.05f和20,清晰化
    VAGUE,//噪点和干扰线为范围随机值0.05f ~ 0.1f和20 ~ 155,模糊化
    D3,//噪点和干扰线为范围随机值0.05f ~ 0.1f和20 ~ 155,3D中空自定义字体
    GIF,//噪点和干扰线为范围随机值0.05f ~ 0.1f和20 ~ 155,普通动态GIF
    GIF3D,//噪点和干扰线为范围随机值0.05f ~ 0.1f和20 ~ 155,3D动态GIF
    MIX,//噪点和干扰线为范围随机值0.05f ~ 0.1f和20 ~ 155,普通字体和3D字体混合
    MIXGIF//噪点和干扰线为范围随机值0.05f ~ 0.1f和20 ~ 155,混合动态GIF
}

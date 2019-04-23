package com.lv.reg.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface ActionOptions {
    void setActionButtons(SendMessage sendMessage);
    ActionOptions handleMessage(String message, SendMessage sendMessage);
}

package com.lv.reg.bot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class MessageHandler {
    Map<Long, ActionOptions> userToHandlerMap = new HashMap<>();

    public void handle(Long userId, SendMessage response, String request){
        if(request.equalsIgnoreCase("дякую"))
            userToHandlerMap.remove(userId);
        ActionOptions actionOptions = getHandlerForUser(userId);
        actionOptions.setActionButtons(response);
        ActionOptions actionOptionsNew = actionOptions.handleMessage(request, response);
        updateHandlerForUser(userId, actionOptionsNew);
    }

    public void updateHandlerForUser(Long userId, ActionOptions actionOptions){
        userToHandlerMap.put(userId, actionOptions);
    }

    public ActionOptions getHandlerForUser(Long userId){
        ActionOptions actionOptions = userToHandlerMap.get(userId);
        if(Objects.isNull(actionOptions))
            userToHandlerMap.put(userId, new InitialStage());
        return userToHandlerMap.get(userId);
    }
}

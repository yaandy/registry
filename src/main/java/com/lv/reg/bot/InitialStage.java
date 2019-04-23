package com.lv.reg.bot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class InitialStage implements ActionOptions {

    @Override
    public void setActionButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();

        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton("Про погоду"));
        keyboardRow.add(new KeyboardButton("Анекдот"));
        keyboardRow.add(new KeyboardButton("Попрацюєм"));

        keyboardRowList.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }

    @Override
    public ActionOptions handleMessage(String response, SendMessage sendMessage) {
        if(response.equals("Про погоду")){
            sendMessage.setText("Нехай проблеми та незгоди не роблять вам в житті погоди хай вам щастить");
            return this;
        }else if (response.equals("Анекдот")){
            sendMessage.setText("Тоьща целка");
            return this;
        }else if (response.equals("Попрацюєм")){
            sendMessage.setText("Ну давай");
            return new SecondStage();
        }else
            return this;
    }
}

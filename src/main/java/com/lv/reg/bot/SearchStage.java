package com.lv.reg.bot;

import com.lv.reg.entities.Contract;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.lv.reg.bot.SearchStage.SearchBy.NAME;
import static com.lv.reg.bot.SearchStage.SearchBy.PHONE;

@Component
public class SearchStage implements ActionOptions {
    SearchBy searchBy = null;
    Search search;
    @Autowired
    Search.SearchFactory searchFactory;
    @Autowired
    SecondStage secondStage;

    @Override
    public void setActionButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();

        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton(PHONE.getDisplayName()));
        keyboardRow.add(new KeyboardButton(SearchBy.NAME.getDisplayName()));
        keyboardRow.add(new KeyboardButton("Назад"));


        keyboardRowList.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }

    @Override
    public ActionOptions handleMessage(String response, SendMessage sendMessage) {
        if(response.equals("Назад")){
            sendMessage.setText("*ok*");
            return secondStage;
        }else if (response.equals(PHONE.getDisplayName())){
            searchBy = PHONE;
            sendMessage.setText("відправ номер телефону в повідомленні");
            search = searchFactory.getSearch(PHONE);
            return this;
        }else if(response.equals(NAME.getDisplayName())){
            searchBy = NAME;
            search = searchFactory.getSearch(NAME);
            sendMessage.setText("Поки не годна - приходь пізніше або попробуй інший варіант");
            return this;
        }else if(search != null){
            Iterable<Contract> contracts = search.performSearch(response);
            String msg = StreamSupport.stream(contracts.spliterator(), false)
                    .map(c -> String.format("Замовник %s сільрада %s статус %s", c.getCustomer().getName(), c.getVillageCouncil(), c.getOrderStatus()))
                    .collect(Collectors.joining("\n \n"));
            sendMessage.setText(msg);
            search = null;
            return this;
        }else {
            sendMessage.setText("і шо з тим робити ?");
            return this;
        }

    }

    @RequiredArgsConstructor
    @Getter
    public enum SearchBy{
        PHONE("Пошук по номеру"), NAME("Пошук по назві");
        private final String displayName;
    }
}

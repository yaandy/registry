package com.lv.reg.bot;

import com.lv.reg.entities.Contract;
import com.lv.reg.service.ContractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class LvZemSecretaryBot extends TelegramLongPollingBot {
    @Autowired
    ContractService contractService;

    @Override
    public void onUpdateReceived(Update update) {
        String text = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();
        String firstName = update.getMessage().getFrom().getFirstName();

        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        if (text.equals("привіт"))
            sendMessage.setText(String.format("Привіт %s, я Галя, ваша нова секретарка, вмію мало але може шось походу навчусь", firstName));
        else
            sendMessage.setText("Ти скаже чьо тє нада може дам шо ти хоч, можу видати інфо по договору - скажи чарівне слово");

        if (text.equals("договір")) {
            sendMessage.setText(getInfo());
            setButton(sendMessage);
        }

        log.info("onUdpate triggered + " + text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "LvZemSecretaryBot";
    }

    @Override
    public String getBotToken() {
        return "749753156:***";
    }

    private String getInfo() {
        Contract byId = contractService.findById(1l);
        return String.format("Договір #1 замовник %s, район %s, %s", byId.getCustomer(), byId.getDistrict(), byId.getOrderType());
    }

    private void setButton(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();

        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton("Шукати Договір"));
        keyboardRow.add(new KeyboardButton("Шукати Клієнта"));

        keyboardRowList.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }


}

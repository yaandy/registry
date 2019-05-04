package com.lv.reg.bot;

import com.lv.reg.entities.Contract;
import com.lv.reg.service.ContractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
//@Component
public class LvZemSecretaryBot extends TelegramLongPollingBot {
    @Autowired
    ContractService contractService;
    @Autowired
    InitialStage initialStage;
    @Autowired
    MessageHandler messageHandler;

    @Override
    public void onUpdateReceived(Update update) {
        String text = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();
        String firstName = update.getMessage().getFrom().getFirstName();

        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setReplyToMessageId(update.getMessage().getMessageId());

        messageHandler.handle(chatId, sendMessage, text);

        if (text.equalsIgnoreCase("привіт"))
            sendMessage.setText(String.format("Привіт %s, я Галя, ваша нова секретарка, вмію мало але здібна", firstName));

        log.info("onUdpate triggered + " + update.getMessage().getFrom().getId());
        log.info("msg <-" + update.getMessage().getText());
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
        return "749753156:AAGxxWHPPqXw3NPZfjnE53b4qgaCccJKCA4";
    }

    private String getInfo() {
        Contract byId = contractService.findById(1l);
        return String.format("Договір #1 замовник %s, район %s, %s", byId.getCustomer(), byId.getDistrict(), byId.getOrderType());
    }

}
